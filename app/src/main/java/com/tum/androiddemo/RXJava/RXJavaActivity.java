package com.tum.androiddemo.RXJava;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tum.androiddemo.R;
import com.tum.androiddemo.RXJava.DEMO1.RxJavaDemo1Activity;

/**
 * RxJava Demo
 */
public class RXJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
    }

    public void rxjava_btn_onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btn_rxjava_demo1:
                intent.setClass(this, RxJavaDemo1Activity.class);
                break;
        }
        startActivity(intent);
    }

}
