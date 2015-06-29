package com.dianping.lion.leader;

public interface LeaderCallback {

    /**
     * If leadership is taken, this method will be invoked.
     * If any exception is thrown from this method, leadership is released.
     * 
     * @throws Exception
     */
    public void takeLeadership() throws Exception;
    
    /**
     * Get invoked when leadership will be lost immediately.
     * 
     * @throws Exception
     */
    public void loseLeadership() throws Exception;
    
}
