package com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.server;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class WebCofiguration {
    private int port;//端口
    private int maxParallels;//最大连接数

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
