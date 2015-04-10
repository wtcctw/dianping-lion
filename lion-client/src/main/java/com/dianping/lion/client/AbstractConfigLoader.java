package com.dianping.lion.client;

public abstract class AbstractConfigLoader implements ConfigLoader {

    protected ConfigListener configListener = null;

    @Override
    public void addConfigListener(ConfigListener configListener) {
        this.configListener = configListener;
    }

    @Override
    public void removeConfigListener(ConfigListener configListener) {
        this.configListener = null;
    }

}
