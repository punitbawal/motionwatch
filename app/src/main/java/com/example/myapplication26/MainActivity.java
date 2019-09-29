package com.example.myapplication26;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication26.network.NetworkThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView mTextView, mKissValView;
    private SensorManager mSensorManager;
    public static int mLastSensor;
    public static int i =0;
    public float mCurrentPositionX = 0;
    public float mPrevKiss = 0;
    private static final String TAG = "MainActivity";
    private SharedPreferences mSharedPreferences;
    private Utils mUtils;
    NetworkThread networkThread;
    Thread thread;
    public boolean closerFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getApplicationContext().getSharedPreferences("actions", 0);
        mUtils = new Utils();

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(mUtils.SONG_KEY, "");
        editor.putString(mUtils.VOLUME_KEY, "");
        editor.putString(mUtils.PLAY_KEY, "pause");

        mTextView = findViewById(R.id.sensorVal);
        mKissValView = findViewById(R.id.kissVal);
        // Enables Always-on
        setAmbientEnabled();
        networkThread = new NetworkThread("start");
        thread = new Thread(networkThread);
        thread.start();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);



    }


    @Override /* KeyEvent.Callback */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                // Do something that advances a user View to the next item in an ordered list.
                Log.e(TAG, "onKeyDown: right");
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS:
                Log.e(TAG, "onKeyDown: left");
                return true;
        }
        // If you did not handle it, let it be handled by the next possible element as deemed by the Activity.
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // code for play and pause
            if (event.values[0] <= 50.0 && !closerFlag) {
                closerFlag = true;
                networkThread.message = "change";
            }
            if(event.values[0] > 100.0){
                closerFlag = false;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_GRAVITY){
            if (event.values[0] > 3 && event.values[0] < 6){
                networkThread.message = "next";
                Log.e(TAG, "onSensorChanged: next" );

            }
            if (event.values[0] < -3 && event.values[0] > -6){
                networkThread.message = "previous";
                Log.e(TAG, "onSensorChanged: previous" );
            }
        }


    }






    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            networkThread.mSocket.close();
            networkThread.dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
