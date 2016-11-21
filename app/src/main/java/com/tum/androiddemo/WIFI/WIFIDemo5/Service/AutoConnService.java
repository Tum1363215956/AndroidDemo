package com.tum.androiddemo.WIFI.WIFIDemo5.Service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;

import java.util.Timer;
import java.util.TimerTask;

public class AutoConnService extends Service {

    private WifiAdmin wifiAdmin;

    private boolean isScanning = false;//正在扫描

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            intent.setAction("com.tum.wifi.statechange.autoconnect");
            AutoConnService.this.sendBroadcast(intent);
        }
    };

    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Log.i("TGA","TGA SCAN Start!");
            wifiAdmin.startScan();
            for (ScanResult result : wifiAdmin.getWifiList()) {
                Log.i("TGA","TGA SCAN SSID:"+result.SSID);
                if (result.SSID.equals("welldone")) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                    isScanning = false;
                    timer.cancel();
                    break;
                }
            }
        }
    };

    public AutoConnService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (wifiAdmin == null) {
            wifiAdmin = WifiAdmin.getInstance(getApplicationContext());
        }
        Log.i("TGA", "TGA 开启服务!");
        //启动扫描任务
        boolean isScan = intent.getBooleanExtra("scan", false);
        if (isScan&&!isScanning) {
            Log.i("TGA", "TGA 开启检查!");
            timer.schedule(task, 0, 1000);
            isScanning = true;
        } else if(isScan == false){//取消
            Log.i("TGA", "TGA 关闭检查任务!");
            if(isScanning) {
                timer.cancel();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private class ScanThread extends Thread{

        @Override
        public void run() {
            while(isScanning){
                
            }
        }
    }


}
