package com.example.violetang.navigationbuttom;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/** Public class EditPersonalInfoActivity extends AppCompatActivity
 *  Author: Runyu Xu
 *  Propose: to edit personal information and store into database
 */
public class EditPersonalInfoActivity extends AppCompatActivity {
    //global variables
    //data base part
    private DatabaseHelper myDB; // database name
    private Cursor cursor; // cursor to get data from database

    //Frontend part
    private ImageView goBack; // the image that click on to go back
    private EditText userName; // edit area to enter or update username
    private EditText nickName; // edit area to enter or update nickname
    private EditText email; // edit area to enter or update email
    private EditText instruction; // edit area to enter or update instruction
    private ImageView photo;
    private Button save; // Button to save information into database
    private String username; // string of username
    private String nickname; // string of nickname
    private String emailAddress; // string of email address
    private String instructions; // string of instruction

    /**
     * Override onCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_info);
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
        photo = (ImageView) findViewById(R.id.edit_personal_info_image);
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
}
