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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class MainActivity extends AppCompatActivity{

    private Switch switch1;
    private IntentFilter intentFilter;
    private BroadcastReceiver messageReceiver;

    private MyReceiver myReceiver;

    public String value = null;

    private MailThread myThread;

    private class MailThread extends Thread {

        private String content;

        private String to;

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getTo() {
            return to;
        }

        @Override
        public void run(){
            //System.out.println(content);
            MailUtils.sendMail(to, content, "新短信");
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

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                        }
                    } else {
                        messageReceiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                Bundle bundle = intent.getExtras();
                                value = bundle.getString("name");
                                myThread = new MailThread();
                                myThread.setContent(value);

                                try {
                                    InputStream is = MainActivity.class.getResourceAsStream("/assets/email.properties");
                                    System.out.println(is);
                                    Properties props = new Properties();
                                    props.load(is);
                                    myThread.setTo(props.getProperty("mail.sendto"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                myThread.start();
                            }
                        };
                        registerReceiver(messageReceiver, new IntentFilter("CLOSE_ACTION"));

                    }
                    Toast.makeText(MainActivity.this, "Listening SMS", Toast.LENGTH_LONG).show();
                    if (value != null) {

                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Permission must be granted to get SMS", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
