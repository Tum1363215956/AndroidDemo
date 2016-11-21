package com.tum.androiddemo.WIFI.WIFIDemo5.Service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.IBinder;

import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;

import java.util.Timer;
import java.util.TimerTask;

public class AutoConnService extends Service {

    private WifiAdmin wifiAdmin;

    private Timer timer = new Timer();
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            wifiAdmin.startScan();
            for(ScanResult result:wifiAdmin.getWifiList()){
                if(result.SSID.equals("welldone")){
                    Intent intent = new Intent();
                    intent.setAction("com.tum.wifi.statechange.autoconnect");
                    AutoConnService.this.sendBroadcast(intent);
                    task.cancel();
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

        if(wifiAdmin == null){
            wifiAdmin = WifiAdmin.getInstance(getApplicationContext());
        }
        //启动扫描任务
        boolean isScan = intent.getBooleanExtra("scan",false);
        if(isScan){
           timer.schedule(task,1000,5000);
        }else{//取消
            task.cancel();
        }
        return super.onStartCommand(intent, flags, startId);
    }


}
