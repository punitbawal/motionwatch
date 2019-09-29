package com.example.myapplication26;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.myapplication26.network.AppService;
import com.example.myapplication26.network.Server;

public class Utils {
    SharedPreferences.Editor mSPEditor;
    public String SONG_KEY = "song";
    public String VOLUME_KEY = "volume";
    public String PLAY_KEY = "play";

    public void updateAction (SharedPreferences sharedPreferences, String key, String value){
        mSPEditor = sharedPreferences.edit();
        mSPEditor.putString(key, value);
        mSPEditor.apply();
    }

    public void sendDataToServer(Server server, SharedPreferences sharedPreferences, Context context) {
        String[] list = new String[]{
                sharedPreferences.getString(SONG_KEY, ""),
                sharedPreferences.getString(VOLUME_KEY, ""),
                sharedPreferences.getString(PLAY_KEY, "pause")};
        server.execute(list);


//        Intent intent = new Intent(context, AppService.class);
//        intent.putExtra("actions", list);
//        context.startService(intent);
    }


    public void startList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }






}
