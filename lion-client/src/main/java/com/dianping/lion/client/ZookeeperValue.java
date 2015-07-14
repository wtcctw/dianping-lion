package com.dianping.lion.client;

import org.codehaus.plexus.util.StringUtils;

public class ZookeeperValue {

    public static final ZookeeperValue NOT_EXIST_VALUE = new ZookeeperValue();
    
    private String value;
    private int channel;
    private long timestamp;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ZookeeperValue))
            return false;
        if (object == this)
            return true;
        
        ZookeeperValue zkValue = (ZookeeperValue) object;
        if(channel != zkValue.getChannel())
            return false;
        if(!StringUtils.equals(value, zkValue.getValue())) 
            return false;
        if(timestamp != zkValue.getTimestamp())
            return false;
        return true;
    }
    
    public int hashCode() {
        int hash = 137;
        hash = hash * 17 + channel;
        hash = hash * 17 + (value == null ? 0 : value.hashCode());
        hash = hash * 17 + (int)(timestamp ^ (timestamp >>> 32));
        return hash;
    }
    
    public String toString() {
        return String.format("ZookeeperValue[value=%s, timestamp=%s, channel=%s]", value, timestamp, channel);
    }

}
