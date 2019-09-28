package com.example.myapplication26;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;

public class MainActivity extends WearableActivity implements SensorEventListener {

    private TextView mTextView,mkissValView;
    private SensorManager sensorManager;
    public static int LAST_SENSOR;
    public static int i =0;
    public float current_position_x = 0;
    public float prev_kiss = 0;
    private Socket socket;

    private static final int SERVERPORT = 80;
    private static final String SERVER_IP = "129.107.80.18";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.sensorVal);
        mkissValView = (TextView) findViewById(R.id.kissVal);

        // Enables Always-on
        setAmbientEnabled();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            socket = new Socket(serverAddr, SERVERPORT);
            OutputStream opStream = socket.getOutputStream();
            // create a data output stream from the output stream so we can send data through it
            DataOutputStream dataOutputStream = new DataOutputStream(opStream);

            dataOutputStream.writeUTF("Hello from the other side!");
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done.

            System.out.println("Closing socket and terminating program.");
            socket.close();

        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            if(event.values[0]<=20.0 && event.values[0] != prev_kiss) {
                i++;
                System.out.println("KISS" + i);
                mkissValView.setText("KISS" + i);
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(current_position_x<event.values[0] && Math.abs(current_position_x - event.values[0])>=2){
                current_position_x = event.values[0];
                System.out.println("RIGHT");
                mTextView.setText("RIGHT");
            }
            if(current_position_x>event.values[0] && Math.abs(current_position_x - event.values[0])>=2){
                current_position_x = event.values[0];
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
}
