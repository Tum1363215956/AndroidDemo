package com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tum.androiddemo.R;
import com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.server.SimpleHttpServer;
import com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.server.WebCofiguration;

public class SimpleHttpActivity extends AppCompatActivity {
    private SimpleHttpServer shs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_http_server);
        Log.i("TGA","TGA server start");
        WebCofiguration wc = new WebCofiguration();
        wc.setPort(10000);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.startAsync();
    }

    @Override
    protected void onDestroy() {
        shs.stopAsync();
        super.onDestroy();
    }
}
