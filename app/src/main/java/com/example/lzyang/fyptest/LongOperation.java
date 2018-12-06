package com.example.lzyang.fyptest;

import android.os.AsyncTask;
import android.util.Log;

public class LongOperation extends AsyncTask<String, Void, String> {
    //still dunno how to pass variables here
    String opsPassword;// = "password";
    String opsEmail;// = "lansl-wa15@student.tarc.edu.my";
    public LongOperation (String password, String email){
        opsEmail = email;
        opsPassword = password;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            GMailSender sender = new GMailSender("lansl-wa15@student.tarc.edu.my", "nopassword970803");
            sender.sendMail("Password Recovery",
                    "The password for you account is:  "+opsPassword+" .Please do remember your password.",
                    "info@gmail.com",
                    ""+opsEmail)                   ;
        } catch (Exception e) {
            Log.e("error", e.getMessage(), e);
            return "Email Not Sent";
        }
        return "The Password has been sent to your registered Email";
    }
    @Override
    protected void onPostExecute(String result) {
        Log.e("LongOperation",result+"");
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }
}