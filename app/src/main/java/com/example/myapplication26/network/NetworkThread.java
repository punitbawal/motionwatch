package com.example.myapplication26.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkThread implements Runnable {

    public Socket mSocket;
    private OutputStream mOpStream;
    private static final int SERVERPORT = 1234;
    private static final String SERVER_IP = "192.168.43.35";
    public DataOutputStream dataOutputStream;

    public  NetworkThread() {
        try {


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            // create a data output stream from the output stream so we can send data through it
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            this.mSocket = new Socket(serverAddr, SERVERPORT);
            this.mOpStream = this.mSocket.getOutputStream();
            this.dataOutputStream = new DataOutputStream(mOpStream);
            dataOutputStream.writeUTF("hi");
            dataOutputStream.flush();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public void sendData()
    {
        this.dataOutputStream.wr
    }
    }

