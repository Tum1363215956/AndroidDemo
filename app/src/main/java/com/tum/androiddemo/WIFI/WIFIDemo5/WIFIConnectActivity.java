package com.tum.androiddemo.WIFI.WIFIDemo5;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tum.androiddemo.R;
import com.tum.androiddemo.WIFI.WIFIDemo5.Service.AutoConnService;
import com.tum.androiddemo.WIFI.WIFIDemo5.Thread.AutoConnectThread;
import com.tum.androiddemo.WIFI.WIFIDemo5.adapter.MyListViewAdapter;
import com.tum.androiddemo.WIFI.WIFIDemo5.dialog.OnNetworkChangeListener;
import com.tum.androiddemo.WIFI.WIFIDemo5.dialog.WifiConnDialog;
import com.tum.androiddemo.WIFI.WIFIDemo5.dialog.WifiStatusDialog;
import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;
import com.tum.androiddemo.WIFI.WIFIDemo5.view.MyListView;

import java.util.List;

public class WIFIConnectActivity extends AppCompatActivity {

    //UI
    private ToggleButton tgb_wifi_switch;//滑动button
    private MyListView freelook_listview;//刷新列表
    private MyListViewAdapter mAdapter;//设配器

    //WIFI
    private WifiAdmin mWifiAdmin;
    private List<ScanResult> mWifiList;

    private int mTime = 1000;



    private NetWorkStateChangeReceviver receviver;

    private boolean isFlushing = false;//是否正在刷新

    private OnNetworkChangeListener mOnNetworkChangeListener = new OnNetworkChangeListener() {

        @Override
        public void onNetWorkDisConnect() {
            getWifiListInfo();
            mAdapter.setDatas(mWifiList);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onNetWorkConnect() {
            getWifiListInfo();
            mAdapter.setDatas(mWifiList);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(WIFIConnectActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(WIFIConnectActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    mTime = 3000;
                    flushWifi();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wificonnect_demo5);

        //WIFI
        mWifiAdmin = WifiAdmin.getInstance(getApplicationContext());
        getWifiListInfo();

        //自动连接
//        autoConnect();

        startCheckAuto(true);

        cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(mWifiAdmin.getConfiguration() != null) {
            for (WifiConfiguration config : mWifiAdmin.getConfiguration()) {
                if(config.BSSID == null){
                    Log.i("TGA=======>", "TGA config SSID:" + config.SSID+",BSSID:空"+",netWorkId:"+config.networkId+",密码:"+config.preSharedKey);
                }else {
                    Log.i("TGA=======>", "TGA config SSID:" + config.SSID+",BSSID:"+config.BSSID);
                }
            }
        }

        //初始化UI
        initView();
        setListener();

        registerBroadcast();//注册广播
    }

    private void autoConnect(){
        if(mWifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED && NetworkInfo.State.CONNECTED != getNetWorkState()) {

            Log.i("TGA","TGA 自动连接 autoconnect!Wifi的状态为:"+getNetWorkState());
            mWifiAdmin.startScan();
            AutoConnectThread mAutoConnectThread = new AutoConnectThread(mWifiList, mWifiAdmin.getConfiguration(), true, mWifiAdmin, handler);
            new Thread(mAutoConnectThread).start();
        }
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        receviver = new NetWorkStateChangeReceviver();
        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction("com.tum.wifi.statechange.autoconnect");
        registerReceiver(receviver,filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receviver);
        super.onDestroy();
    }

    //=============================== UI =====================================================

    /**
     * 初始化UI
     */
    private void initView() {
        tgb_wifi_switch = (ToggleButton) findViewById(R.id.tgb_wifi_switch);
        freelook_listview = (MyListView) findViewById(R.id.freelook_listview);
        mAdapter = new MyListViewAdapter(this, mWifiList);
        freelook_listview.setAdapter(mAdapter);

        //设置Wifi状态
        if (mWifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED) {
            tgb_wifi_switch.setChecked(true);
        } else {
            tgb_wifi_switch.setChecked(false);
        }
    }

    /**
     * 监听
     */
    private void setListener() {
        tgb_wifi_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWifiAdmin.openWifi();
                } else {
                    mWifiAdmin.closeWifi();
                }
            }
        });

