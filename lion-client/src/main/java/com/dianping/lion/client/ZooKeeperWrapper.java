/**
 *
 */
package com.dianping.lion.client;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.ConnectionLossException;
import org.apache.zookeeper.KeeperException.SessionExpiredException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

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

    private static Logger logger = Logger.getLogger(ZooKeeperWrapper.class);

    private ZooKeeper zk;

    private String addresses;
    private int timeout;
    private Watcher watcher;
    private volatile CountDownLatch latch;

    private Map<String, Watcher> watcherMap = new ConcurrentHashMap<String, Watcher>();

    protected ZooKeeperWrapper() {
    }

    public ZooKeeperWrapper(String addresses, int timeout, final Watcher watcher) throws IOException,
            InterruptedException {
        this.addresses = addresses;
        this.timeout = timeout;
        this.watcher = watcher;
        this.latch = new CountDownLatch(1);
        this.zk = new ZooKeeper(addresses, timeout, this);
    }

    private void init(ZooKeeper zk) throws IOException, KeeperException, InterruptedException {
        if(zk != this.zk) 
            return;
        
        Random random = new Random();
        Thread.sleep(random.nextInt(2000));
        while(true) {
            try {
                this.zk = new ZooKeeper(addresses, timeout, this);
                waitUntilConnected();
                break;
            } catch(IOException e) {
                logger.error("failed to connect to zookeeper " + this.addresses, e);
                Thread.sleep(5000);
            }
        }
        
        for (Entry<String, Watcher> entry : watcherMap.entrySet()) {
            this.zk.getData(entry.getKey(), entry.getValue(), null);
        }
    }

    public void close() throws InterruptedException {
        if (zk != null) {
            zk.close();
            zk = null;
        }
    }

    private void reconnectSession() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk_ = this.zk;
        zk_.close();
        init(zk_);
    }
    
    public Stat exists(String path, boolean watch) throws KeeperException, InterruptedException, IOException {
        while (true) {
            try {
                Stat stat = zk.exists(path, watch);
                return stat;
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public Stat exists(String path, Watcher watcher) throws KeeperException, InterruptedException, IOException {
        while (true) {
            try {
                Stat stat = zk.exists(path, watcher);
                if (watcher != null)
                    this.watcherMap.put(path, watcher);
                return stat;
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public String create(String path, byte[] data, List<ACL> acl, CreateMode createMode) throws KeeperException,
            InterruptedException, IOException {
        while (true) {
            try {
                return zk.create(path, data, acl, createMode);
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public byte[] getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException,
            IOException {
        while (true) {
            try {
                byte[] data = zk.getData(path, watcher, stat);
                if (watcher != null)
                    this.watcherMap.put(path, watcher);
                return data;
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public byte[] getData(String path, boolean watch, Stat stat) throws KeeperException, InterruptedException,
            IOException {
        while (true) {
            try {
                return zk.getData(path, watch, stat);
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public Stat setData(String path, byte[] data, int version) throws KeeperException, InterruptedException,
            IOException {
        while (true) {
            try {
                return zk.setData(path, data, version);
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException,
            IOException {
        while (true) {
            try {
                return zk.getChildren(path, watch);
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public void delete(String path, int version) throws KeeperException, InterruptedException, IOException {
        while (true) {
            try {
                zk.delete(path, version);
                return;
            } catch (ConnectionLossException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            } catch (SessionExpiredException e) {
                logger.info(e.getMessage());
                waitUntilConnected();
            }
        }
    }

    public void removeWatcher(String path) {
        this.watcherMap.remove(path);
    }

    /**
     * @return the addresses
     */
    public String getAddresses() {
        return addresses;
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getType() == EventType.None) {
            try {
                processStateChange(event);
            } catch (Exception e) {
                logger.error("", e);
            }
            return;
        }

        if (watcher != null) {
            watcher.process(event);
        }
    }

    private void processStateChange(WatchedEvent event) throws InterruptedException, IOException,
            KeeperException {
        switch (event.getState()) {
        case SyncConnected:
            logger.info("Connected to zookeeper " + zk);
            latch.countDown();
            break;
        case Disconnected:
            logger.info("Disconnected from zookeeper " + zk);
            latch = new CountDownLatch(1);
            break;
        case Expired:
            logger.info("Zookeeper session expired " + zk);
            reconnectSession();
            break;
        }
    }

    private void waitUntilConnected() throws IOException, InterruptedException {
        if (!latch.await(60, TimeUnit.SECONDS)) {
            throw new IOException("Timeout while connecting to zookeeper " + addresses);
        }
    }
}
