package com.tum.androiddemo.WIFI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tum.androiddemo.R;
import com.tum.androiddemo.WIFI.AndroidWIFIdemo3.Android_Wifi;
import com.tum.androiddemo.WIFI.WIFIDEMO1.WIFIDEMOONEActivity;
import com.tum.androiddemo.WIFI.WIFIDEMO4.WifiGuan;
import com.tum.androiddemo.WIFI.WIFIDemo5.WIFIConnectActivity;
import com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.SimpleHttpActivity;
import com.tum.androiddemo.WIFI.wificontrolDemo2.control;

public class WIFIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
    }

    public void wifi_btn_onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btn_wifi_wifidemo1:
                intent.setClass(this, WIFIDEMOONEActivity.class);
                break;
            case R.id.btn_wifi_wifidemo2:
                intent.setClass(this, control.class);
                break;
            case R.id.btn_wifi_wifidemo3:
                intent.setClass(this, Android_Wifi.class);
                break;
            case R.id.btn_wifi_wifidemo4:
                intent.setClass(this, WifiGuan.class);
                break;
            case R.id.btn_wifi_wifidemo5:
                intent.setClass(this, WIFIConnectActivity.class);
                break;
            case R.id.btn_wifi_wifidemo6:
                intent.setClass(this, SimpleHttpActivity.class);
                break;
        }
        startActivity(intent);
    }
}
