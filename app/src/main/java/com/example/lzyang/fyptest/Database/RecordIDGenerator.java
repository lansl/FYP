package com.example.lzyang.fyptest.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.lzyang.fyptest.Entity.EmergencyCard;
import com.example.lzyang.fyptest.ServerModule.PublishTaskMQTT;

/**
 * Created by Lz-Yang on 27/11/2017.
 */

public class RecordIDGenerator {

    private Context context;
    private EmergencyCard emergencyCard;

    private String counted_record = "";

    public RecordIDGenerator(Context context, EmergencyCard emergencyCard) {
        this.context = context;
        this.emergencyCard = emergencyCard;
    }

    public void startCountRecord_Database(){
        new CountRecord_Database().execute();
    }

    public void setRecordID_Generated(){
        String RecordID = null;
//        String countedRecord = countRecord_Database();
        int int_countedRecord;
        if(counted_record == ""){
            RecordID = "1";
        }else{
            int_countedRecord = Integer.parseInt(counted_record);
            int_countedRecord = int_countedRecord + 1;
            RecordID = String.valueOf(int_countedRecord);
            System.out.println(""+int_countedRecord);
        }

        PublishTaskMQTT publishActivity = new PublishTaskMQTT(context);
        emergencyCard.setRecord_ID(RecordID);
        publishActivity.setUpPayload(emergencyCard);
    }

    class CountRecord_Database extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
//            try {
//                URL url = new URL(retrieveURL);
//                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
//                httpUrlConnection.setRequestMethod("POST");
//                httpUrlConnection.setDoOutput(true);
//                httpUrlConnection.setDoInput(true);
//                OutputStream outputStream = httpUrlConnection.getOutputStream();
//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                outputStream.close();
//                InputStream inputStream = httpUrlConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream, "iso-8859-1")));
//                String line = "";
//                while ((line = bufferedReader.readLine()) != null) {
//                    counted_record += line;
//                    System.out.println(""+counted_record);
//                }
//                bufferedReader.close();
//                inputStream.close();
//                httpUrlConnection.disconnect();
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setRecordID_Generated();
        }
    }

//    public String countRecord_Database(){
//        String counted_record = null;
//        try {
//            URL url = new URL(retrieveURL);
//            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
//            httpUrlConnection.setRequestMethod("POST");
//            httpUrlConnection.setDoOutput(true);
//            httpUrlConnection.setDoInput(true);
//            OutputStream outputStream = httpUrlConnection.getOutputStream();
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            outputStream.close();
//            InputStream inputStream = httpUrlConnection.getInputStream();
//            BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream, "iso-8859-1")));
//            String line = "";
//            while ((line = bufferedReader.readLine()) != null) {
//                counted_record += line;
//            }
//            bufferedReader.close();
//            inputStream.close();
//            httpUrlConnection.disconnect();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return counted_record;
//    }
}
