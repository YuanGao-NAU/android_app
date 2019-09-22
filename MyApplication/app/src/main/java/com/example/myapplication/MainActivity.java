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
import android.net.Uri;
import android.os.Bundle;
import android.os.health.PackageHealthStats;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private Switch switch1;
    private IntentFilter intentFilter;
    private BroadcastReceiver messageReceiver;

    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked == true){

                   Toast.makeText(MainActivity.this, "applying permission", Toast.LENGTH_LONG).show();

                   if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
                       Toast.makeText(MainActivity.this, "Permission must be granted to get sms", Toast.LENGTH_LONG).show();
                       ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
                       if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
                           Toast.makeText(MainActivity.this, "Permission must be granted to get sms", Toast.LENGTH_LONG).show();
                           ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                       }
                   }
                   else {
                       Toast.makeText(MainActivity.this, "permission granted", Toast.LENGTH_LONG).show();
                       //intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                       //myReceiver = new MyReceiver();
                       //registerReceiver(myReceiver, intentFilter);
                       messageReceiver = new BroadcastReceiver(){
                           @Override
                           public void onReceive(Context context, Intent intent) {
                               String value = getIntent().getStringExtra("name");
                           }
                       };
                       registerReceiver(messageReceiver, new IntentFilter(CLOSE_ACTION));
                   }
               }
            }
        });
    }

}
