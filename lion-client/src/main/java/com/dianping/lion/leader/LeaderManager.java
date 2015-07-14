package com.dianping.lion.leader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianping.lion.Environment;
import com.dianping.lion.client.ConfigCache;

public class LeaderManager {

    private static Logger logger = LoggerFactory.getLogger(LeaderManager.class);
    
    private static int SLEEP_INTERVAL = 1000;
    
    private static Map<String, LeaderManager> instanceMap = new HashMap<String, LeaderManager>();
    
    private String zookeeperAddress;
    private CuratorFramework client;
    
    private static AtomicInteger idGenerator = new AtomicInteger(0);
    
    private LeaderManager(String zookeeperAddress) {
        this.zookeeperAddress = zookeeperAddress;
        
        client = CuratorFrameworkFactory.newClient(zookeeperAddress, 60*1000, 30*1000, 
                new ExponentialBackoffRetry(1000, 3));
        
        client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("leader zookeeper state changed to {}", newState);
            }
        });
        
        client.start();
    };
    
    private static String getZookeeperAddress() {
        String zkAddr;
        try {
            zkAddr = ConfigCache.getInstance().getProperty("lion.leader.zookeeper.address");
            if(zkAddr == null) {
                zkAddr = Environment.getZKAddress();
            }
        } catch(Exception e) {
            logger.error("failed to get lion.leader.zookeeper.address", e);
            zkAddr = Environment.getZKAddress();
        }
        return zkAddr;
    }

    public static LeaderManager getInstance() {
        return getInstance(getZookeeperAddress());
    }
    
    public static LeaderManager getInstance(String zookeeperAddress) {
        LeaderManager lm = instanceMap.get(zookeeperAddress);
        if(lm == null) {
            synchronized(instanceMap) {
                lm = instanceMap.get(zookeeperAddress);
                if(lm == null) {
                    lm = new LeaderManager(zookeeperAddress);
                    instanceMap.put(zookeeperAddress, lm);
                }
            }
        }
        return lm;
    }
    
    public LeaderLock takeLeadership(final String lockName, final LeaderCallback callback) {
        String leaderPath = getLeaderPath(lockName);
        final String selectorId = lockName + "-" + idGenerator.incrementAndGet();
        
        LeaderSelectorListener leaderListener = new LeaderSelectorListenerAdapter() {
            
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                try {
                    logger.debug(selectorId + " is taking leader lock: " + lockName + 
                            ", zookeeper address: " + zookeeperAddress);
                    callback.takeLeadership();
                    logger.info(selectorId + " has taken leader lock: " + lockName + 
                            ", zookeeper address: " + zookeeperAddress);
                } catch(Exception e) {
                    logger.error(selectorId + " failed to take leader lock: " + lockName + 
                            ", zookeeper address: " + zookeeperAddress, e);
                    callback.loseLeadership();
                    throw e;
                }
                // need to keeper this method from return, return means release the leader lock
                while(!Thread.interrupted()) {
                    try {
                        Thread.sleep(SLEEP_INTERVAL);
                    } catch(Exception e) {
                        logger.warn(selectorId + " is releasing leader lock: " + lockName + 
                                ", zookeeper address: " + zookeeperAddress);
                        callback.loseLeadership();
                        throw e;
                    }
                }
                logger.warn(selectorId + " is releasing leader lock: " + lockName + 
                            ", zookeeper address: " + zookeeperAddress);
                callback.loseLeadership();
            }
            
        };
        
        LeaderSelector leaderSelector = new LeaderSelector(client, leaderPath, leaderListener);
        leaderSelector.autoRequeue();
        leaderSelector.setId(selectorId);
        leaderSelector.start();
        LeaderLock leaderLock = new LeaderLock(lockName, leaderSelector);
        return leaderLock;
    }
    
    /**
     * return the leader lock path
     * 
     * Leader path pattern: /dp/leader/${app.name}/${lock.name}
     * 
     * @param lockName
     * @return
     */
    static String getLeaderPath(String lockName) {
        if(lockName == null) {
            throw new NullPointerException("lock name is null");
        }
        StringBuilder path = new StringBuilder(64);
        path.append("/dp/leader/");
        String appName = Environment.getAppName();
        if(appName == null) {
            logger.warn("app name is null, use \"default\"");
            appName = "default";
        }
        path.append(appName).append('/').append(lockName);
        return path.toString();
    }

    public void releaseLeadership(LeaderLock lock) {
        LeaderSelector ls = lock.getLeaderSelector();
        if(ls.hasLeadership()) {
            ls.interruptLeadership();
        }
        ls.close();
    }
    
}
