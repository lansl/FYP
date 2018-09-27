package com.example.lzyang.fyptest.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;

/**
 * Created by Lz-Yang on 23/11/2017.
 */

public class GoogleRoute_Task extends AsyncTask<Double,Void,Void> {

    private Context context;
    private double originLatitude, originLongitude;
    LatLng destination, origin =null;

    public GoogleRoute_Task(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }
    @Override
    protected Void doInBackground(Double... params) {
        //Testing purpose
//        originLatitude= 3.242149;
//        originLongitude= 101.705299;

        originLatitude= params[1];
        originLongitude= params[0];
        System.out.println("origin" + originLatitude + "origin"+originLongitude);

        origin = new LatLng(originLatitude,originLongitude);
        destination = new LatLng(3.237829, 101.684020);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                try {
                    new DirectionFinder((DirectionFinderListener) context, origin, destination).execute();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return null;
    }
}
