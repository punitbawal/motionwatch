package com.example.myapplication26.network;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;



public class SocketService extends JobService {
    private  Server mServer;

    public  SocketService(String[] list){

    }




    private static final String TAG = "SocketService";
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters params) {
        mServer = new Server(){
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                Log.e(TAG, "done");
                jobFinished(params, false);
            }
        };

        mServer.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
