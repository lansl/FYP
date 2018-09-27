package com.example.lzyang.fyptest.Functions;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import com.example.lzyang.fyptest.Entity.EmergencyCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;
import static com.example.lzyang.fyptest.ServerModule.MJobScheduler.emergencyCards_recyclerAdapter;

/**
 * Created by Lz-Yang on 7/11/2017.
 */

public class ProcessingEmergencyCard extends AsyncTask<Void,Void,Void> {

    private Context context;

    private ArrayList<EmergencyCard> arrayList;
    private String address;
    private double longitude,latitude;

    public ProcessingEmergencyCard(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        arrayList = emergencyCards_arrayList.get_arrayList();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(int position = 0; position < arrayList.size() ; position++){
            longitude = Double.parseDouble(arrayList.get(position).getLongitude());
            latitude = Double.parseDouble(arrayList.get(position).getLatitude());
            try {
                address = getCurrent_address(latitude,longitude);
                arrayList.get(position).setLocation_address(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        emergencyCards_arrayList.set_arrayList(arrayList);
//        set_EmergencyCards_Prefs(arrayList);
        emergencyCards_recyclerAdapter.setArrayList(arrayList);
//        System.out.println("Run emergencyCards_recyclerAdapter finished");
        emergencyCards_recyclerAdapter.refreshRecyclerAdapter();
    }

    public String getCurrent_address(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        return addresses.get(0).getAddressLine(0);
    }
}
