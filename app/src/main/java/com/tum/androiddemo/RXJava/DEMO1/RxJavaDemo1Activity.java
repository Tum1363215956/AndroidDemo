package com.tum.androiddemo.RXJava.DEMO1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tum.androiddemo.R;

import rx.Observable;
import rx.functions.Action1;

public class RxJavaDemo1Activity extends AppCompatActivity {

    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_demo1);

        tv_show = (TextView)findViewById(R.id.tv_show);
    }

    public void rxjava_btn_demo_onclick(View view){
        switch (view.getId()){
            case R.id.btn_rxjava_demo1:
                RxDmeo1();
                break;
        }
    }

    private void RxDmeo1(){
        final StringBuilder sb = new StringBuilder();
        String [] names = new String[]{"abc","asdgvs","gashsdah","asdgherh","gasghhrtdjerh","sdhdflgjdflkgj"};
        Observable.from(names).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TGA------------>","TGA names:"+s);
                sb.append(s+"\n");
                tv_show.setText(sb.toString());
            }
        });
    }
}
