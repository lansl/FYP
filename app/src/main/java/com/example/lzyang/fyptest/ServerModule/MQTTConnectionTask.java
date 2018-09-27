package com.example.lzyang.fyptest.ServerModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.lzyang.fyptest.Functions.Action;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.lzyang.fyptest.Entity.CommandClass.getCommandAction;
import static com.example.lzyang.fyptest.LoginActivity.PREFS_NAME;

/**
 * Created by LzYang on 16/10/2017.
 */

public class MQTTConnectionTask {

    static String topicStr = "MY/TARUC/ESS/000000001/PUB";

    public static MqttAndroidClient client = null;
    static MqttConnectOptions options;

    public static void verify_connection(final Context context){
        if(client == null){
            initClient(context);
        }
        try {
            if(!client.isConnected()){
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        System.out.println("Connected");
                        Toast.makeText(context,"Connected",Toast.LENGTH_LONG).show();
                        setSubscription();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        System.out.println("Failed");
                        Toast.makeText(context,"Failed",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                setSubscription();
                System.out.println("Running MQTT Connected...");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        mCallBack(context);
    }

    public static void initClient(final Context context){
        System.out.println("Setting UP clientId For MQTT");

        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME,0);
        String userID = settings.getString("userid","");
        String userName = settings.getString("username","");
        String userPass = settings.getString("userpass","");
        String contactNo = settings.getString("contactno","");

//        String clientId = MqttClient.generateClientId();
        String clientId = userID + userName + contactNo;

        client = new MqttAndroidClient(context, "tcp://iot.eclipse.org:1883", clientId);

        options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(userPass.toCharArray());
        options.setAutomaticReconnect(true);
//        options.setKeepAliveInterval(60);
        System.out.println("Setting UP MQTT pass");
    }

    public static void mCallBack(final Context context){
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("MQTT no Connection");
                Toast.makeText(context,"MQTT no Connection",Toast.LENGTH_LONG).show();
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String message_Payload = new String(message.getPayload());

                String Analysed_Action;
                String msg_command;
                boolean retry = false;

                // Analyse Command
                try {
                    Payload_Executer payload_executer = new Payload_Executer(context);
                    JSONObject jsonObj = new JSONObject(message_Payload);
                    msg_command = Action.hexToAscii(jsonObj.getString("Command"));
                    do{
                        Analysed_Action = getCommandAction(msg_command);
                        if(Analysed_Action.equals("Emergency_Event_Massage")){
                            payload_executer.execute_Emergency_Event_Massage(jsonObj);
                            retry = false;
                        }else if(Analysed_Action.equals("Rescue_Action")){
                            payload_executer.execute_Emergency_Rescue_Action(jsonObj);
                            retry = false;
                        }else if(Analysed_Action.equals("Solved_Emergency_Event")){
                            payload_executer.execute_Emergency_Solved_Action(jsonObj);
                            retry = false;
                        }else if(Analysed_Action.equals("Retry_Action")){
                            retry = true;
                        }
                    }while(retry);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
            }
        });
    }

    public static void setSubscription(){
        try {
            client.subscribe(topicStr,2);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
