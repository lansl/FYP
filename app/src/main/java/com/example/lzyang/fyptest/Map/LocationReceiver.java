package com.example.lzyang.fyptest.Map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.lzyang.fyptest.DetailActivity;
import com.example.lzyang.fyptest.R;
import com.example.lzyang.fyptest.RequestActivity;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Lz-Yang on 21/11/2017.
 */

public class LocationReceiver {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 9;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double[] GPS_location = new double[2];
    private Context context;

    private TextView textViewCurrentLoc;

    public LocationReceiver(final Context context) {
        this.context = context;
    }

    public void initGPSlocation() {
        if (this.context instanceof RequestActivity) {
            textViewCurrentLoc = (TextView) ((Activity) context).findViewById(R.id.text_view_Current_Loc);
        }

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("Location Change");
                GPS_location[0] = location.getLongitude();
                GPS_location[1] = location.getLatitude();
                if (context instanceof RequestActivity) {
                    textViewCurrentLoc.setText("Longitude: " + GPS_location[0] + "\nLatitude: " + GPS_location[1]);
                } else if (context instanceof DetailActivity) {
                    GoogleRoute_Task googleRouteTask = new GoogleRoute_Task(context);
                    googleRouteTask.execute(GPS_location[0], GPS_location[1]);
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        };

        checkVersion_Permission();
    }

    //------------------Location Service--------------------------------------------------------------------------------------------------------------------------------
    public void checkVersion_Permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, MY_PERMISSIONS_REQUEST_LOCATION);
                System.out.println("Checked Location");
                return;
            }
            System.out.println("Out Location");
            configureButton();
        } else {
            System.out.println("Run Location");
            configureButton();
        }
    }

    public void configureButton() {
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    public void stopLocationUpdate(){
        locationManager.removeUpdates(locationListener);
    }

    public double[] getGPSLocation(){
        return GPS_location;
    }

}