        // 设置刷新监听
        freelook_listview.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                flushWifi();
            }
        });
        //点击连接
        freelook_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // position与id的区别:http://blog.csdn.net/u012398902/article/details/51024243
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TGA", "TGA position:" + position + ",id:" + id);
                // 1、获取当前的点击的WIFI
                ScanResult clickWifiItem = mWifiList.get((int) id);

                Log.i("TGA=========>", "TGA Wifi name：" + clickWifiItem.SSID + ",IP:" + clickWifiItem.BSSID + ",安全:" + clickWifiItem.capabilities + ",信号强度:" + clickWifiItem.frequency);

                // 2、判断当前wifi是否已经连接 否 连接 是 显示连接信息
                if (checkWifiIsConnec(clickWifiItem)) {//已连接
                    isConnectSelf(clickWifiItem);
                } else if(isHasConfig(clickWifiItem)){
                    Log.i("TGA","TGA 已经配置!SSID:"+clickWifiItem.SSID);
                    showDetails(clickWifiItem);
                }else {//未连接
                    isConnect(clickWifiItem);
                }
            }
        });
    }


    /**
     * 刷新Wifi
     */
    private void flushWifi(){
        if(isFlushing){
            return;
        }
        isFlushing = true;
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(mTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getWifiListInfo();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                mAdapter.setDatas(mWifiList);
                mAdapter.notifyDataSetChanged();
                freelook_listview.onRefreshComplete();
                Log.i("TGA----->","TGA 刷新完成");
                isFlushing = false;
            }
        }.execute();
    }

    /**
     * 判断是否已经配置过
     * @param scanResult
     * @return
     */
    private boolean isHasConfig(ScanResult scanResult){
        for(WifiConfiguration config : mWifiAdmin.getConfiguration()) {
            Log.i("TGA", "TGA config.SSID：" + config.SSID + ",scanResult.SSID：" + scanResult.SSID);
            if (config.SSID.equals("\"" + scanResult.SSID + "\"")) {
                return true;
            }
        }
        return false;
    }


    /**
     * 连接网络
     * @param scanResult
     */
    private void isConnect(ScanResult scanResult) {
        if (mWifiAdmin.isConnect(scanResult)) {
            // 已连接，显示连接状态对话框
            WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                    this, R.style.PopDialog,
                    scanResult, mOnNetworkChangeListener);
            mStatusDialog.show();
        } else {
            // 未连接显示连接输入对话框
            WifiConnDialog mDialog = new WifiConnDialog(
                    this, R.style.PopDialog,
                    scanResult, mOnNetworkChangeListener);
            mDialog.show();
        }
    }

    private void isConnectSelf(ScanResult scanResult) {
        if (mWifiAdmin.isConnect(scanResult)) {

            // 已连接，显示连接状态对话框
            WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                    this, R.style.PopDialog,
                    scanResult, mOnNetworkChangeListener);
            mStatusDialog.show();

        } else {
            boolean iswifi = mWifiAdmin.connectSpecificAP(scanResult);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (iswifi) {
                Toast.makeText(this, "连接成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 展示信息
     * @param scanResult
     */
    private void showDetails(ScanResult scanResult){
        // 已连接，显示连接状态对话框
        WifiStatusDialog mStatusDialog = new WifiStatusDialog(
                this, R.style.PopDialog,
                scanResult, mOnNetworkChangeListener);
        mStatusDialog.show();
    }

    //判断当前wifi是否是已经连接
    ConnectivityManager cm;

    private boolean checkWifiIsConnec(ScanResult scanResult) {

        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (wifi == NetworkInfo.State.CONNECTED) {
            WifiInfo wifiInfo = mWifiAdmin.getWifiInfo();
            String g1 = wifiInfo.getSSID();
            Log.e("g1============>", g1);
            Log.e("g2============>", scanResult.SSID);

            String g2 = "\"" + scanResult.SSID + "\"";

            if (g2.endsWith(g1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取WIFI列表
     */
    private void getWifiListInfo() {
        mWifiAdmin.startScan();
        mWifiList = mWifiAdmin.getWifiList();
    }

    //================================== 状态监听 ==================================================
    public class NetWorkStateChangeReceviver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
                Log.i("TGA=======>","TGA RSSI changed!");
            }else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否
                System.out.println("网络状态改变");
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){//断开
                    System.out.println("wifi网络连接断开");
                    Log.i("TGA======>","TGA wifi网络连接断开");
                    flushWifi();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCheckAuto(true);
                        }
                    },10000);

                }else if(info.getState().equals(NetworkInfo.State.CONNECTED)){//连接成功

                    WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                    //获取当前wifi名称
                    System.out.println("连接到网络 " + wifiInfo.getSSID());
                    Log.i("TGA======>","TGA 连接到网络 " + wifiInfo.getSSID());
                    flushWifi();
                }else if(info.getState().equals(NetworkInfo.State.CONNECTING)){
                    Log.i("TGA======>","TGA 正在连接网络。。。。。 ");
                }else if(info.getState().equals(NetworkInfo.State.SUSPENDED)){
                    Log.i("TGA======>","TGA 网络冲突 ");
                }else if(info.getState().equals(NetworkInfo.State.DISCONNECTING)){
                    Log.i("TGA======>","TGA 网络正在断开连接。。。 ");
                }else if(info.getState().equals(NetworkInfo.State.UNKNOWN)){
                    Log.i("TGA======>","TGA 网络未知! ");
                }
            }else if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//wifi打开与否
                int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);

                if(wifistate == WifiManager.WIFI_STATE_DISABLED){
                    System.out.println("系统关闭wifi");
                    Log.i("TGA======>","TGA 系统关闭wifi");
                    startCheckAuto(false);
                    flushWifi();
                }
                else if(wifistate == WifiManager.WIFI_STATE_ENABLED){
                    System.out.println("系统开启wifi");
                    Log.i("TGA======>","TGA 系统开启wifi");
                    mTime = 1500;
                    flushWifi();
                }
            }else if(intent.getAction().equals("com.tum.wifi.statechange.autoconnect")){
                Log.i("TGA","TGA 自动连接启动!");
                autoConnect();
            }
        }
    }

    /**
     * 开启服务，检测自动更新
     * @param isStart
     */
    private void startCheckAuto(boolean isStart){
        Intent intent = new Intent(this, AutoConnService.class);
        intent.putExtra("scan",isStart);
        startService(intent);
    }

    private void stopService(boolean isStart){
        Intent intent = new Intent(this, AutoConnService.class);
        intent.putExtra("scan",isStart);
        stopService(intent);
    }


    /**
     * 网络状态发生改变
     */
    private void netWorkStateChanage(){
        NetworkInfo.State wifiState = null;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        Log.i("TGA-------->","============ NetWorkStateChangeReceviver ================");
        if(wifiState != null && NetworkInfo.State.CONNECTED == wifiState){
            Log.i("TGA-------->","TGA wifi 已经打开:"+wifiState);
            flushWifi();//刷新界面
        }
        if(wifiState != null && NetworkInfo.State.CONNECTING == wifiState){
            Log.i("TGA-------->","TGA wifi 正在打开:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.SUSPENDED == wifiState){
            Log.i("TGA-------->","TGA wifi 有冲突:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.DISCONNECTING == wifiState){
            Log.i("TGA-------->","TGA wifi 正在关闭:"+wifiState);
        }
        if(wifiState != null && NetworkInfo.State.DISCONNECTED == wifiState){
            Log.i("TGA-------->","TGA wifi 已经关闭:"+wifiState);
            flushWifi();
        }
        if(wifiState != null && NetworkInfo.State.UNKNOWN == wifiState){
            Log.i("TGA-------->","TGA wifi 未知:"+wifiState);
        }
    }

    /**
     * 获取当前wifi连接状态
     * @return
     */
    private NetworkInfo.State getNetWorkState(){
        NetworkInfo.State wifiState = null;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        return wifiState;
    }
}
