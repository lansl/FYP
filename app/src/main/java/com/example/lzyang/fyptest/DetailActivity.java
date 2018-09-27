package com.example.lzyang.fyptest;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.Clicked_Rescue_Task;
import com.example.lzyang.fyptest.Database.Clicked_Solved_Task;
import com.example.lzyang.fyptest.Entity.EmergencyCard;
import com.example.lzyang.fyptest.Map.DirectionFinderListener;
import com.example.lzyang.fyptest.Map.LocationReceiver;
import com.example.lzyang.fyptest.Map.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import static com.example.lzyang.fyptest.Entity.Storage_Emergency_Cards.emergencyCards_arrayList;
import static com.example.lzyang.fyptest.LoginActivity.PREFS_NAME;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {
    GoogleMap googleMap;
    MapFragment mapFragment;

    private ImageView detail_Image;
    private TextView detail_Title, detail_Desc, detail_Date, detail_Time, detail_locationaddress, detail_noOfRescuer;
    private Button button_rescue, button_solved;
    private double longitude, latitude;
    private String recordID, userID;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    private LocationReceiver locationReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Help Request Details");

        detail_Image = (ImageView) findViewById(R.id.ess_detail_img);
        detail_Title = (TextView) findViewById(R.id.ess_detail_title);
        detail_Desc = (TextView) findViewById(R.id.ess_detail_description);
        detail_Date = (TextView) findViewById(R.id.ess_detail_date);
        detail_Time = (TextView) findViewById(R.id.ess_detail_time);
        detail_locationaddress = (TextView) findViewById(R.id.locationAddress);
        detail_noOfRescuer = (TextView) findViewById(R.id.ess_detail_noRescuer);

        button_rescue = (Button) findViewById(R.id.btn_rescue);
        button_solved = (Button) findViewById(R.id.btn_solved);

        new SetUpDetails_Task().execute();

        button_rescue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(DetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Rescue Action");
                dialog.setPositiveButton("Rescue ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Clicked_Rescue_Task clicked_rescue_task = new Clicked_Rescue_Task(DetailActivity.this);
                        clicked_rescue_task.execute(recordID,userID);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
                final android.support.v7.app.AlertDialog alert = dialog.create();
                alert.show();
            }
        });

        button_solved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(DetailActivity.this);
                dialog.setCancelable(true);
                dialog.setTitle("Rescue Action");
                dialog.setPositiveButton("Solved ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        locationReceiver.stopLocationUpdate();
                        Clicked_Solved_Task clicked_solved_task = new Clicked_Solved_Task(DetailActivity.this);
                        clicked_solved_task.execute(recordID);
                        dialog.cancel();
                        DetailActivity.this.finish();
                    }
                })
                        .setNegativeButton("Not solved ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
                final android.support.v7.app.AlertDialog alert = dialog.create();
                alert.show();
            }
        });



        if(googleServicesAvailable()){
            //Check googleServicesAvailable Toast
//            Toast.makeText(this,"Perfect",Toast.LENGTH_LONG).show();
            initMap();
            locationReceiver = new LocationReceiver(DetailActivity.this);
            locationReceiver.initGPSlocation();
        }else{
            //Add Retry googleServicesAvailable
        }
    }

    //-------------Validation on Buttons----------------------------------------------------------------
    public void checkButton_Visible(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,0);
        String userID = settings.getString("userid","");
        if(this.userID.equals(userID)){
            button_rescue.setVisibility(View.GONE);
            button_solved.setVisibility(View.VISIBLE);
        }else{
            button_rescue.setVisibility(View.VISIBLE);
            button_solved.setVisibility(View.GONE);
        }
    }

    //-------------Google Map SetUP----------------------------------------------------------------
    private void initMap(){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable(){
        GoogleApiAvailability api= GoogleApiAvailability.getInstance();
        int isAvailable=api.isGooglePlayServicesAvailable(this);
        if(isAvailable== ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog=api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(this,"Cant Connect to play service",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onMapReady(GoogleMap gMap) {
        System.out.println(latitude);
        System.out.println(longitude);
        googleMap = gMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {

        }

        //Edit the following as per you needs
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        //
        LatLng placeLocation = new LatLng(latitude,longitude ); //Make them global
        Marker placeMarker = googleMap.addMarker(new MarkerOptions().position(placeLocation)
                .title("I'm Here!"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeLocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(500), 1000, null);
    }

    @Override
    public void onDirectionFinderStart() {
        System.out.print("HI IM HERE1");
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        System.out.print("HI IM HERE2");
        for (Route route : routes) {
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.duration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.distance)).setText(route.distance.text);

            originMarkers.add(googleMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(googleMap.addMarker(new MarkerOptions()
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(googleMap.addPolyline(polylineOptions));
        }
    }

    //-------------setUpDetails_Task----------------------------------------------------------------
    class SetUpDetails_Task extends AsyncTask<Void,Void,Void> {
        private ArrayList<EmergencyCard> arrayList;

        private Bitmap img_bitmap;
        private String title, desc, date, time, locatiomaddress;
        private int position = 0 , noOfRescuer = 0;
        private boolean validation = true;
        @Override
        protected void onPreExecute() {
            System.out.println("Hey what happened!!");

            arrayList = emergencyCards_arrayList.get_arrayList();
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                position = extras.getInt("Position");
            }else{
                validation = false;
                System.out.println("Can't take the position!!");
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(validation == true){
                recordID = arrayList.get(position).getRecord_ID();
                userID = arrayList.get(position).getUser_ID();
                img_bitmap = arrayList.get(position).getImg();
                title = arrayList.get(position).getTitle();
                desc = arrayList.get(position).getDescription();
                date = arrayList.get(position).getDate();
                time = arrayList.get(position).getTime();
                longitude = Double.parseDouble(arrayList.get(position).getLongitude());
                latitude = Double.parseDouble(arrayList.get(position).getLatitude());
                locatiomaddress = arrayList.get(position).getLocation_address();
                noOfRescuer = arrayList.get(position).getNoOfRescuer();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(validation == true){
                System.out.println(title + " " + desc );
                detail_Image.setImageBitmap(img_bitmap);
                detail_Title.setText(title);
                detail_Desc.setText(desc);
                detail_Date.setText(date);
                detail_Time.setText(time);
                detail_locationaddress.setText(locatiomaddress);
                detail_noOfRescuer.setText(noOfRescuer + "");
            }
            checkButton_Visible();
        }
    }

    @Override
    public void onBackPressed() {
        locationReceiver.stopLocationUpdate();
        finish();
    }
}
