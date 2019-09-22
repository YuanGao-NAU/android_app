package com.example.myapplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class ConnectThread extends Thread{
    Socket socket = null;		//定义socket
    OutputStream outputStream = null;	//定义输出流（发送）
    InputStream inputStream=null;	//定义输入流（接收）
    MainActivity ma;
    public void run(){
        System.out.println(Thread.currentThread().getName()+": Hello");
        try {
            socket = new Socket("211.159.189.50", 2029);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //获取输出流
        try {
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try{
            while (true)
            {
                final byte[] buffer = new byte[1024];//创建接收缓冲区
                inputStream = socket.getInputStream();
                final int len = inputStream.read(buffer);//数据读出来，并且返回数据的长度
                runOnUiThread(new Runnable()//不允许其他线程直接操作组件，用提供的此方法可以
                {
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        recvET.append(new String(buffer,0,len)+"\r\n");
                    }
                });
            }
        }
        catch (IOException e) {

        }

    }
}