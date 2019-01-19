package com.example.violetang.navigationbuttom;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Author: Runyu Xu
 * Date: Nov. 2018
 */
public class calenderFragment extends Fragment {

    CalendarView calendarView;
    TextView myDate;
    ListView myList;

    ImageView mysetting;
    ImageView addNewTask;

    DatabaseHelper myDB;
    CalendarAdapter calendarItemAdapter;

    public calenderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Resources res = getResources();
        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        myDate = (TextView) view.findViewById(R.id.calendarbar_text);

        myList = (ListView) view.findViewById(R.id.task_ListView);

        mysetting = (ImageView) view.findViewById(R.id.calendarbar_setting);
        setting();

        addNewTask = (ImageView) view.findViewById(R.id.calendarbar_addNewList);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(addNewTask.getContext(),AddTaskActivity.class);
                startActivity(Addtask_Intent);
            }
        });

        myDB = new DatabaseHelper(getActivity());
        viewSetting();

        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Calendar cal_2 = Calendar.getInstance();
                cal_2.set(year,month,day);
                String m = (String) DateFormat.format("MM",cal_2);
                String d = (String) DateFormat.format("dd",cal_2);
                String y = (String) DateFormat.format("yyyy",cal_2);

                String task_date_2 = m + d + y; // generate the task_date for backend
                String date = (month + 1) + "/" + day + "/" + year;
                myDate.setText(date);

                taskId.clear();
                taskItems.clear();
                taskDes.clear();
                taskStatus.clear();

                Cursor todo_task_data = myDB.getTaskbyDate(task_date_2,0);
                while(todo_task_data.moveToNext()) {
                    taskId.add(todo_task_data.getInt(0));
                    taskItems.add(todo_task_data.getString(1));
                    taskDes.add(todo_task_data.getString(2));
                    taskStatus.add(todo_task_data.getInt(4));
                }

                CalendarAdapter calendarItemAdapter= new CalendarAdapter(getContext(), taskId, taskItems, taskDes,taskStatus);
                myList.setAdapter(calendarItemAdapter);
            }


        });

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
                //Toast.makeText(MyApplication.getAppContext(),"Item clicked "+taskid,Toast.LENGTH_LONG).show();
            }
        });



        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
        viewSetting();
        calendarItemAdapter.notifyDataSetChanged();

    }

    public void setting(){
        mysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.calendarbar_setting:

                        PopupMenu popup = new PopupMenu(MyApplication.getAppContext(), v);
                        popup.getMenuInflater().inflate(R.menu.titlebar_settingmenu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.titleBar_Setting_Pinfo:
                                        Intent PersonInfo_Intent = new Intent(mysetting.getContext(),personalInfoActivity.class);
                                        startActivity(PersonInfo_Intent);
                                        break;

                                    case R.id.titleBar_Setting_About:
                                        Intent About_Intent = new Intent(mysetting.getContext(), AboutActivity.class);
                                        startActivity(About_Intent);
                                        break;

                                    default:
                                        break;
                                }
                                return true;
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void viewSetting(){
        Calendar cal = Calendar.getInstance();

        final String mm = (String) DateFormat.format("MM",cal);
        final String dd = (String) DateFormat.format("dd",cal);
        final String yy = (String) DateFormat.format("yyyy",cal);
        String task_date = mm+dd+yy;
        myDate.setText(task_date);

        final ArrayList<String> taskItems = new ArrayList<>();
        final ArrayList<String> taskDes = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();
        final ArrayList<Integer> taskStatus = new ArrayList<>();

        Cursor todo_task_data = myDB.getTaskbyDate(task_date,0);
        while(todo_task_data.moveToNext()) {
            taskId.add(todo_task_data.getInt(0));
            taskItems.add(todo_task_data.getString(1));
            taskDes.add(todo_task_data.getString(2));
            taskStatus.add(todo_task_data.getInt(4));

        }
        calendarItemAdapter= new CalendarAdapter(getContext(), taskId, taskItems, taskDes,taskStatus);
        myList.setAdapter(calendarItemAdapter);

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
                //Toast.makeText(MyApplication.getAppContext(),"Item clicked "+taskid,Toast.LENGTH_LONG).show();
            }
        });
    }
}
