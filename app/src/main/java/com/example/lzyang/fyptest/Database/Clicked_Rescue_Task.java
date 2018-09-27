package com.example.lzyang.fyptest.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.lzyang.fyptest.R;
import com.example.lzyang.fyptest.ServerModule.PublishTaskMQTT;

import static com.example.lzyang.fyptest.Entity.CommandClass.setCommandPayload;

/**
 * Created by Lz-Yang on 28/11/2017.
 */

public class Clicked_Rescue_Task extends AsyncTask<String,Void,Void> {
    private String command;
    private Context context;

    public Clicked_Rescue_Task(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        do {
            command = setCommandPayload(R.id.btn_rescue);
        }while (command == null);
    }

    @Override
    protected Void doInBackground(String... strings) {
        PublishTaskMQTT publishTaskMQTT = new PublishTaskMQTT(context);
        publishTaskMQTT.onClickPublish_Rescue(command,strings[0],strings[1]);
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(Void voids) {
        Toast.makeText(context,"Rescue Action Begin",Toast.LENGTH_SHORT).show();
    }
}