package com.example.violetang.navigationbuttom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.DateFormat;
import java.util.Calendar;

/**
     Author: Jiali
     Date: Nov. 2018
     Description: Add new task with a date
 todo: datePickerDialog format should change
 */



public class AddTaskActivity extends AppCompatActivity {
    private String task_name;
    private String task_des;
    private String task_date;

    private Button btnClear;
    private Button btnDone;
    //private Button btnTest;

    EditText tname, tdes;

    private DatabaseHelper myDB;
    private ImageView IconBack;
    private TextView myDate;
    private DatePickerDialog.OnDateSetListener myDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        myDB = new DatabaseHelper(this);

        tname = (EditText)findViewById(R.id.addtask_name);
        tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
            }
        });

        tdes = (EditText)findViewById(R.id.addtask_description);
        tdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdes.getText().clear();
            }
        });


        btnClear = (Button)findViewById(R.id.addtask_clear);
        btnDone = (Button)findViewById(R.id.addtask_Done);
        IconBack = (ImageView)findViewById(R.id.addtask_toolbar_back);

        //btnTest = (Button)findViewById(R.id.addtask_test);

        myDate = (TextView) findViewById(R.id.addtask_Date_select);

        addTask();
        viewClear();
        comeBack();
        //test();

    }

    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addTask(){
        addDate();
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the input
                task_name = tname.getText().toString();
                task_des = tdes.getText().toString();
                task_date = myDate.getText().toString();

                //insert the task into database
                if(task_name.length() != 0){ //valid input

                    //public boolean addTaskData( String name, String des, String date, int list)
                    boolean insertTask = myDB.addTaskData(task_name,task_des,task_date);
                    if(insertTask){
                        finish();
                        Toast.makeText(AddTaskActivity.this, "Task created!", Toast.LENGTH_LONG).show();
                    }else{
                        //show input error
                        Toast.makeText(AddTaskActivity.this, "Invalid Input, please check again!"+task_name+task_des +task_date, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(AddTaskActivity.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /*
     Add date function to add the task tate
     */
    public void addDate(){
        myDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddTaskActivity.this, android.R.style.Theme_Material_Light,
                        myDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        myDateSetListener = new DatePickerDialog.OnDateSetListener() {
            String m,d,y,dateview;
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year,month,dayOfMonth);
                m = (String) DateFormat.format("MM",cal);
                d = (String) DateFormat.format("dd",cal);
                y = (String) DateFormat.format("yyyy",cal);
                task_date = m + d + y; // generate the task_date for backend
                //dateview = m + "/" + d + "/"+ y;
                myDate.setText(task_date); // show the date on frontend
            }
        };
    }

    public void viewClear(){
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
                tdes.getText().clear();
            }
        });
    }

    public void display(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
   /*
    public void test(){
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
                tdes.getText().clear();

                Cursor task_data = myDB.showTaskData();

                if(task_data.getCount() == 0){
                    //message
                    display("Error","NO Data Found.");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(task_data.moveToNext()){
                    buffer.append("ID: " + task_data.getString(0) + "\n");
                    buffer.append("Name: " + task_data.getString(1) + "\n");
                    buffer.append("Des: " + task_data.getString(2) + "\n");
                    buffer.append("Date: " + task_data.getString(3) + "\n");
                    buffer.append("Status: " + task_data.getString(4) + "\n");
                    buffer.append("List: " + task_data.getString(5) + "\n");

                    //display message
                    display("ALL stored data:", buffer.toString());
                }

            }
        });
    }
    */

}
