package com.example.violetang.navigationbuttom;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *  Author: Jiali
 *  Date: Nov. 2018
 *  Description: TaskFragment class
 */
public class TaskFragment extends Fragment {

    ListView myToDoTasksList;   //to do task list
    ListView myComTasksList;    // complete task list

    ImageView tasktoolBarSetting;   //setting icon
    ImageView tasktoolbarAdd;   //add task icon

    DatabaseHelper myDB;    //SQLite database

    ItemAdapterTest taskItemAdapter;    //TaskList item Adapter

    public TaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize
        Resources res =getResources();
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        myDB = new DatabaseHelper(getActivity());

        //Two listView (todo + complete)
        myToDoTasksList = (ListView)view.findViewById(R.id.tasklist_ListView);
        myComTasksList = (ListView)view.findViewById(R.id.completelist_ListView);

        //tool bar ImageView(setting + adding)
        tasktoolBarSetting = (ImageView)view.findViewById(R.id.listbar_setting);
        tasktoolbarAdd = (ImageView)view.findViewById(R.id.listbar_addNewList);

        listViewCreate(myToDoTasksList,0);
        listViewCreate(myComTasksList,1);

        /*======================================================================================
         * Add new task
         * User click the add icon, jump to another activity(AddTaskActivity.activity)
        ======================================================================================*/
        tasktoolbarAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(tasktoolbarAdd.getContext(),AddTaskActivity.class);
                startActivity(Addtask_Intent);
            }
        });

        /*======================================================================================
        * Setting icon
        * User click Setting icon, show a setting menu.
        * click items of the menu, go to corresponing pages.
        ======================================================================================*/
        tasktoolBarSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(MyApplication.getAppContext(), v);
                popup.getMenuInflater().inflate(R.menu.titlebar_settingmenu, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.titleBar_Setting_Pinfo:
                                Intent PersonInfo_Intent = new Intent(tasktoolBarSetting.getContext(),personalInfoActivity.class);
                                startActivity(PersonInfo_Intent);
                                break;

                            case R.id.titleBar_Setting_About:
                                Intent About_Intent = new Intent(tasktoolBarSetting.getContext(), AboutActivity.class);
                                startActivity(About_Intent);
                                break;

                            default:
                                break;
                        }
                        return true;
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        listViewCreate(myToDoTasksList,0); //update the listview
        listViewCreate(myComTasksList,1);   //update the listview
        taskItemAdapter.notifyDataSetChanged();

    }

    /*======================================================================================
     * Make the listview has dynamic size
     * enable two listviews in one page
    ======================================================================================*/
    public static void setDynamicHeight(ListView listView) {
        ItemAdapterTest adapter = (ItemAdapterTest)listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    /*======================================================================================
     * create listview for tasks
     ======================================================================================*/
    public void listViewCreate(ListView temp, int status){
        ArrayList<String> taskItems = new ArrayList<>();
        ArrayList<String> taskDes = new ArrayList<>();
        ArrayList<Integer> taskStatus = new ArrayList<>();
        final ArrayList<Integer> taskId = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        String d = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String m = Integer.toString(c.get(Calendar.MONTH)+1);
        String y = Integer.toString(c.get(Calendar.YEAR));

        m = (String) DateFormat.format("MM",c);
        d = (String) DateFormat.format("dd",c);
        y = (String) DateFormat.format("yyyy",c);

        String task_date = m+d+y;

        //select the tasks from database by date
        Cursor todo_task_data = myDB.getTaskbyDate(task_date,status);
        while(todo_task_data.moveToNext()) {
            taskId.add(todo_task_data.getInt(0));
            taskItems.add(todo_task_data.getString(1));
            taskDes.add(todo_task_data.getString(2));
            taskStatus.add(todo_task_data.getInt(4));

        }
        taskItemAdapter = new ItemAdapterTest(MyApplication.getAppContext(), taskId,taskItems, taskDes, taskStatus);
        temp.setAdapter(taskItemAdapter);
        setDynamicHeight(temp); //set dynmic height for listview

        //task clickListener
        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskid = taskId.get(position);
                Intent item_detail = new Intent(view.getContext(), ItemDetail.class);
                item_detail.putExtra("TASK_ID",taskid);
                startActivity(item_detail);
            }
        });

    }

}
