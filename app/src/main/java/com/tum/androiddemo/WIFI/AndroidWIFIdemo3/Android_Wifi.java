package com.tum.androiddemo.WIFI.AndroidWIFIdemo3;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tum.androiddemo.R;

public class Android_Wifi extends Activity {
	private Button startButton=null;
	private Button stopButton=null;
	private Button checkButton=null;
	WifiManager wifiManager=null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_demo3);
        startButton=(Button)findViewById(R.id.startButton);
        stopButton=(Button)findViewById(R.id.stopButton);
        checkButton=(Button)findViewById(R.id.checkButton);
        startButton.setOnClickListener(new startButtonListener());
        stopButton.setOnClickListener(new stopButtonListener());
        checkButton.setOnClickListener(new checkButtonListener());
    }
    class startButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			wifiManager=(WifiManager)Android_Wifi.this.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);
			System.out.println("wifi state --->"+wifiManager.getWifiState());
			Toast.makeText(Android_Wifi.this, "当前网卡状态为："+wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();
		}
    	
    }
    class stopButtonListener implements OnClickListener
    {

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			wifiManager=(WifiManager)Android_Wifi.this.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(false);
			System.out.println("wifi state --->"+wifiManager.getWifiState());
			Toast.makeText(Android_Wifi.this, "当前网卡状态为："+wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();
		}
    	
    }
    class checkButtonListener implements OnClickListener
    {

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			wifiManager=(WifiManager)Android_Wifi.this.getSystemService(Context.WIFI_SERVICE);			
			System.out.println("wifi state --->"+wifiManager.getWifiState());
			Toast.makeText(Android_Wifi.this, "当前网卡状态为："+wifiManager.getWifiState(), Toast.LENGTH_SHORT).show();
		}    	
    }
}