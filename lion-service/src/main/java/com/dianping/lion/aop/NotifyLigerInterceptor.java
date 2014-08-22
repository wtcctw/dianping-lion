package com.dianping.lion.aop;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianping.lion.entity.Config;
import com.dianping.lion.entity.ConfigInstance;
import com.dianping.lion.entity.Environment;
import com.dianping.lion.register.ConfigRegisterService;
import com.dianping.lion.register.ConfigRegisterServiceRepository;
import com.dianping.lion.service.ConfigService;
import com.dianping.lion.service.EnvironmentService;

public class NotifyLigerInterceptor implements MethodInterceptor {

    private static Logger logger = Logger.getLogger(NotifyLigerInterceptor.class);
    
    @Autowired
    private ConfigRegisterServiceRepository registerServiceRepository;
    
    @Autowired
    private EnvironmentService environmentService;
    
    @Autowired
    private ConfigService configService;
    
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 20, TimeUnit.SECONDS, new ArrayBlockingQueue(10), new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            // safely ingnore it
        }
    });
    
    private HttpClient httpClient;
    
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        Object result = invocation.proceed();
        executor.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    notifyLiger(invocation);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                } catch (Exception e) {
                    logger.error("failed to notify liger", e);
                }
            }
            
        });
        return result;
    }

    private void notifyLiger(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        if(methodName.equals("createInstance")) {
            Object[] arguments = invocation.getArguments();
            ConfigInstance ci = (ConfigInstance) arguments[0];
            Config config = configService.getConfig(ci.getConfigId());
            notifyLiger("create", ci.getEnvId(), config.getKey(), ci.getContext());
        } else if(methodName.equals("setConfigValue")) {
            Object[] arguments = invocation.getArguments();
            if(arguments.length == 4 || arguments.length == 5) {
                Config config = configService.getConfig((Integer) arguments[0]);
                notifyLiger("update", (Integer)arguments[1], config.getKey(), (String)arguments[2]);
            }
        } else if(methodName.equals("deleteInstance")) {
            Object[] arguments = invocation.getArguments();
            if(arguments.length == 3) {
                Config config = configService.getConfig((Integer) arguments[0]);
                notifyLiger("delete", (Integer)arguments[1], config.getKey(), (String)arguments[2]);
            }
        } else if(methodName.equals("deleteInstances")) {
            Object[] arguments = invocation.getArguments();
            if(arguments.length == 2) {
                Config config = null;
                if(!(arguments[0] instanceof Config)) {
                    config = configService.getConfig((Integer) arguments[0]);
                } else {
                    config = (Config)arguments[0];
                }
                notifyLiger("delete", (Integer)arguments[1], config.getKey(), "");
            }
        } else if(methodName.equals("updateInstance")) {
            Object[] arguments = invocation.getArguments();
            ConfigInstance ci = (ConfigInstance) arguments[0];
            Config config = configService.getConfig(ci.getConfigId());
            notifyLiger("update", ci.getEnvId(), config.getKey(), ci.getContext());
        }
    }

    void notifyLiger(String type, int envId, String key, String group) {
        ConfigRegisterService registerService = registerServiceRepository.getRegisterService(envId);
        String enabled = registerService.get("lion-console.liger.notify.enabled");
        if(enabled == null || !enabled.equals("true"))
            return;
        String ligerNotifyUrl = registerService.get("lion-console.liger.notify.url");
        if(ligerNotifyUrl == null)
            return;
        String url = generateUrl(ligerNotifyUrl, type, envId, key, group);
        String content;
        try {
            content = doHttpGet(url);
            if(logger.isInfoEnabled()) {
                logger.info("notify liger url: " + url + ", response: " + content);
            }
        } catch (Exception e) {
            logger.error("failed to notify liger, url " + url, e);
        }
    }

    private String doHttpGet(String url) throws Exception {
        GetMethod get = new GetMethod(url);
        HttpClient httpClient = getHttpClient();
        try {
            httpClient.executeMethod(get);
            return get.getResponseBodyAsString();
        } finally {
            get.releaseConnection();
        }
    }

    private String generateUrl(String ligerNotifyUrl, String type, int envId, String key, String group) {
        StringBuilder url = new StringBuilder(ligerNotifyUrl);
        url.append("&type=").append(type);
        Environment environment = environmentService.findEnvByID(envId);
        url.append("&env=").append(environment.getName());
        url.append("&key=").append(key);
        if(group == null) {
            group = "";
        }
        url.append("&group=").append(group.trim());
        return url.toString();
    }
    
    private HttpClient getHttpClient() {
        if(httpClient == null) {
            HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams params = new HttpConnectionManagerParams();
            params.setMaxTotalConnections(500);
            params.setDefaultMaxConnectionsPerHost(10);
            params.setConnectionTimeout(3000);
            params.setTcpNoDelay(true);
            params.setSoTimeout(3000);
            params.setStaleCheckingEnabled(true);
            connectionManager.setParams(params);
            
            httpClient = new HttpClient(connectionManager);
        }
        return httpClient;
    }
}
