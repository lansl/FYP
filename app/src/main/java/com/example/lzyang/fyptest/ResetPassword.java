package com.example.lzyang.fyptest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.BackgroundWorker;

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

    public void check_mail(View view){
        email_address = edtEmail.getText().toString();
        if (email_address.isEmpty()) {
                edtEmail.setError("Please fill up this");
        } else {
            String type = "forgot_password";
            final BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            //backgroundWorker.delegate= this;
            //backgroundWorker.execute(type,userid,userpass);
        }
    }

    public void OnLogin(View view) {
        Intent login_page = new Intent(view.getContext(), LoginActivity.class);
        view.getContext().startActivity(login_page);
    }
}
