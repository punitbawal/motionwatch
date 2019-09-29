package com.example.myapplication26;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.myapplication26.network.NetworkThread;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView mTextView, mKissValView;
    private SensorManager mSensorManager;
    private static final String TAG = "MainActivity";
    float max = 25;
    float currentX = 0;
    float currentXN = -50;


    NetworkThread networkThread;
    Thread thread;
    public boolean closerFlag = false;
    private long mStartTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.sensorVal);
        mKissValView = findViewById(R.id.kissVal);
        // Enables Always-on
        setAmbientEnabled();
        networkThread = new NetworkThread("start");
        thread = new Thread(networkThread);
        thread.start();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mStartTime = getStartTime();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_GAME);
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

    private long getStartTime() {
        return System.currentTimeMillis() / 1000;
    }

    private long getDiffTime() {
        return getStartTime() - mStartTime;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // code for play and pause
            if (event.values[0] <= 50.0 && !closerFlag) {
                closerFlag = true;
                networkThread.message = "c";
            }
            if (event.values[0] > 100.0) {
                closerFlag = false;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (max < event.values[0]) {
                currentX = event.values[0];
                if (getDiffTime() > 1) {
                    networkThread.message = "l";
                    mStartTime = getStartTime();
                }
            }
            if (-(max - 5) > event.values[0]) {
                currentXN = event.values[0];
                if (getDiffTime() > 1) {
                    networkThread.message = "r";
                    mStartTime = getStartTime();
                }
            }
        }
    }


//           Log.e(TAG, "onSensorChanged: "+event.values[0] );

//            if (event.values[0] < -3 && event.values[0] > -5 && currentXN < -max ){
//                networkThread.message = "r";
//                Log.e(TAG, "onSensorChanged: next,right "+event.values[0] );
//            }
//            if (event.values[0] > 3 && event.values[0] < 5 && currentX > max){
//                networkThread.message = "l";
//                Log.e(TAG, "onSensorChanged: previous,left "+event.values[0] );
//            }
//            currentXN = -50;
//            currentX = 0;


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//
////            networkThread.dataOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}
