package com.example.lzyang.fyptest.ServerModule;

import android.content.Context;
import android.os.AsyncTask;

import static com.example.lzyang.fyptest.ServerModule.MJobScheduler.MQTTConnectionTask;

/**
 * Created by Lz-Yang on 13/11/2017.
 */

public class MJobExecuter extends AsyncTask<Context, Void, String> {
    @Override
    protected String doInBackground(Context... contexts) {
        MQTTConnectionTask.mCallBack(contexts[0]);
        return "Running CallBack...";
    }

}
