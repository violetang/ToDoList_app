package com.example.violetang.navigationbuttom;

import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Author: Runyu Xu
 * Date: Nov. 2018
 */
public class CalendarAdapter extends BaseAdapter {

    LayoutInflater mInflater;

    ArrayList<String> task_items;
    ArrayList<String> task_descriptions;
    ArrayList<Integer>task_id;
    ArrayList<Integer>task_status;

    private DatabaseHelper myDB;

    //modify the constructor add new parameter date
    public CalendarAdapter(Context c, ArrayList<Integer> id, ArrayList<String> i, ArrayList<String> d, ArrayList<Integer> status){
        task_id = id;
        task_items = i;
        task_descriptions = d;
        task_status = status;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return task_items.size();
    }

    @Override
    public Object getItem(int position) {
        return task_items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.item_task, null);
        myDB = new DatabaseHelper(MyApplication.getAppContext());


        TextView nameTextView = (TextView)v.findViewById(R.id.taskName_TextView);
        TextView decriptionTextView = (TextView) v.findViewById(R.id.taskDes_TextView);
        final CheckBox taskCheckBox = (CheckBox) v.findViewById(R.id.taskCheckBox);

        final int index = position;

        String name = task_items.get(position);
        String desc = task_descriptions.get(position);
        int status = task_status.get(position);

        nameTextView.setText(name);
        decriptionTextView.setText(desc);

        if (status == 1) {
            taskCheckBox.setChecked(true);
        } else {
            taskCheckBox.setChecked(false);
        }

        taskCheckBox.setOnClickListener(new View.OnClickListener() {

            int id = task_id.get(index);

            @Override
            public void onClick(View v) {
                if(taskCheckBox.isChecked()){
                    myDB.setTaskComplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set Complete "+id,Toast.LENGTH_LONG).show();

                }
                else{
                    myDB.setTaskIncomplete(id);
                    Toast.makeText(MyApplication.getAppContext(),"set unComplete "+id,Toast.LENGTH_LONG).show();

                }
            }
        });
        return v;
    }

}