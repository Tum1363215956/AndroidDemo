package com.tum.androiddemo.WIFI.WIFIDEMO1.wifiutil;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;

/**
 * wifi Tools class
 * Created by Administrator on 2016/11/10 0010.
 */
public class WifiAdmin {

    private WifiManager mWifiManager;//定义WifiManager对象
    private WifiInfo mWifiInfo;//定义WifiInfo对象
    private List<ScanResult> mWifiList;//扫描出的网络连接列表
    private List<WifiConfiguration> mWifiConfiguration;//网络连接列表
    WifiManager.WifiLock  mWifiLock;

    //构造器
    public WifiAdmin(Context context){
        mWifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);//取得WifiManager对象
        mWifiInfo = mWifiManager.getConnectionInfo();//取得WifiInfo对象
    }

    //打开wifi
    public void openWifi(){
       if(!mWifiManager.isWifiEnabled()){
           mWifiManager.setWifiEnabled(true);
       }
    }
    //关闭wifi
    public void closeWifi(){
        if(mWifiManager.isWifiEnabled()){
            mWifiManager.setWifiEnabled(false);
        }
    }

    //锁定WifiLock，当下载大文件时需要锁定
    public void AcquireWifiLock(){
        mWifiLock.acquire();
    }

    //解锁WifiLock————有问题
    public void ReleaseWifiLock() {
        if(mWifiLock.isHeld()){//判断是否锁定
            mWifiLock.acquire();
        }
    }

    //创建一个wifilock
    public void createWifiLock(){
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    //得到配置好的网络
    public List<WifiConfiguration> getmWifiConfiguration(){
        return mWifiConfiguration;
    }

    //指定配置好的网络进行连接
    public void connectConfiguration(int index){
        //索引大于配置好的网络，索引返回
        if(index > mWifiConfiguration.size()){
            return;
        }
        //连接配置好的指定ID的网络
        mWifiManager.enableNetwork(mWifiConfiguration.get(index).networkId,true);
    }

    //扫描wifi
    public void StartScan(){
        mWifiManager.startScan();
        //得到扫描的结果
        mWifiList = mWifiManager.getScanResults();
        //得到配置好的网络连接
        mWifiConfiguration = mWifiManager.getConfiguredNetworks();
    }

    //得到网络列表
    public List<ScanResult> getmWifiList(){
        return mWifiList;
    }

    //查看扫描结果
    public StringBuilder lookUpScan(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<mWifiList.size();i++){
            sb.append("index"+new Integer(i+1).toString()+":");
            //将ScanResult信息转换成一个字符串包
            //其中包括：BSSID、SSID、capabilities\frequency、level
            sb.append(mWifiList.get(i).toString());
            sb.append("\n");
        }
        return sb;
    }

    //得到MAC地址
    public String GetMacAddress(){
        return (mWifiInfo == null)?"NULL":mWifiInfo.getMacAddress();
    }

    //得到接入点的BSSID
    public String getBSSID(){
        return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();
    }

    //得到IP地址
    public int GetIPAddress(){
        return (mWifiInfo == null) ? 0:mWifiInfo.getIpAddress();
    }

    //得到连接的ID
    public int getNetWorkId(){
        return (mWifiInfo == null)?0:mWifiInfo.getNetworkId();
    }

    //得到WifiInfo的所有信息包
    public String getWifiInfo(){
        return (mWifiInfo == null)?"NULL":mWifiInfo.toString();
    }

    //添加一个网络连接
    public void AddNetWork(WifiConfiguration wcg){
        int wcgID = mWifiManager.addNetwork(wcg);
        mWifiManager.enableNetwork(wcgID,true);
    }

    //断开指定的ID的网络
    public void DisConnectWifi(int netId){
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
}
