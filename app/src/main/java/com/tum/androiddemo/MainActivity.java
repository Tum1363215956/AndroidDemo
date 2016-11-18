package com.tum.androiddemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tum.androiddemo.RXJava.RXJavaActivity;
import com.tum.androiddemo.UI.UIActivity;
import com.tum.androiddemo.WIFI.WIFIActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void main_btn_onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btn_main_wifi://wifi
                intent.setClass(this, WIFIActivity.class);
                break;
            case R.id.btn_main_rxjava://RxJava
                intent.setClass(this, RXJavaActivity.class);
                break;
            case R.id.btn_main_UI://UiJava
                intent.setClass(this, UIActivity.class);
                break;
        }
        startActivity(intent);
    }
}
