package com.tum.androiddemo.WIFI.WIFIDemo5.Thread;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.util.Log;

import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class AutoConnectThread implements Runnable {

    //扫描、配置过的wifi信息
    private List<ScanResult> scanResultList;
    private List<WifiConfiguration> wifiConfigList;

    private boolean mCanAtuoConnect = false;//可以自动连接

    //WifiConfigutate的SSID的格式为："\"" + wifi名称 + "\""
    private final static String CONFIGURATESSID = "\"" + "welldone" + "\"";
    //ScanResult的SSID的格式为："wifi名称"
    private final static String SCANSSID = "welldone";

    // 表示是否在可连接范围内
    private boolean canConnectable = false;
    // 表示wifi是否已经配置过了
    private boolean isConfigurated = false;
    //判断是否连接上制定的wifi
    private boolean isConnected = false;
    private int networkId;

    private WifiAdmin mWifiAdmin;//Wifi封装

    private Handler mHandler;


    public AutoConnectThread(List<ScanResult> scanWifiList, List<WifiConfiguration> wifiConfigList, boolean mCanAtuoConnect, WifiAdmin mWifiAdmin, Handler mHandler) {
        this.scanResultList = scanWifiList;
        this.wifiConfigList = wifiConfigList;
        this.mCanAtuoConnect = mCanAtuoConnect;
        this.mWifiAdmin = mWifiAdmin;
        this.mHandler = mHandler;
    }

    /**
     * 关闭线程
     */
    public void closeThread() {
        mCanAtuoConnect = false;
    }

    @Override
    public void run() {
        if (scanResultList != null) {
            for (int i = 0; i < scanResultList.size(); i++) {
                System.out.println("scanResultList" + i + "----->"
                        + scanResultList.get(i).SSID);
                if (scanResultList.get(i).SSID.equals(SCANSSID)) {
                    canConnectable = true;
                    break;
                }
            }
            if (canConnectable) {
                Log.i("TGA---------->", "TGA 在可以连接范围内");
                if (wifiConfigList != null) {
                    Log.i("TGA-------->", "TGA ==================================");
                    for (int j = 0; j < wifiConfigList.size(); j++) {
                        Log.i("TGA ----------->", "wifiConfigList:" + j + "," + wifiConfigList.get(j).SSID);
                        if (wifiConfigList.get(j).SSID.equals(CONFIGURATESSID)) {
                            isConfigurated = true;
                            networkId = wifiConfigList.get(j).networkId;
                            Log.i("TGA---------->", "TGA networkId为:" + networkId);
                            break;
                        }
                    }

                    Log.i("TGA------------>", "TGA ===================================");
                    if (isConfigurated) {
                        Log.i("TGA---------------->", "TGA 连接前");
                        boolean isConnSuccess = mWifiAdmin.connectWifi(networkId);
                        Log.i("TGA---------------->", "TGA 连接后");
                        if (isConnSuccess) {
                            mHandler.sendEmptyMessage(1);
                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    }
                }
            }
        }

    }
}
