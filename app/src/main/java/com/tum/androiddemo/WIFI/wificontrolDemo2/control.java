package com.tum.androiddemo.WIFI.wificontrolDemo2;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.tum.androiddemo.R;

public class control extends Activity
{
  private TextView mTextView01;
  private CheckBox mCheckBox01;
  
  /* ????WiFiManager???? */
  private WifiManager mWiFiManager01;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.wifi_control_demo2);
    
    mTextView01 = (TextView) findViewById(R.id.myTextView1);
    mCheckBox01 = (CheckBox) findViewById(R.id.myCheckBox1);

    mWiFiManager01 = (WifiManager)
                     this.getSystemService(Context.WIFI_SERVICE);

    if(mWiFiManager01.isWifiEnabled())
    {
      if(mWiFiManager01.getWifiState()==
         WifiManager.WIFI_STATE_ENABLED)
      {
        mCheckBox01.setChecked(true);
        mCheckBox01.setText(R.string.str_uncheck);
      }
      else
      {
        mCheckBox01.setChecked(false);
        mCheckBox01.setText(R.string.str_checked);
      }
    }
    else
    {
      mCheckBox01.setChecked(false);
      mCheckBox01.setText(R.string.str_checked);
    }

    mCheckBox01.setOnClickListener(
    new CheckBox.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        if(mCheckBox01.isChecked()==false)
        {
          try
          {
            if(mWiFiManager01.isWifiEnabled() )
            {
              if(mWiFiManager01.setWifiEnabled(false))
              {
                mTextView01.setText(R.string.str_stop_wifi_done);
              }
              else
              {
                mTextView01.setText(R.string.str_stop_wifi_failed);
              }
            }
            else
            {
              switch(mWiFiManager01.getWifiState())
              {
                case WifiManager.WIFI_STATE_ENABLING:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_stop_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_enabling)
                  );
                  break;
                case WifiManager.WIFI_STATE_DISABLING:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_stop_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_disabling)
                  );
                  break;
                case WifiManager.WIFI_STATE_DISABLED:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_stop_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_disabled)
                  );
                  break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_stop_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_unknow)
                  );
                  break;
              }
              mCheckBox01.setText(R.string.str_checked);
            }
          }
          catch (Exception e)
          {
            Log.i("HIPPO", e.toString());
            e.printStackTrace();
          }
        }
        else if(mCheckBox01.isChecked()==true)
        {
          try
          {
            if(!mWiFiManager01.isWifiEnabled() && 
               mWiFiManager01.getWifiState()!=
               WifiManager.WIFI_STATE_ENABLING )
            {
              if(mWiFiManager01.setWifiEnabled(true))
              {
                switch(mWiFiManager01.getWifiState())
                {
                  case WifiManager.WIFI_STATE_ENABLING:
                    mTextView01.setText
                    (
                      getResources().getText
                      (R.string.str_wifi_enabling)
                    );
                    break;
                  case WifiManager.WIFI_STATE_ENABLED:
                    mTextView01.setText
                    (
                      getResources().getText
                      (R.string.str_start_wifi_done)
                    );
                    break;
                  default:
                    mTextView01.setText
                    (
                      getResources().getText
                      (R.string.str_start_wifi_failed)+":"+
                      getResources().getText
                      (R.string.str_wifi_unknow)
                    );
                    break;
                }
              }
              else
              {
                mTextView01.setText(R.string.str_start_wifi_failed);
              }
            }
            else
            {
              switch(mWiFiManager01.getWifiState())
              {
                case WifiManager.WIFI_STATE_ENABLING:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_start_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_enabling)
                  );
                  break;
                case WifiManager.WIFI_STATE_DISABLING:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_start_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_disabling)
                  );
                  break;
                case WifiManager.WIFI_STATE_DISABLED:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_start_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_disabled)
                  );
                  break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                default:
                  mTextView01.setText
                  (
                    getResources().getText
                    (R.string.str_start_wifi_failed)+":"+
                    getResources().getText
                    (R.string.str_wifi_unknow)
                  );
                  break;
              }
            }
            mCheckBox01.setText(R.string.str_uncheck);
          }
          catch (Exception e)
          {
            Log.i("HIPPO", e.toString());
            e.printStackTrace();
          }
        }
      }
    });
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(control.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(control.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    try
    {
      switch(mWiFiManager01.getWifiState())
      {
        case WifiManager.WIFI_STATE_ENABLED:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_enabling)
          );
          break;
        case WifiManager.WIFI_STATE_ENABLING:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_enabling)
          );
          break;
        case WifiManager.WIFI_STATE_DISABLING:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_disabling)
          );
          break;
        case WifiManager.WIFI_STATE_DISABLED:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_disabled)
          );
          break;
        case WifiManager.WIFI_STATE_UNKNOWN:
        default:
          mTextView01.setText
          (
            getResources().getText(R.string.str_wifi_unknow)
          );
          break;
      }
    }
    catch(Exception e)
    {
      mTextView01.setText(e.toString());
      e.getStackTrace();
    }
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
  }
}
