package com.example.lzyang.fyptest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.graphics.drawable.VectorDrawable;

import com.example.lzyang.fyptest.Database.AsyncResponse;
import com.example.lzyang.fyptest.Database.BackgroundWorker;
import com.example.lzyang.fyptest.Functions.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by User on 30/10/2017.
 */

public class RegisterActivity extends AppCompatActivity implements AsyncResponse {
    EditText editid, editname, editpassword,editContact, editEmail;
    ImageButton insert_image;
    String userid, username, userpass, email;
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editid = (EditText) findViewById(R.id.editID);
        editname = (EditText) findViewById(R.id.editName);
        editpassword = (EditText) findViewById(R.id.editPass);
        editContact =(EditText) findViewById(R.id.editTextCotact);
        editEmail = (EditText)findViewById(R.id.editTextEmail);
        //editEmail = (EditText)findViewById(R.id.editTextEmail);
        insert_image = (ImageButton) findViewById(R.id.insert_image);
        insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }

        });

    }
    public Bitmap getImageToBitmap(){

        insert_image.setDrawingCacheEnabled(true);
        // this is the important code :)
        // Without it the view will have a dimension of 0,0 and the bitmap will be null
        insert_image.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        insert_image.layout(0, 0, insert_image.getMeasuredWidth(), insert_image.getMeasuredHeight());
        insert_image.buildDrawingCache(true);
        Bitmap bitmap = ((BitmapDrawable) insert_image.getDrawable()).getBitmap();

        return bitmap;
    }
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public void Onsave(View view) {

        String userid = editid.getText().toString();
        String username = editname.getText().toString();
        String userpass = editpassword.getText().toString();
        String contactNo = editContact.getText().toString();
        String email = editEmail.getText().toString();
        Bitmap ess_inserted_image = getImageToBitmap();
        String img_bytes = Base64.encodeToString(getBytesFromBitmap(ess_inserted_image), Base64.DEFAULT);



        if (userid.isEmpty() || userpass.isEmpty() || username.isEmpty()|| email.isEmpty()){
            if (userid.isEmpty()) {
                editid.setError("Please fill up this");
            } else if (username.isEmpty()) {
                editname.setError("Please fill up this");
            } else if (userpass.isEmpty()) {
                editpassword.setError("Please fill up this");
            }else if (email.isEmpty()) {
                editEmail.setError("Please fill up this");
            }
        }
        else if (userid.matches("[a-zA-Z]+")) {
            editid.requestFocus();
            editid.setError("Enter only Numerical number");
        }
        else if (!username.matches("[a-zA-Z]+")){
            editname.requestFocus();
            editname.setError("Enter only Alphabetical Character");
        }
        else if (!userpass.matches("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{4,}$"))
        {
            editpassword.requestFocus();
            editpassword.setError("Must have at least 1 number and 1 character");
        }
        else if (contactNo.matches("^(?=.*[@#$%^&+=])$")) {
            editContact.requestFocus();
            editContact.setError("Enter only Numerical number");
        }
        else if (email.matches("^(?=.*[@#$%^&+=])$")) {
            editEmail.requestFocus();
            editEmail.setError("Enter only Numerical number");
        }
        else
        {
            String type = "register";
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.delegate= this;
            backgroundWorker.execute(type, userid, userpass, username,img_bytes,contactNo, email);

        }

    }

    @Override
    public void processFinish(String result) {
        if (result.equals("Values have been inserted successfully")) {
            Toast success = Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG);
            success.show();
            Intent login_page = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(login_page);
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
            dialog.setCancelable(true);
            dialog.setTitle("Register Error");
            dialog.setMessage("Sorry we cant register");
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


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(RegisterActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
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
        insert_image.getLayoutParams().height = 500;//set appropriate sizes
        insert_image.getLayoutParams().width= 500;
        insert_image.requestLayout();;
        insert_image.setImageBitmap(thumbnail);

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
        insert_image.getLayoutParams().height = 500;//set appropriate sizes
        insert_image.getLayoutParams().width= 500;
        insert_image.requestLayout();;
        insert_image.setImageBitmap(bm);

    }

}
