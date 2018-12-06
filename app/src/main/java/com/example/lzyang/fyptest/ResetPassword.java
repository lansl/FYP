package com.example.lzyang.fyptest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lzyang.fyptest.Database.BackgroundWorker;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPassword extends AppCompatActivity {
    private EditText edtEmail;
    private Button btnResetPassword;
    private Button btnBack;

    String email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        edtEmail = (EditText) findViewById(R.id.edt_reset_email);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
    }

    public void check_mail(View view) {
        email_address = edtEmail.getText().toString();
        if (email_address.isEmpty()) {
            edtEmail.setError("Please fill up this");
            return;
        } else {

            Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse2 = new JSONObject(response);
                        boolean success = jsonResponse2.getBoolean("success");
                        if (success) {
                            String email = jsonResponse2.getString("Email");
                            //String  = jsonResponse2.getString("Email");
                            String password = jsonResponse2.getString("userPass");
                            SharedPreferences passPref = getSharedPreferences("passPref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor forgotEditor = passPref.edit();
                            forgotEditor.putString("email", email);
                            forgotEditor.putString("password", password);
                            forgotEditor.apply();
                            send(email, password);


                            Toast Esuccess = Toast.makeText(ResetPassword.this, "Email Sent. Check your email", Toast.LENGTH_LONG);
                            Esuccess.show();

                        } else {
                            String message = jsonResponse2.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {

                    }
                }
            };

            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(email_address, responseListener2);
            RequestQueue queue = Volley.newRequestQueue(ResetPassword.this);
            queue.add(forgotPasswordRequest);

        }
    }

    public void processFinish(String result) {
        if (result.equals("Values have been inserted successfully")) {
            Toast success = Toast.makeText(ResetPassword.this, "Successfully Registered", Toast.LENGTH_LONG);
            success.show();
            Intent login_page = new Intent(ResetPassword.this, LoginActivity.class);
            startActivity(login_page);
        }

    }
    public void send(String email, String password) {

        try {
            LongOperation longOps = new LongOperation(password, email);
            longOps.execute();  //sends the email in background
            Toast.makeText(this, longOps.get(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    public void OnLogin(View view) {
        Intent login_page = new Intent(view.getContext(), LoginActivity.class);
        view.getContext().startActivity(login_page);
    }
}
