package com.example.violetang.navigationbuttom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: task detail class
 */
public class ItemDetail extends AppCompatActivity {

    private DatabaseHelper myDB;
    private Integer taskid;
    private ImageView IconBack;

    EditText taskName;
    EditText taskDes;
    TextView taskDate;

    Button btnSave;
    Button btnDelete;

    private DatePickerDialog.OnDateSetListener myDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        myDB = new DatabaseHelper(this);

        taskName = (EditText) findViewById(R.id.taskDetail_name);
        taskDes = (EditText) findViewById(R.id.taskDetail_description);
        taskDate = (TextView) findViewById(R.id.taskDetail_date);

        btnSave = (Button) findViewById(R.id.taskDetail_Edit);
        btnDelete = (Button) findViewById(R.id.taskDetail_Delete);

        IconBack = (ImageView)findViewById(R.id.taskdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.taskdetail_toolbar_barname);

         /*======================================================================================
         * get the intent 's content from task_fragment ListView
         * by "TASK_ID"
       ======================================================================================*/
        Intent intent = getIntent();
        taskid = intent.getIntExtra("TASK_ID",-1);

        /*======================================================================================
         * SQL query: get the task by given its ID;
         * And show the data into the front end
        ======================================================================================*/
        Cursor task_detail = myDB.getTaskbyId(taskid);
        if(task_detail.getCount() == 0){
            display("Error","NO Data Found.");
            return;
        }

        while(task_detail.moveToNext()){

            String one = task_detail.getString(1);
            taskName.setText(one);
            bar_name.setText(task_detail.getString(1));
            taskDes.setText(task_detail.getString(2));
            String temp_date = task_detail.getString(3);
            String m = temp_date.substring(0,2);
            String d = temp_date.substring(2,4);
            String y = temp_date.substring(4);
            String new_temp = m + "/" + d + "/"+ y;
            taskDate.setText(temp_date);
        }

        addDate();

          /*======================================================================================
         * click Icon back, back to previous page
       ======================================================================================*/
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteTaskbyId(taskid);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskname = taskName.getText().toString();
                String taskdes = taskDes.getText().toString();
                String taskdate = taskDate.getText().toString();
                myDB.updateTaskByIdNoList(taskid, taskname, taskdes, taskdate);
                finish();
            }
        });

    }

    public void display(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void addDate(){
        taskDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ //show the dialog for user to select date
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        ItemDetail.this, android.R.style.Theme_Material_Light,
                        myDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                String temp_date_1 = m + d + y; // generate the task_date for backend
                //dateview = m + "/" + d + "/"+ y;
                taskDate.setText(temp_date_1); // show the date on frontend
            }
        };
    }


}
