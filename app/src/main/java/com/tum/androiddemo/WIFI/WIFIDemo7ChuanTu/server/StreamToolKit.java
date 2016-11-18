package com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class StreamToolKit {

    public static String readLine(InputStream nis) throws IOException{
        StringBuilder sb = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        while(c2!=-1&&!(c2=='\n')){
            c1 = c2;
            c2 = nis.read();
            sb.append((char)c2);
        }
        if(sb.length() == 0){
            return null;
        }
        return sb.toString();
    }
}
