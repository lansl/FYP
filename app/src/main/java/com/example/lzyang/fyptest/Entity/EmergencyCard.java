package com.example.lzyang.fyptest.Entity;

import android.graphics.Bitmap;

/**
 * Created by LzYang on 27/7/2017.
 */
public class EmergencyCard {

    String command;
    String user_ID,record_ID;

    Bitmap img;
    String title, description, latitude, longitude, date, time;

    //extra for ess card
    String location_address;

    //extra for detailactivity
    int noOfRescuer = 0;

    //For RequestActivity
    public EmergencyCard(Bitmap img, String title, String latitude, String longitude, String description) {
        this.img = img;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    //For Payload_Executer

    public EmergencyCard(String record_ID, String user_ID, Bitmap img, String title, String description, String latitude, String longitude, String date, String time) {
        this.record_ID = record_ID;
        this.user_ID = user_ID;
        this.img = img;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getRecord_ID() {
        return record_ID;
    }

    public void setRecord_ID(String record_ID) {
        this.record_ID = record_ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public int getNoOfRescuer() {
        return noOfRescuer;
    }

    public void setNoOfRescuer(int noOfRescuer) {
        this.noOfRescuer = noOfRescuer;
    }
}
