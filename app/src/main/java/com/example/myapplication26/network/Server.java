package com.example.myapplication26.network;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends AsyncTask<String, Object, Boolean> {

    public Socket mSocket;
    private OutputStream mOpStream;
    private static final int SERVERPORT = 1234;
    private static final String SERVER_IP = "192.168.43.35";
    public DataOutputStream dataOutputStream;

    public Server() {
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
            this.mSocket = new Socket(serverAddr, SERVERPORT);
            this.mOpStream = this.mSocket.getOutputStream();
            this.dataOutputStream = new DataOutputStream(mOpStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            // create a data output stream from the output stream so we can send data through it
            dataOutputStream.writeUTF((strings.length > 0) ? strings[0]+" "+strings[1]+" "+strings[2] : "");
            dataOutputStream.flush();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }


}
