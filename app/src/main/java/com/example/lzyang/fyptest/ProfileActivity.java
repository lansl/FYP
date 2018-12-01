package com.example.lzyang.fyptest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.lzyang.fyptest.Database.UrlPhp_Database.retrieve2URL;
import static com.example.lzyang.fyptest.Functions.Action.byteToBitmap;

public class ProfileActivity extends AppCompatActivity {
    Button logout;
    TextView profilename,profileid,contactNo;

    public static final String PREFS_NAME = "LoginPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        circleImageView.setImageResource(R.drawable.profile);
        logout = (Button) findViewById(R.id.logoutButton);
        profilename = (TextView) findViewById(R.id.prof_name);
        profileid = (TextView) findViewById(R.id.profile_ID);
        contactNo = (TextView) findViewById(R.id.contact_no);


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String userid = settings.getString("userid", "");
        String username = settings.getString("username", "");
        String profileimage = settings.getString("profileimage", "");
        String contactno = settings.getString("contactno", "");

        profileid.setText(userid.toString());
        profilename.setText(username.toString());
        contactNo.setText(contactno.toString());

        byte[] profile_image = Base64.decode(profileimage, Base64.DEFAULT);

        circleImageView.setImageBitmap(byteToBitmap(profile_image));
    }

    //--------------------------------------------------------------
    public void onEdit(View v){
        Intent edit_page = new Intent(v.getContext(), EditProfileActivity.class);
        v.getContext().startActivity(edit_page);
    }

    public void onLogout(View v){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, 0);
        this.getSharedPreferences(PREFS_NAME, 0).edit().clear().commit();
        Intent login_page = new Intent(v.getContext(), LoginActivity.class);
        v.getContext().startActivity(login_page);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
