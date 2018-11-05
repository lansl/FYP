package com.example.lzyang.fyptest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzyang.fyptest.Database.RecordIDGenerator;
import com.example.lzyang.fyptest.Entity.EmergencyCard;
import com.example.lzyang.fyptest.Functions.Utility;
import com.example.lzyang.fyptest.Map.LocationReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.lzyang.fyptest.Entity.CommandClass.setCommandPayload;
import static com.example.lzyang.fyptest.ServerModule.MQTTConnectionTask.client;

public class RequestActivity extends AppCompatActivity {
    private ImageButton btnRefresh;
    private TextView textViewCurrentLoc;
    private ImageButton edit_image;
    private EditText editText_description,editText_othertitle;
    private Spinner emergency_dropdown;

    private static final int VIDEO_CAPTURE = 101;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private Uri fileUri;

    private LocationReceiver locationReceiver;

    private double[] GPS_location = new double[2];

    public static ProgressDialog mProgress;

        double loc1, loc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        setTitle("Create Request");

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Publishing...");
        mProgress.setMessage("Please Wait...");

        // Get ID from UI
        edit_image = (ImageButton) findViewById(R.id.ess_add_image);
        editText_othertitle=(EditText) findViewById(R.id.edit_otherTitle);
        emergency_dropdown= (Spinner)findViewById(R.id.titleSpinner);
        editText_description = (EditText) findViewById(R.id.edit_description);
        textViewCurrentLoc = (TextView) findViewById(R.id.text_view_Current_Loc);

        //choose Image
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        // Select title
        emergency_dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String selected = parentView.getItemAtPosition(position).toString();
                if(selected.equals("Other")){
                    editText_othertitle.setEnabled(true);
                    editText_othertitle.setVisibility(selectedItemView.VISIBLE);
                    editText_othertitle.setClickable(true);
                }
                else {
                    editText_othertitle.setEnabled(false);
                    editText_othertitle.setClickable(false);
                    editText_othertitle.setVisibility(selectedItemView.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
            }
        });

        // Get GPS location-----------------------------------------------------------
        locationReceiver = new LocationReceiver(this);
        locationReceiver.initGPSlocation();
        GPS_location = locationReceiver.getGPSLocation();
        textViewCurrentLoc.setText("Longitude: " + GPS_location[0] + "\nLatitude: " + GPS_location[1]);
        System.out.println("GPS_location[0]" + GPS_location[0]);
        System.out.println("GPS_location[1]" + GPS_location[1]);

        btnRefresh = (ImageButton) findViewById(R.id.ess_refresh);      //btnRefresh set OnClickListener
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationReceiver.configureButton();
                textViewCurrentLoc.setText("Longitude: " + GPS_location[0] + "\nLatitude: " + GPS_location[1]);
                loc1 = GPS_location[0];
                loc2 = GPS_location[1];
            }
        });

        //This is for 1km//
        //GPS_location[0] >= 101.7199 && GPS_location[0] <= 101.7390 && GPS_location[1] >= 3.2072 && GPS_location[1] <= 3.2253//

    }

//--------------------------------------------------------------------------------------------------------------------------------------------
    // Check Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationReceiver.MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    locationReceiver.configureButton();
                return;
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if(userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if(userChoosenTask.equals("Choose from Library"))
                            galleryIntent();
                        else if(userChoosenTask.equals("Capture Video"))
                            VideoIntent();
                    } else {
                        //code for deny
                    }
                    break;
            }
        }

//--------------------------------------------------------------------------------------------------------------------------------------------
    //Choose Image Or Capture Photo
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Capture Video", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(RequestActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                }
                else if (items[item].equals("Capture Video")) {
                    userChoosenTask ="Capture Video";
                    if(result)
                        VideoIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    private void VideoIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    public void startRecording(View view){
        File mediaFile =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/myvideo.mp4");

        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri videoUri = Uri.fromFile(mediaFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        edit_image.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        edit_image.setImageBitmap(bm);
    }

//--------------------------------------------------------------------------------------------------------------------------------------------
    // Get Image to Bitmap From ImageView
    public Bitmap getImageToBitmap(){

        edit_image.setDrawingCacheEnabled(true);
        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        edit_image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        edit_image.layout(0, 0, edit_image.getMeasuredWidth(), edit_image.getMeasuredHeight());
        edit_image.buildDrawingCache(true);
        Bitmap bitmap = edit_image.getDrawingCache(true).copy(Bitmap.Config.RGB_565, false);

        return bitmap;
    }
//--------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            System.out.println("Send Information");
            EmergencyCard emergencyCard;
            Bitmap ess_inserted_image;
            String title , description, command;

            //Set command for Payload
            do {
                command = setCommandPayload(id);
            }while (command == null);
            System.out.println("Command" + command);
            ess_inserted_image = getImageToBitmap();

            if(String.valueOf(emergency_dropdown.getSelectedItem()).equals("Other")){
                title= editText_othertitle.getText().toString();
            }
            else {
                title = String.valueOf(emergency_dropdown.getSelectedItem());
            }

            description = editText_description.getText().toString();

            if(client.isConnected()){
                if(!title.isEmpty() && !description.isEmpty() && ess_inserted_image != null && GPS_location[0] != 0.0 && GPS_location[1] != 0.0){
                    mProgress.show();
                    emergencyCard = new EmergencyCard(
                            ess_inserted_image,
                            title,
                            String.valueOf(GPS_location[1]),
                            String.valueOf(GPS_location[0]),
                            description);
                    emergencyCard.setCommand(command);
                    RecordIDGenerator recordIDGenerator = new RecordIDGenerator(this,emergencyCard);
                    recordIDGenerator.startCountRecord_Database();
//                    PublishTaskMQTT publishActivity = new PublishTaskMQTT(this);
//                    publishActivity.setUpPayload(emergencyCard);

                }else{
                    //add dialog for reqiure title
                    android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(RequestActivity.this);
                    dialog.setCancelable(true);
                    dialog.setTitle("Incomplete Message Error");
                    dialog.setMessage("Please fill in all the flied !" );
                    dialog.setPositiveButton("OK !", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final android.support.v7.app.AlertDialog alert = dialog.create();
                    alert.show();
                }
            }else{
                System.out.println("You haven connect the MQTT Server!!!");
                Toast.makeText(this,"You haven connect the MQTT Server!!!",Toast.LENGTH_SHORT).show();
                //TRY reconnect to MQTT manually !!
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
