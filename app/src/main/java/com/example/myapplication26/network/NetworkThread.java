package com.example.myapplication26.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkThread implements Runnable {

    public Socket mSocket;
//    private OutputStream mOpStream;
    private static final int SERVERPORT = 1234;
    private static final String SERVER_IP = "192.168.43.35";
//    public DataOutputStream dataOutputStream;
    public String message;


    public NetworkThread(String msg) {
        this.message = msg;
    }


    @Override
    public void run() {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            this.mSocket = new Socket(serverAddr, SERVERPORT);
            OutputStream mOpStream;
            mOpStream = this.mSocket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(mOpStream);



            while (true) {
                // create a data output stream from the output stream so we can send data through it
                if (message != null) {
                    dataOutputStream.writeBytes(message);
                    message = null;
                }
            }
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}




