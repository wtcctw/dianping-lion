package com.dianping.lion.leader;

import org.apache.curator.framework.recipes.leader.LeaderSelector;

public class LeaderLock {

    private String lockName;
    private LeaderSelector leaderSelector;
    
    public LeaderLock(String lockName, LeaderSelector leaderSelector) {
        this.lockName = lockName;
        this.leaderSelector = leaderSelector;
    }

    public String getLockName() {
        return lockName;
    }
    
    public String getId() {
        return leaderSelector.getId();
    };
    
    public boolean hasLeadership() {
        return leaderSelector.hasLeadership();
    };
    
    LeaderSelector getLeaderSelector() {
        return leaderSelector;
    }
    
    public String toString() {
        return leaderSelector.getId() + ":" + leaderSelector.hasLeadership();
    }
    
}
