package com.example.lzyang.fyptest;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordRequest extends StringRequest{
    private static final String url = "unbloodied-heat.000webhostapp.com/forgot.php";
    private Map<String, String> params;

    public ForgotPasswordRequest(String email, Response.Listener<String> listener){
        super(Request.Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("Email", email);
    }

    @Override
    public Map<String,String> getParams(){
        return params;
    }
}
