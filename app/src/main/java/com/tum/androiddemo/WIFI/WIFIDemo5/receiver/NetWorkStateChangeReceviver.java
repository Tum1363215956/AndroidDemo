package com.tum.androiddemo.WIFI.WIFIDemo5.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络状态监听
 * 资料:http://blog.csdn.net/lonely_fireworks/article/details/7373166
 * Created by Administrator on 2016/11/15 0015.
 */
public class NetWorkStateChangeReceviver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkInfo.State wifiState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        Log.i("TGA-------->","============ NetWorkStateChangeReceviver ================");
        if(wifiState != null && NetworkInfo.State.CONNECTED == wifiState){
            Log.i("TGA-------->","TGA wifi 已经打开:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.CONNECTING == wifiState){
            Log.i("TGA-------->","TGA wifi 正在*********打开:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.SUSPENDED == wifiState){
            Log.i("TGA-------->","TGA wifi 有冲突:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.DISCONNECTING == wifiState){
            Log.i("TGA-------->","TGA wifi 正在******关闭:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.DISCONNECTED == wifiState){
            Log.i("TGA-------->","TGA wifi 已经关闭:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.UNKNOWN == wifiState){
            Log.i("TGA-------->","TGA wifi 未知:"+wifiState);
        }
    }
}
