package com.example.lzyang.fyptest.Functions;

import android.app.Application;

import com.example.lzyang.fyptest.Entity.EmergencyCard;

import java.util.ArrayList;

/**
 * Created by LzYang on 17/8/2017.
 */
public class EmergencyCard_App extends Application {
    ArrayList<EmergencyCard> arrayList = new ArrayList<>();

    public ArrayList<EmergencyCard> get_arrayList() {
        return arrayList;
    }

    public void set_arrayList(ArrayList<EmergencyCard> main_arrayList) {
        this.arrayList = main_arrayList;
    }

}
