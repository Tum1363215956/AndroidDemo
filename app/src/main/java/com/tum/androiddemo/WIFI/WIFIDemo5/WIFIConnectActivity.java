package com.tum.androiddemo.WIFI.WIFIDemo5;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tum.androiddemo.R;
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wificonnect_demo5);

        //WIFI
        mWifiAdmin = new WifiAdmin(this);
        getWifiListInfo();

        cm = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        //初始化UI
        initView();

        setListener();
    }

    //=============================== UI =====================================================

    /**
     * 初始化UI
     */
    private void initView() {
        tgb_wifi_switch = (ToggleButton)findViewById(R.id.tgb_wifi_switch);
        freelook_listview = (MyListView)findViewById(R.id.freelook_listview);
        mAdapter = new MyListViewAdapter(this,mWifiList);
        freelook_listview.setAdapter(mAdapter);

        //设置Wifi状态
        if(mWifiAdmin.checkState() == WifiManager.WIFI_STATE_ENABLED){
            tgb_wifi_switch.setChecked(true);
        }else{
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
                if(isChecked){
                    mWifiAdmin.openWifi();
                }else{
                    mWifiAdmin.closeWifi();
                }
            }
        });

        // 设置刷新监听
        freelook_listview.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
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
                    }
                }.execute();
            }
        });
        //点击连接
        freelook_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // position与id的区别:http://blog.csdn.net/u012398902/article/details/51024243
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TGA","TGA position:"+position+",id:"+id);
                // 1、获取当前的点击的WIFI
                ScanResult clickWifiItem = mWifiList.get((int)id);
                // 2、判断当前wifi是否已经连接 否 连接 是 显示连接信息
                if(checkWifiIsConnec(clickWifiItem)){//已连接
                    isConnectSelf(clickWifiItem);
                }else{//未连接
                    isConnect(clickWifiItem);
                }
            }
        });
    }

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
            }else {
                Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //判断当前wifi是否是已经连接
    ConnectivityManager cm;
    private boolean checkWifiIsConnec(ScanResult scanResult) {

        NetworkInfo.State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == NetworkInfo.State.CONNECTED) {
            WifiInfo wifiInfo = mWifiAdmin.getWifiInfo();
            String g1 = wifiInfo.getSSID();
            Log.e("g1============>", g1);
            Log.e("g2============>", scanResult.SSID);

            String g2 = "\""+scanResult.SSID+"\"";

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

}
