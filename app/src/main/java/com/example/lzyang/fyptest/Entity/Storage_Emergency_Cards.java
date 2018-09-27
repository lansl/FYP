package com.example.lzyang.fyptest.Entity;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lzyang.fyptest.Functions.EmergencyCard_App;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Lz-Yang on 28/11/2017.
 */

public class Storage_Emergency_Cards {
    public static final String ESS_CARDS_PREFS_NAME = "EmergencyCardsPrefs";
    public static EmergencyCard_App emergencyCards_arrayList = new EmergencyCard_App();

    private static SharedPreferences EmergencyCards_Prefs;

    private static ArrayList<EmergencyCard> currentEmergencyCards;

    public static void init_EmergencyCards_Prefs(Context context){
        Gson gson = new Gson();
        String json;

        EmergencyCards_Prefs = context.getSharedPreferences(ESS_CARDS_PREFS_NAME, 0);
        json = EmergencyCards_Prefs.getString("ESS_Cards","");
        if(gson.fromJson(json, EmergencyCard_App.class) != null ){
            System.out.println("emergencyCards_arrayList NOT null");
            emergencyCards_arrayList = gson.fromJson(json.toString(), EmergencyCard_App.class);
        }else{
//            emergencyCards_arrayList
            System.out.println("emergencyCards_arrayList IS null");
        }

//        EmergencyCards_Prefs = context.getSharedPreferences(ESS_CARDS_PREFS_NAME, Context.MODE_PRIVATE);
//        try {
//            currentEmergencyCards = (ArrayList<EmergencyCard>) ObjectSerializer.deserialize(EmergencyCards_Prefs.getString("ESS_Cards", ObjectSerializer.serialize(new ArrayList<EmergencyCard>())));
//            emergencyCards_arrayList.set_arrayList(currentEmergencyCards);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

    public static void set_EmergencyCards_Prefs(ArrayList<EmergencyCard> arrayList){
        Gson gson = new Gson();
        String json;

        SharedPreferences.Editor new_EmergencyCards_Prefs = EmergencyCards_Prefs.edit();
        emergencyCards_arrayList.set_arrayList(arrayList);
        System.out.println("set_EmergencyCards_Prefs--------------------------------------------------------------------------------------------Running");
        if(emergencyCards_arrayList.get_arrayList().isEmpty()){
            System.out.println("emergencyCards_arrayList IS null ------------------------------------------------------isEmpty()");
        }else{
            System.out.println("set_EmergencyCards_Prefs--------------------------------------------------------------------------------------------Saving");
            json = gson.toJson(emergencyCards_arrayList); // myObject - instance of MyObject
            new_EmergencyCards_Prefs.putString("ESS_Cards", json);
            new_EmergencyCards_Prefs.commit();
            System.out.println("set_EmergencyCards_Prefs--------------------------------------------------------------------------------------------Done");
        }

    }

}
