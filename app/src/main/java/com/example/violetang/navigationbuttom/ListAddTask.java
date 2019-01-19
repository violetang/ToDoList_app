package com.example.violetang.navigationbuttom;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: add task in list fragment
 */
public class ListAddTask extends AppCompatActivity {
    private String task_name;
    private String task_des;
    private int task_listid;

    private Button btnClear;
    private Button btnDone;
    private Button btnTest;

    EditText tname,tdes;
    Spinner lists;

    private DatabaseHelper myDB;
    private ImageView IconBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add_task);

        myDB = new DatabaseHelper(this);

        tname = (EditText)findViewById(R.id.listaddtask_name);
        tname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tname.getText().clear();
            }
        });

        tdes = (EditText)findViewById(R.id.listaddtask_description);
        tdes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tdes.getText().clear();
            }
        });

        btnClear = (Button)findViewById(R.id.listaddtask_clear);
        btnDone = (Button)findViewById(R.id.listaddtask_Done);
        IconBack = (ImageView)findViewById(R.id.listaddtask_toolbar_back);
       // btnTest = (Button)findViewById(R.id.listaddtask_test);

        viewClear();
        comeBack();
        addTask();

        lists = (Spinner)findViewById(R.id.listaddtask_List_select);
        ArrayList<String> listdownlist = new ArrayList<>();
        final ArrayList<Integer> listdownlist_ID = new ArrayList<>();
        Cursor getMyList = myDB.getLists();
        while(getMyList.moveToNext()){
            listdownlist_ID.add(getMyList.getInt(0));
            listdownlist.add(getMyList.getInt(0)+ " -- "+getMyList.getString(1));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listdownlist){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent){
                View view = super.getDropDownView(position, convertView,parent);
                TextView tv = (TextView) view;
                tv.setBackgroundColor(Color.parseColor("#ff7400"));
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lists.setAdapter(adapter);

        lists.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task_listid = listdownlist_ID.get(position);
                Toast.makeText(MyApplication.getAppContext(), "TEST" + task_listid, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    public void addTask(){
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the input
                task_name = tname.getText().toString();
                task_des = tdes.getText().toString();

                Toast.makeText(ListAddTask.this, task_name + task_des + task_listid, Toast.LENGTH_LONG).show();

                //insert the task into database
                if(task_name.length() != 0){ //valid input

                    boolean insertTaskList = myDB.addTaskInList(task_name,task_des,task_listid);
                    if(insertTaskList){
                        finish();
                        Toast.makeText(ListAddTask.this, "Task Created!", Toast.LENGTH_LONG).show();
                    }else{
                        //show input error
                        Toast.makeText(ListAddTask.this, "Invalid Input, please check again!"+task_name+task_des +task_listid, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ListAddTask.this, "Name can't be null !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void comeBack(){
        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
