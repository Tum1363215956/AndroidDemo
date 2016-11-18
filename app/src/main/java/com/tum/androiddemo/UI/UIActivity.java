package com.tum.androiddemo.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tum.androiddemo.R;
import com.tum.androiddemo.UI.Dialog.Demo1.UiDialogDemo1Activity;

public class UIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);
    }


    public void ui_btn_onClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.btn_ui_dialog://Dialog
                intent.setClass(this, UiDialogDemo1Activity.class);
                break;
        }
        startActivity(intent);
    }
}
