package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity{

    private Switch switch1;
    private IntentFilter intentFilter;
    private BroadcastReceiver messageReceiver;

    private MyReceiver myReceiver;

    public String value = null;
    public String message;

    private MySocketThread myThread;

    private class MySocketThread extends Thread{

        private Socket socket;

        @Override
        public void run(){
            try {
                this.socket = new Socket("127.0.0.1", 10000);       //change to you own server IP address
                this.socket.setSoTimeout(10000);
                OutputStream os = this.socket.getOutputStream();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
                bw.write(value);
                bw.flush();
                this.socket.close();
            }
            catch (UnknownHostException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {

                    //Toast.makeText(MainActivity.this, "applying permission", Toast.LENGTH_LONG).show();

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                        //Toast.makeText(MainActivity.this, "Permission must be granted to get sms", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                            //Toast.makeText(MainActivity.this, "Permission must be granted to get sms", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                        }
                    } else {
                        //Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_LONG).show();
                        //intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                        //myReceiver = new MyReceiver();
                        //registerReceiver(myReceiver, intentFilter);
                        messageReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Bundle bundle = intent.getExtras();
                                value = bundle.getString("name");
                                //Toast.makeText(context, value, Toast.LENGTH_LONG).show();
                                //Intent intentTmp = new Intent(MainActivity.this, SocketActivity.class);
                                //intentTmp.putExtra("extra_data", value);
                                //startActivity(intentTmp);
                                myThread = new MySocketThread();
                                myThread.start();
                            }
                        };
                        registerReceiver(messageReceiver, new IntentFilter("CLOSE_ACTION"));

                    }
                    Toast.makeText(MainActivity.this, "I'm here", Toast.LENGTH_LONG).show();
                    if (value != null) {

                    }
                }
                else{
                    //Do something
                }
            }
        });
    }
}
