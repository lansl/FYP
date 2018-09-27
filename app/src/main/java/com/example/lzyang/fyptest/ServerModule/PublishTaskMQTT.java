package com.example.lzyang.fyptest.ServerModule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.AsyncResponse;
import com.example.lzyang.fyptest.Entity.EmergencyCard;
import com.example.lzyang.fyptest.Functions.Action;
import com.example.lzyang.fyptest.MainActivity;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.lzyang.fyptest.LoginActivity.PREFS_NAME;
import static com.example.lzyang.fyptest.RequestActivity.mProgress;

/**
 * Created by LzYang on 16/10/2017.
 */

public class PublishTaskMQTT extends MQTTConnectionTask implements AsyncResponse {

    private final Context context;
    private byte[] Payloadbytes;
    private EmergencyCard emergencyCard;
    private String encodeText;
    private String reserve = "303030303030303030303030303030303030303030303030";


    public PublishTaskMQTT(Context context) {
        this.context = context;
    }

    public void onClickPublish(byte[] encodedPayload) {

        try {
            client.publish(topicStr, encodedPayload, 2, false);
            System.out.println("Published");
            mProgress.dismiss();
            Toast.makeText(context,"Published Successfully!",Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, MainActivity.class);
            context.startActivity(i);
            ((Activity) context).finish();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void onClickPublish_Rescue(String command, String record_id, String rescue_id){
        encodeText = "{\"Command\": "+ "\"" + Action.asciiToHex(command) + "\" ," +
                "\"RecordID\": "+ "\"" + Action.asciiToHex(record_id) + "\" ," +
                "\"RescueID\": "+ "\"" + Action.asciiToHex(rescue_id) + "\"}";
        try {
            client.publish(topicStr, encodeText.getBytes(), 2, false);
            System.out.println("Published Rescue");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void onClickPublish_EmergencySolved(String command, String record_id){
        encodeText = "{\"Command\": "+ "\"" + Action.asciiToHex(command) + "\" ," +
                "\"RecordID\": "+ "\"" + Action.asciiToHex(record_id) + "\"}";
        try {
            client.publish(topicStr, encodeText.getBytes(), 2, false);
            System.out.println("Published EmergencySolved(");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setUpPayload(EmergencyCard emergencyCard){

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        this.emergencyCard = emergencyCard;

        String type= "publish";
        String command = emergencyCard.getCommand();
        String img_bytes = Base64.encodeToString(getBytesFromBitmap(emergencyCard.getImg()), Base64.DEFAULT);
        // recordid generator
        String record_ID = emergencyCard.getRecord_ID();
//        RecordIDGenerator recordIDGenerator = new RecordIDGenerator();
//        record_ID = recordIDGenerator.getRecordID_Generated();

        String user_ID = settings.getString("userid","");
        String title = emergencyCard.getTitle();
        String latitude = emergencyCard.getLatitude();
        String longitude = emergencyCard.getLongitude();
        String description = emergencyCard.getDescription();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        String time = timeformat.format(c.getTime());
        String date= dateformat.format(c.getTime());
        String status ="not solved";

        encodeText ="{\"Command\": "+ "\"" + Action.asciiToHex(command) + "\" ," +
                "\"Reserve\": "+ "\"" + Action.asciiToHex(reserve) + "\" ," +
                "\"RecordID\": "+ "\"" + Action.asciiToHex(record_ID) + "\" ," +
                "\"UserID\": "+ "\"" + Action.asciiToHex(user_ID) + "\" ," +
                "\"Title\": "+ "\"" + Action.asciiToHex(title) + "\" ," +
                "\"Image\": "+ "\"" + img_bytes + "\" ," +
                "\"Description\": "+ "\"" + Action.asciiToHex(description) + "\" ," +
                "\"Longitude\": "+ "\"" + Action.asciiToHex(longitude) + "\" ," +
                "\"Latitude\": "+ "\"" + Action.asciiToHex(latitude) + "\" ," +
                "\"Date\": "+ "\"" + Action.asciiToHex(date) + "\" ," +
                "\"Time\": "+ "\"" + Action.asciiToHex(time) + "\"}";
        Payloadbytes = encodeText.getBytes();

//        BackgroundWorker backgroundWorker = new BackgroundWorker(context);
//        backgroundWorker.delegate= this;
//        backgroundWorker.execute(type,record_ID,user_ID,title,description,img_bytes,time,date,latitude,longitude,status);

        //Testing while Database Sleep
        onClickPublish(Payloadbytes);

    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    @Override
    public void processFinish(String result){
        System.out.println(result);
        onClickPublish(Payloadbytes);
    }
}
