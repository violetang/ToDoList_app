package com.example.violetang.navigationbuttom;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Author: Runyu Xu
 * Date: Nov. 2018
 */
public class EditPersonalInfoActivity extends AppCompatActivity {

    private DatabaseHelper myDB; // database name
    private Cursor cursor; // cursor to get data from database

    //Frontend part
    private ImageView goBack; // the image that click on to go back
    private EditText userName; // edit area to enter or update username
    private EditText nickName; // edit area to enter or update nickname
    private EditText email; // edit area to enter or update email
    private EditText instruction; // edit area to enter or update instruction
    private Button save; // Button to save information into database
    private String username; // string of username
    private String nickname; // string of nickname
    private String emailAddress; // string of email address
    private String instructions; // string of instruction

    private ImageView image;
    private Button camera;
    private Button photo;
    private Bitmap head;
    private static String path = "/sdcard/DemoHead"; //sd path

    /**
     * Override onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        goBack();
        initView();
        saveInfo();
    }

    /**
     * goBack() function to go back to previous page
     */
    private void goBack(){
        goBack = (ImageView) findViewById(R.id.edit_personal_info_bar_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            // listen the click action, if click, finish this page and go back to main page
            public void onClick(View v) {
                EditPersonalInfoActivity.this.finish();
            }
        });
    }

    /**
     * initView() function to initialize the edit page
     */
    private void initView(){
        userName = (EditText) findViewById(R.id.edit_personal_info_username);
        nickName = (EditText) findViewById(R.id.edit_personal_info_name);
        email = (EditText) findViewById(R.id.edit_personal_info_email);
        instruction = (EditText) findViewById(R.id.edit_personal_info_instruction);
        image = (ImageView) findViewById(R.id.edit_personal_info_image);
        camera = (Button) findViewById(R.id.edit_personal_camera);
        photo = (Button) findViewById(R.id.edit_personal_photo);
        myDB = new DatabaseHelper(this); // initialize database
        cursor = myDB.showUserData(); // get data from database
        if (cursor.getCount()!= 0) { // if there's a user in the database
            while (cursor.moveToNext()) { // get all information we want and set to fr
                username = cursor.getString(1);
                nickname = cursor.getString(2);
                emailAddress = cursor.getString(3);
                instructions = cursor.getString(4);
                userName.setText(username);
                nickName.setText(nickname);
                email.setText(emailAddress);
                instruction.setText(instructions);
            }
        }
        // clear the text area to enter new text
        userName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                userName.getText().clear();
            }
        });
        nickName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nickName.getText().clear();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                email.getText().clear();
            }
        });
        instruction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                instruction.getText().clear();
            }
        });
        camera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    Intent _camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    _camera.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                    "head.jpg")));
                    startActivityForResult(_camera,1);
                }catch(Exception e){
                    Toast.makeText(EditPersonalInfoActivity.this,
                            "cannot start camera", Toast.LENGTH_LONG).show();
                }
            }
        });
        photo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent _photo = new Intent(Intent.ACTION_PICK, null);
                _photo.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(_photo,2);
            }
        });

        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");
        if(bt != null){
            image.setImageBitmap(bt);
        }else{

        }
    }

    /**
     * saveInfo() method to save data
     */
    private void saveInfo(){
        save = (Button) findViewById(R.id.edit_personal_info_bar_save);
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //get strings from user input
                username = userName.getText().toString();
                nickname = nickName.getText().toString();
                emailAddress = email.getText().toString();
                instructions = instruction.getText().toString();
                //check if it is a valid input
                if (username.length() != 0 ) { //valid input
                    if (cursor.getCount() == 0) {
                        boolean insertTask = myDB.addPersonData(username, nickname, emailAddress, instructions);
                        if (insertTask) {
                            Toast.makeText(EditPersonalInfoActivity.this, "Success!", Toast.LENGTH_LONG).show();
                        } else {
                            //show input error
                            Toast.makeText(EditPersonalInfoActivity.this, "Invalid Input, please check again!", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Integer id = new Integer(1);
                        myDB.updateUserInfo(id,username,nickname,emailAddress,instructions);
                        Toast.makeText(EditPersonalInfoActivity.this, "Success!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(EditPersonalInfoActivity.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
                myDB.close();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));//crop photo
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    cropPhoto(data.getData());//crop photo
                }
                break;
            case 3:
                if(data != null){
                    Bundle newImage = data.getExtras();
                    head = newImage.getParcelable("data");
                    if(head != null){
                        setPicToView(head);
                        image.setImageBitmap(head);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void cropPhoto(Uri uri){
        Intent crop = new Intent("com.android.camera.action.CROP");
        crop.setDataAndType(uri, "image/*");
        crop.putExtra("crop", "true");
        // aspectX aspectY
        crop.putExtra("aspectX", 1);
        crop.putExtra("aspectY", 1);
        // outputX outputY
        crop.putExtra("outputX", 100);
        crop.putExtra("outputY", 100);
        crop.putExtra("return-data", true);
        startActivityForResult(crop, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // check if sd is available
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// create a file folder
        String fileName = path + "head.jpg";// image name
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// write and compress the data

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
