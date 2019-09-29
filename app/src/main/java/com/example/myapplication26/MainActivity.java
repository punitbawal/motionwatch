package com.example.myapplication26;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import com.example.myapplication26.network.NetworkThread;

import java.io.IOException;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView mTextView, mKissValView;
    private SensorManager mSensorManager;
    public static int mLastSensor;
    public static int i =0;
    public float mCurrentPositionX = 0;
    public float mPrevKiss = 0;
    private static final String TAG = "MainActivity";
    public boolean closerFlag = false;
    private SharedPreferences mSharedPreferences;
    private Utils mUtils;
    NetworkThread networkThread;
    Thread thread;


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


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
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
            if (event.values[0] <= 50.0 && closerFlag==false) {
                closerFlag = true;
                networkThread.message = "change";
            }
            if(event.values[0] > 100.0){
                closerFlag = false;
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(mCurrentPositionX <event.values[0] && Math.abs(mCurrentPositionX - event.values[0])>=2){
                mCurrentPositionX = event.values[0];
                System.out.println("RIGHT");
                mTextView.setText("RIGHT");
            }
            if(mCurrentPositionX >event.values[0] && Math.abs(mCurrentPositionX - event.values[0])>=2){
                mCurrentPositionX = event.values[0];
                System.out.println("LEFT");
                mTextView.setText("LEFT");
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
