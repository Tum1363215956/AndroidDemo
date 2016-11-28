package com.tum.androiddemo.WIFI.WIFIDemo5.Service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private boolean isScan;
    private ExecutorService exec = Executors.newCachedThreadPool();

    private boolean  isDestory = false;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.i("TGA", "TGA 开启任务!");
            while(isScan&&!isDestory){
                if(wifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED){
                    return;
                }
                Log.i("TGA","TGA SCAN Start!");
                wifiAdmin.startScan();
                for (ScanResult result : wifiAdmin.getWifiList()) {
                    Log.i("TGA","TGA SCAN SSID:"+result.SSID);
                    if (result.SSID.equals("welldone")) {
                        handler.sendEmptyMessage(0);
                        return;
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        isScan = intent.getBooleanExtra("scan", false);
        exec.execute(runnable);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isDestory = true;
        super.onDestroy();
    }
}
