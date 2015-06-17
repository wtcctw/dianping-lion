package com.dianping.lion.leader;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dianping.lion.log.LoggerLoader;

public class LeaderManagerTest {

    static {
        LoggerLoader.init();
    }
    
    @Test
    public void test() {
        LeaderManager leaderManager = LeaderManager.getInstance();
        assertNotNull(leaderManager);
    }

    @Test 
    public void testGetLeaderPath() {
        String leaderPath = LeaderManager.getLeaderPath("hello");
        assertEquals(leaderPath, "/dp/leader/default/hello");
    }
    
    @Test
    public void testLock() throws Exception {
        List<LeaderLock> locks = new ArrayList<LeaderLock>();
        final LeaderLock lock1 = LeaderManager.getInstance("zookeeper01.beta").takeLeadership("testLock", new LeaderCallback() {

            @Override
            public void takeLeadership() throws Exception {
                Thread.sleep(1000);
            }

            @Override
            public void loseLeadership() throws Exception {
                Thread.sleep(1000);
            }
            
        });
        locks.add(lock1);
        final LeaderLock lock2 = LeaderManager.getInstance("zookeeper02.beta").takeLeadership("testLock", new LeaderCallback() {

            @Override
            public void takeLeadership() throws Exception {
                Thread.sleep(1000);
            }

            @Override
            public void loseLeadership() throws Exception {
                Thread.sleep(1000);
            }
            
        });
        locks.add(lock2);
        final LeaderLock lock3 = LeaderManager.getInstance("zookeeper03.beta").takeLeadership("testLock", new LeaderCallback() {
            
            @Override
            public void takeLeadership() throws Exception {
                Thread.sleep(1000);
            }

            @Override
            public void loseLeadership() throws Exception {
                Thread.sleep(1000);
            }
            
        });
        locks.add(lock3);
        for(int i=0; i<1000; i++) {
            Thread.sleep(3000);
            LeaderLock currentLeader = null;
            for(LeaderLock lock : locks) {
                System.out.println(lock + (lock.hasLeadership() ? "*" : ""));
            }
            System.out.println("==============================");
        };
    }
}
