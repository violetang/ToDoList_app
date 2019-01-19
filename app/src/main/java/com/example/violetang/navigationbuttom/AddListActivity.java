package com.example.violetang.navigationbuttom;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 Author: Jiali
 Date: Nov. 2018
 Description: Add List
 */
public class AddListActivity extends AppCompatActivity {
    private String list_name;
    private String list_des;

    private Button btnClear;
    private Button btnDone;
    private Button btnTest;

    EditText lname, ldes;

    private DatabaseHelper myDB;
    private ImageView IconBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        myDB = new DatabaseHelper(this);

        lname = (EditText)findViewById(R.id.addlist_name);
        lname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lname.getText().clear();
            }
        });

        ldes = (EditText)findViewById(R.id.addlist_description);
        ldes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ldes.getText().clear();
            }
        });

        btnClear = (Button)findViewById(R.id.addlist_clear);
        btnDone = (Button)findViewById(R.id.addlist_Done);
        IconBack = (ImageView)findViewById(R.id.addlist_toolbar_back);

        // btnTest = (Button)findViewById(R.id.addlist_test); //for test

        addList();
        viewClear();
        comeBack();
    }

    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lname.getText().clear();
                ldes.getText().clear();
            }
        });
    }

    public void addList(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the input
                list_name = lname.getText().toString();
                list_des = ldes.getText().toString();

                //insert the task into database
                if(list_name.length() != 0){ //valid input

                    //public boolean addTaskData( String name, String des, String date, int list)
                    boolean insertList = myDB.addListData(list_name,list_des);
                    if(insertList){
                        finish();
                        Toast.makeText(AddListActivity.this, "List created!", Toast.LENGTH_LONG).show();
                    }else{
                        //show input error
                        Toast.makeText(AddListActivity.this, "Invalid Input, please check again!"+list_name+list_des, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(AddListActivity.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
