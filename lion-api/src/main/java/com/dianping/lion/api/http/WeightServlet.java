package com.dianping.lion.api.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.lion.entity.Environment;
import com.dianping.lion.entity.OperationLog;
import com.dianping.lion.entity.OperationTypeEnum;
import com.dianping.lion.entity.User;
import com.dianping.lion.service.ServiceService;

public class WeightServlet extends AbstractLionServlet {

	private static final String DEFAULT_PORT = "" + ServiceService.DEFAULT_PORT;
	
	private enum Key {env, id, ip, port, weight};
	
	private enum Action {get, set};
	
	public WeightServlet () {
		this.requestIdentityRequired = false;
	}
	
	/*
	 * Previously, weight related interface is provided by lion-api and 
	 * used by OP startup/shutdown script to achieve smooth server startup/shutdown.
	 * 
	 * To keep compatible with lion-acl, the output format is:
	 *     STATUS_CODE|CONTENT
	 * For STATUS_CODE:
	 *     0 - Success
	 *     1 - Failure
	 *     
	 * !!!This code only works when there is only one application deployed 
	 * on a single server, which the common case and sense in production environment.
	 * 
	 * Weight is directly read from and write to Zookeeper, which will not be the case
	 * in pigeon-governor, which has a local DB to cache the data.
	 */
    @Override
	protected void doService(HttpServletRequest request, HttpServletResponse response, String querystr) throws Exception {
		String pathInfo = request.getPathInfo();
		if(pathInfo == null) 
			throw new RuntimeException("action is null");
		
		pathInfo = pathInfo.toLowerCase().substring(1);
		Action action = null;
		try {
			action = Action.valueOf(pathInfo);
		} catch(RuntimeException e) {
			throw new RuntimeException("unknown action " + pathInfo);
		}
		
		String env, id, ip, port, weight = null;
		int result = -1;
		switch (action) {
		case get:
			// getWeight(env, ip, port)
			env = getParam(request, Key.env);
			ip = getParam(request, Key.ip);
			port = getParam(request, Key.port, DEFAULT_PORT);
			result = getWeight(env, ip, port);
			response.getWriter().write("0|" + result);
			break;
		case set:
			// setWeight(env, id, ip, port, weight)
			env = getParam(request, Key.env);
			id = getParam(request, Key.id);
			ip = getParam(request, Key.ip);
			port = getParam(request, Key.port, DEFAULT_PORT);
			weight = getParam(request, Key.weight);
			result = setWeight(env, id, ip, port, weight);
			response.getWriter().write("0|" + result);
			break;
		}
	}

	private int getWeight(String env, String ip, String port) throws Exception {
		int envId = getEnvId(env);
		int portNo = Integer.parseInt(port);
		int result = serviceService.getWeight(envId, ip, portNo);
		return result;
	}

	private int setWeight(String env, String id, String ip, String port,
			String weight) throws Exception {
		int envId = getEnvId(env);
		verifyIdentity(id);
		int portNo = Integer.parseInt(port);
		int weightNo = Integer.parseInt(weight);
		try {
			int result = serviceService.setWeight(envId, ip, portNo, weightNo);
			operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_SetWeight, null, envId,
			        "成功: " + "设置实例" + ip + ":" + port + "权重为" + weight).key(ip + ":" + port, "true", null, null, null));
			return result;
		} catch (Exception ex) {
			operationLogService.createOpLog(new OperationLog(OperationTypeEnum.API_SetWeight, null, envId,
			        "失败: " + "设置实例" + ip + ":" + port + "权重为" + weight).key(ip + ":" + port, "false", null, null, null));
			throw ex;
		}
	}

	private void verifyIdentity(String id) {
		int userId = 0;
		try {
			userId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid id " + id);
		}
		User user = userService.findById(userId);
		if (user == null) {
			throw new RuntimeException("Invalid id " + id);
		}
		if (!user.isSystem()) {
			throw new RuntimeException("Only support user with system level");
		}
	}

	private int getEnvId(String env) {
		Environment environment = environmentService.findEnvByName(env);
		if (environment == null) {
			throw new RuntimeException("Invalid environment " + env);
		}
		return environment.getId();
	}

	// TODO URL decode
	private String getParam(HttpServletRequest request, Key key) {
		String param = request.getParameter(key.name());
		if(param == null)
			throw new RuntimeException(key.name() + " is null");
		return param;
	}
	
	private String getParam(HttpServletRequest request, Key key, String defaultValue) {
		String param = request.getParameter(key.name());
		return param == null ? defaultValue : param;
	}

	
}
