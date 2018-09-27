package com.example.lzyang.fyptest.ServerModule;

import android.content.Context;
import android.util.Base64;

import com.example.lzyang.fyptest.Entity.EmergencyCard;
import com.example.lzyang.fyptest.Functions.Action;
import com.example.lzyang.fyptest.Functions.EmergencyEventClear;
import com.example.lzyang.fyptest.Functions.EmergencyRescuer;
import com.example.lzyang.fyptest.Functions.NotificationGenerator;
import com.example.lzyang.fyptest.Functions.ProcessingEmergencyCard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;
import static com.example.lzyang.fyptest.Functions.Action.byteToBitmap;

/**
 * Created by Lz-Yang on 26/11/2017.
 */

public class Payload_Executer {
    private Context context;

    public Payload_Executer(Context context) {
        this.context = context;
    }

    public void execute_Emergency_Event_Massage(JSONObject jsonObj){
        ArrayList<EmergencyCard> newCard_Added_arrayList = emergencyCards_arrayList.get_arrayList();
        String  msg_recordID = null,
                msg_userID = null,
                msg_title = null,
                msg_img = null,
                msg_latitude = null,
                msg_longitude = null,
                msg_description = null,
                msg_date = null,
                msg_time = null;
        byte[] img_byte;
        try {
            msg_recordID = Action.hexToAscii(jsonObj.getString("RecordID"));
            msg_userID = Action.hexToAscii(jsonObj.getString("UserID"));
            msg_title = Action.hexToAscii(jsonObj.getString("Title"));
            msg_img = jsonObj.getString("Image");
            msg_latitude = Action.hexToAscii(jsonObj.getString("Latitude"));
            msg_longitude = Action.hexToAscii(jsonObj.getString("Longitude"));
            msg_description = Action.hexToAscii(jsonObj.getString("Description"));
            msg_date = Action.hexToAscii(jsonObj.getString("Date"));
            msg_time = Action.hexToAscii(jsonObj.getString("Time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        img_byte = Base64.decode(msg_img, Base64.DEFAULT);

        //---add in emergencyCard
        EmergencyCard emergencyCard = new EmergencyCard(msg_recordID,msg_userID,byteToBitmap(img_byte),msg_title,msg_description,msg_latitude,msg_longitude,msg_date,msg_time);
        newCard_Added_arrayList.add(emergencyCard);
        emergencyCards_arrayList.set_arrayList(newCard_Added_arrayList);
//        set_EmergencyCards_Prefs(newCard_Added_arrayList);

        //Background ProcessingEmergencyCard
        ProcessingEmergencyCard processingEmergencyCard = new ProcessingEmergencyCard(context);
        processingEmergencyCard.execute();

        //Notify User
        NotificationGenerator.openEmergencyEvent(context,msg_title,msg_description);

        // Testing purpose
        System.out.println("Record ID -------------->>>>>>  "+msg_recordID);
    }

    public void execute_Emergency_Rescue_Action(JSONObject jsonObj){
        String record_ID =null;
        String rescue_ID =null;
        try {
            record_ID = Action.hexToAscii(jsonObj.getString("RecordID"));
            rescue_ID = Action.hexToAscii(jsonObj.getString("RescueID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("RecordID = "+record_ID+"\nRescuerID = "+rescue_ID);

        EmergencyRescuer emergencyRescuer = new EmergencyRescuer();
        emergencyRescuer.add_emergencyCards_noOfRescuer(record_ID);
    }

    public void execute_Emergency_Solved_Action(JSONObject jsonObj){
        String record_ID =null;
        try {
            record_ID = Action.hexToAscii(jsonObj.getString("RecordID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("RecordID = "+record_ID);

        EmergencyEventClear emergencyEventClear = new EmergencyEventClear();
        emergencyEventClear.clear_emergencyCards_recyclerAdapter(record_ID);

        //Background ProcessingEmergencyCard
        ProcessingEmergencyCard processingEmergencyCard = new ProcessingEmergencyCard(context);
        processingEmergencyCard.execute();
    }
}
