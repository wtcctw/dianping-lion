/**
 *
 */
package com.dianping.lion.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 * Title: ZooKeeperWrapper.java
 * </p>
 * <p>
 * Description: 描述
 * </p>
 * 
 * @author saber miao
 * @version 1.0
 * @created 2011-7-14 下午05:38:01
 */
public class ZooKeeperWrapper implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(ZooKeeperWrapper.class);

    private CuratorFramework zk;

    private Watcher watcher;

    private Map<String, Watcher> watcherMap = new ConcurrentHashMap<String, Watcher>();

    protected ZooKeeperWrapper() {
    }

    public ZooKeeperWrapper(String addresses, int timeout, final Watcher watcher) throws IOException, InterruptedException {
        this.watcher = watcher;
        
        this.zk = CuratorFrameworkFactory.newClient(addresses, 30*1000, 30*1000, 
                new RetryNTimes(Integer.MAX_VALUE, 1000));
        
        this.zk.getConnectionStateListenable().addListener(new ConnectionStateListener() {
            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                logger.info("zookeeper state changed to " + newState);
                if (newState == ConnectionState.LOST) {
                    while (true) {
                        try {
                            if (client.getZookeeperClient().blockUntilConnectedOrTimedOut()) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            break;
                        } catch (Exception e) {
                            logger.error("error with zookeeper connection:" + e.getMessage());
                        }
                    }
                } else if (newState == ConnectionState.RECONNECTED) {
                    // Re-watch all path
                    for (Entry<String, Watcher> entry : watcherMap.entrySet()) {
                        try {
                            getData(entry.getKey(), entry.getValue(), null);
                        } catch (Exception e) {
                            logger.error("failed to get data of " + entry.getKey(), e);
                        }
                    }
                }
            }
        });
        
        this.zk.getCuratorListenable().addListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                if(watcher != null && event != null) {
                    watcher.process(event.getWatchedEvent());
                }
            }
        });
        
        this.zk.start();
        this.zk.getZookeeperClient().blockUntilConnectedOrTimedOut();
    }

    public void close() throws InterruptedException {
        if (zk != null) {
            zk.close();
            zk = null;
        }
    }

    public Stat exists(String path, boolean watch) throws Exception {
        if(watch) {
            return zk.checkExists().watched().forPath(path);
        } else {
            return zk.checkExists().forPath(path);
        }
    }

    public Stat exists(String path, Watcher watcher) throws Exception {
        if (watcher != null)
            watcherMap.put(path, watcher);
        return zk.checkExists().usingWatcher(watcher).forPath(path);
    }

    public String create(String path, byte[] data, List<ACL> acl, CreateMode createMode) throws Exception {
        return zk.create().creatingParentsIfNeeded().withMode(createMode).withACL(acl).forPath(path, data);
    }

    public byte[] getData(String path, Watcher watcher, Stat stat) throws Exception {
        if (watcher != null)
            this.watcherMap.put(path, watcher);
        return zk.getData().storingStatIn(stat).usingWatcher(watcher).forPath(path);
    }

    public byte[] getData(String path, boolean watch, Stat stat) throws Exception {
        byte[] data = null;
        if(watch) {
            data = zk.getData().storingStatIn(stat).watched().forPath(path);
        } else {
            data = zk.getData().storingStatIn(stat).forPath(path);
        }
        return data;
    }

    public Stat setData(String path, byte[] data, int version) throws Exception {
        return zk.setData().withVersion(version).forPath(path, data);
    }

    public List<String> getChildren(String path, boolean watch) throws Exception {
        if(watch) {
            return zk.getChildren().watched().forPath(path);
        } else {
            return zk.getChildren().forPath(path);
        }
    }

    public void delete(String path, int version) throws Exception {
        zk.delete().withVersion(version).forPath(path);
    }

    public void removeWatcher(String path) {
        this.watcherMap.remove(path);
    }

    @Override
    public void process(WatchedEvent event) {
        if (watcher != null) {
            watcher.process(event);
        }
    }

}
