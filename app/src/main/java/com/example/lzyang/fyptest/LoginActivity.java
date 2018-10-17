package com.example.lzyang.fyptest;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.AsyncResponse;
import com.example.lzyang.fyptest.Database.BackgroundWorker;


/**
 * Created by User on 30/10/2017.
 */

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    EditText loginid, loginpassword;
    private boolean loggedIn = false;
    String idName;
    String userid;
    String userpass;
    AlertDialog alertDialog;
    public static final String PREFS_NAME = "LoginPrefs";
    private ProgressDialog mProgress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginid = (EditText) findViewById(R.id.loginid);
        loginpassword = (EditText) findViewById(R.id.loginpass);


            mProgress = new ProgressDialog(this);
            String titleId = "Signing in...";
            mProgress.setTitle(titleId);
            mProgress.setMessage("Please Wait...");


        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").toString().equals("logged")) {
            // emergencyCards_arrayList get SharedPreferences
//            init_EmergencyCards_Prefs(this);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        Toast.makeText(LoginActivity.this,settings.getString("userid",""),Toast.LENGTH_LONG).show();



    }

    /*protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean("loggedin", false);
        System.out.println("hi im no 1");
        //If we will get true
        if (loggedIn==true) {

            //We will start the Profile Activity
            SharedPreferences shared = getSharedPreferences("SHARED_PREF_NAME", Context.MODE_PRIVATE);
            //  SharedPreferences.Editor editor = sharedPreferences.edit();
            Intent intent = new Intent(Login.this, MainActivity.class);
            idName = shared.getString("ID_SHARED_PREF", userid);
            Toast.makeText(Login.this, idName, Toast.LENGTH_SHORT).show();
            intent.putExtra("ID", idName.trim());
            startActivity(intent);
        }
    }*/

    public void Onregister(View view) {
        Intent register_page = new Intent(view.getContext(), RegisterActivity.class);
        view.getContext().startActivity(register_page);
    }

    public void OnExit(View v){
        finish();
        System.exit(0);
    }

    public void OnLogin(View view) {
        userid = loginid.getText().toString();
        userpass = loginpassword.getText().toString();
        if (userid.isEmpty() || userpass.isEmpty()) {
            if (userid.isEmpty()) {
                loginid.setError("Please fill up this");
            } else if (userpass.isEmpty()) {
                loginpassword.setError("Please fill up this");
            }
        } else {
            mProgress.show();
            String type = "login";
            final BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate= this;
            backgroundWorker.execute(type,userid,userpass);
        }
    }

    @Override
    public void processFinish(String result){
        if(result.equals("login Successful")){
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("logged", "logged");
            editor.putString("userid",userid);
            editor.commit();



            Toast.makeText(LoginActivity.this,userid,Toast.LENGTH_LONG).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            mProgress.dismiss();
        }
        else if (result.equals("login not success")) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setCancelable(true);
            dialog.setTitle("Invalid Student ID or Password !!");
            dialog.setMessage("Please try enter the correct user ID and Password ! Thank You " );


            dialog.setPositiveButton("OK , Got it !", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                    // asd
                    mProgress.dismiss();
                }
            });

            final AlertDialog alert = dialog.create();
            alert.show();

        }
    }
}

