package com.tum.androiddemo.WIFI.WIFIDemo7ChuanTu.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/11/17 0017.
 */
public class SimpleHttpServer {

    private boolean isEnable;

    private WebCofiguration webConfig;
    private ServerSocket serverSocket;

    private ExecutorService threadPool;

    public SimpleHttpServer(WebCofiguration webConfig){
        this.webConfig = webConfig;
        threadPool = Executors.newCachedThreadPool();
    }

    /**
     * 启动服务
     */
    public void startAsync(){
        isEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
              doproSync();
            }
        }).start();
    }

    /**
     * 配置服务ServerSocket
     */
    private void doproSync() {
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(webConfig.getPort());
            serverSocket = new ServerSocket();
            serverSocket.bind(socketAddress);
            while (isEnable){
                final Socket remotePeer = serverSocket.accept();
                //线程池
               threadPool.submit(new Runnable() {
                   @Override
                   public void run() {
                       Log.d("TGA"," a remote perr accepted..."+remotePeer.getRemoteSocketAddress().toString());
                       onAcceptedRemotePeer(remotePeer);
                   }
               });
            }
        } catch (IOException e) {
            Log.e("TGA",e.toString());
        }
    }

    /**
     * 远程连接
     * @param remotePeer
     */
    private void onAcceptedRemotePeer(Socket remotePeer) {
        try {
            InputStream nis = remotePeer.getInputStream();
            String headerLine = null;
            while((headerLine = StreamToolKit.readLine(nis))!=null){
                if(headerLine.equals("\r\n") || headerLine.equals("")||headerLine.equals("\r")){
                    break;
                }
                Log.d("TGA","TGA:"+headerLine);
            }
            remotePeer.getOutputStream().write("Congratulations,connected successful".getBytes());
        } catch (IOException e) {
            Log.e("TGA", e.toString());
        }
    }

    /**
     * 停止服务
     */
    public void stopAsync(){
        if(!isEnable){
            return;
        }
        isEnable = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket = null;
    }
}
