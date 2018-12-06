package com.example.lzyang.fyptest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.AsyncResponse;
import com.example.lzyang.fyptest.Database.BackgroundWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements AsyncResponse {

    private EditText prof_name, prof_contact, prof_email, prof_pass;
    private TextView prof_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prof_name = (EditText) findViewById(R.id.prof_name);
        prof_contact = (EditText) findViewById(R.id.prof_contact);
        prof_pass = (EditText) findViewById(R.id.prof_pass);
        prof_id = (TextView) findViewById(R.id.prof_id);
        prof_email = (EditText) findViewById(R.id.prof_email);

        SharedPreferences sharePrefS = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        //prof_name.setText(sharePrefS.getString("Username",""));
        //prof_contact.setText(sharePrefS.getString("Password",""));
        //prof_email.setText(sharePrefS.getString("ContactNo",""));
        //prof_pass.setText(sharePrefS.getString("Email",""));

        String userid = sharePrefS.getString("userid","");
        String userpass = sharePrefS.getString("userpass","");
        String username = sharePrefS.getString("username","");
        String contactno = sharePrefS.getString("contactno","");
        String email = sharePrefS.getString("email","");

        prof_id.setText(userid.toString());
        prof_name.setText(username.toString());
        prof_contact.setText(contactno.toString());
        prof_pass.setText(userpass.toString());
        prof_email.setText(email.toString());

    }

    public void OnUpdate(View view) {

        String userid = prof_id.getText().toString();
        String username = prof_name.getText().toString();
        String userpass = prof_pass.getText().toString();
        String contactNo = prof_contact.getText().toString();
        String email = prof_email.getText().toString();

        if (userpass.isEmpty() || username.isEmpty() || contactNo.isEmpty() || email.isEmpty()) {
            if (username.isEmpty()) {
                prof_name.setError("Please fill up this");
            } else if (userpass.isEmpty()) {
                prof_pass.setError("Please fill up this");
            } else if (contactNo.isEmpty() ) {
                prof_contact.setError("Please fill up this");
            }
            else if (!username.matches("[a-zA-Z]+")){
                prof_name.requestFocus();
                prof_name.setError("Enter only Alphabetical Character");
            }
            else if (!userpass.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$"))
            {
                prof_pass.requestFocus();
                prof_pass.setError("Must have at least 1 number and 1 character");
            }
            else if (contactNo.matches("^(?=.*[@#$%^&+=])$")) {
                prof_contact.requestFocus();
                prof_contact.setError("Enter only Numerical number");
            }
            else if (email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")) {
                prof_contact.requestFocus();
                prof_contact.setError("Enter a valid email");
            }

        } else {

            String type = "update";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate= this;
            backgroundWorker.execute(type, userid, userpass, username,contactNo, email);


        }
    }

    public void processFinish(String result) {
        if (result.equals("Values have been updated successfully")) {
            Toast success = Toast.makeText(EditProfileActivity.this, "Successfully Updated", Toast.LENGTH_LONG);
            success.show();
            Intent profile_page = new Intent(EditProfileActivity.this, ProfileActivity.class);
            startActivity(profile_page);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditProfileActivity.this);
            dialog.setCancelable(true);
            dialog.setTitle("Update Error");
            dialog.setMessage("Sorry we cant update your profile");
            dialog.setPositiveButton("OK !", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();

        }
    }

    public void OnBack(View view) {
        Intent profile_page = new Intent(view.getContext(), ProfileActivity.class);
        view.getContext().startActivity(profile_page);
    }
}
