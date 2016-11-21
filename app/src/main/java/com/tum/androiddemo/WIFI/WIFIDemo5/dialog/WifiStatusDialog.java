package com.tum.androiddemo.WIFI.WIFIDemo5.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.tum.androiddemo.R;
import com.tum.androiddemo.WIFI.WIFIDemo5.util.WifiAdmin;


/**
 * WIFI网络状态
 */
public class WifiStatusDialog extends Dialog {

	// Wifi管理类
	private WifiAdmin mWifiAdmin;

	private Context context;

	private ScanResult scanResult;
	private String wifiName;
	private int level;
	private String securigyLevel;

	private TextView txtWifiName;
	private TextView txtConnStatus;
	private TextView txtSinglStrength;
	private TextView txtSecurityLevel;
	private TextView txtIpAddress;

	private TextView txtBtnDisConn;
	private TextView txtBtnCancel;

	public WifiStatusDialog(Context context, int theme) {
		super(context, theme);
		this.mWifiAdmin = WifiAdmin.getInstance(context);
	}

	private WifiStatusDialog(Context context, int theme, String wifiName,
							 int singlStren, String securityLevl) {
		super(context, theme);
		this.context = context;
		this.wifiName = wifiName;
		this.level = singlStren;
		this.securigyLevel = securityLevl;
		this.mWifiAdmin = WifiAdmin.getInstance(context);
	}

	public WifiStatusDialog(Context context, int theme, ScanResult scanResult,
							OnNetworkChangeListener onNetworkChangeListener) {
		this(context, theme, scanResult.SSID, scanResult.level,
				scanResult.capabilities);
		this.scanResult = scanResult;
		this.mWifiAdmin = WifiAdmin.getInstance(context);
		this.onNetworkChangeListener = onNetworkChangeListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_wifi_status);
		setCanceledOnTouchOutside(false);

		initView();
		setListener();
	}

	private void setListener() {

		txtBtnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("txtBtnCancel");
				int netWorkId = getNetId(scanResult);
				if(netWorkId != -1){
					mWifiAdmin.disConnectionWifi(netWorkId);
				}
				WifiStatusDialog.this.dismiss();
			}
		});

		txtBtnDisConn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 断开连接
				int netId = mWifiAdmin.getConnNetId();
				mWifiAdmin.disConnectionWifi(netId);
				WifiStatusDialog.this.dismiss();
				onNetworkChangeListener.onNetWorkDisConnect();
			}
		});
	}

	private void initView() {
		txtWifiName = (TextView) findViewById(R.id.txt_wifi_name);
		txtConnStatus = (TextView) findViewById(R.id.txt_conn_status);
		txtSinglStrength = (TextView) findViewById(R.id.txt_signal_strength);
		txtSecurityLevel = (TextView) findViewById(R.id.txt_security_level);
		txtIpAddress = (TextView) findViewById(R.id.txt_ip_address);

		txtBtnCancel = (TextView) findViewById(R.id.txt_btn_cancel);
		txtBtnDisConn = (TextView) findViewById(R.id.txt_btn_disconnect);

		txtWifiName.setText(wifiName);
		txtConnStatus.setText("已连接");
		txtSinglStrength.setText(WifiAdmin.singlLevToStr(level));
		txtSecurityLevel.setText(securigyLevel);
		txtIpAddress
				.setText(mWifiAdmin.ipIntToString(mWifiAdmin.getIpAddress()));

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Point size = new Point();
		wm.getDefaultDisplay().getSize(size);

		super.show();
		getWindow().setLayout((int) (size.x * 9 / 10),
				LayoutParams.WRAP_CONTENT);
	}

	private void showShortToast(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	private OnNetworkChangeListener onNetworkChangeListener;


	/**
	 * 获取netId
	 * @param scanResult
	 * @return
     */
	private int getNetId(ScanResult scanResult){
		for(WifiConfiguration config : mWifiAdmin.getConfiguration()) {
			Log.i("TGA", "TGA config.SSID：" + config.SSID + ",scanResult.SSID：" + scanResult.SSID);
			if (config.SSID.equals("\"" + scanResult.SSID + "\"")) {
				return config.networkId;
			}
		}
		return -1;
	}
}
