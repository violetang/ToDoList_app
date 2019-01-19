package com.example.violetang.navigationbuttom;


import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *  Author: Jiali
 *  Date: Nov. 2018
 *  Description: ListFragment class
 *  Todo: 1.listview need update right after(haven't solve yet)
 *        2.task click action
 *        3.selected list changes color
 */
public class ListFragment extends Fragment {

    DatabaseHelper myDB;

    ImageView setting;
    ImageView addNewTask;
    ImageView addList;

    ListAdapter listItemAdapter;

    ListView listBar;
    ListView tasksOfTheList_todo;
    ListView getTasksOfTheList_complete;

    TextView listbar;

    int list_ID;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize and match layout
        myDB = new DatabaseHelper(getActivity());

        Resources res =getResources();
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        listbar = (TextView)view.findViewById(R.id.listbar_title);

        //Tool bar
        setting = (ImageView)view.findViewById(R.id.listbar_setting);
        setting(); //setting icon clickListener

        addNewTask = (ImageView)view.findViewById(R.id.listbar_addNewList);
        addListTask();  //addtask icon clickListener

        addList =(ImageView)view.findViewById(R.id.addlist_imageview);
        addlist();  //addlist icon clickListener

        //right task bar
        tasksOfTheList_todo = (ListView)view.findViewById(R.id.taskBartodo_ListView);
        getTasksOfTheList_complete = (ListView)view.findViewById(R.id.taskBarcomplete_ListView);

        //left list Bar
        listBar = (ListView)view.findViewById(R.id.listNavBar);
        listbarCreated(listBar); //the list of lists

        //listviewClicksetting(tasksOfTheList_todo,list_ID,0);
        //listviewClicksetting(getTasksOfTheList_complete,list_ID,1);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
        listbarCreated(listBar);
        listItemAdapter.notifyDataSetChanged();

    }


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


    public void addlist(){
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addlist_Intent = new Intent(addList.getContext(),AddListActivity.class);
                startActivity(Addlist_Intent);
            }
        });
    }

    public void addListTask(){
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Addtask_Intent = new Intent(addNewTask.getContext(),ListAddTask.class);
                startActivity(Addtask_Intent);
            }
        });
    }

    public void setting(){
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.listbar_setting:

                        PopupMenu popup = new PopupMenu(MyApplication.getAppContext(), v);
                        popup.getMenuInflater().inflate(R.menu.titlebar_settingmenu, popup.getMenu());
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.titleBar_Setting_Pinfo:
                                        Intent PersonInfo_Intent = new Intent(setting.getContext(),personalInfoActivity.class);
                                        startActivity(PersonInfo_Intent);

                                        break;

                                    case R.id.titleBar_Setting_About:
                                        //Toast.makeText(MyApplication.getAppContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                                        Intent About_Intent = new Intent(setting.getContext(), AboutActivity.class);
                                        //send any info to about page
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

    public void listbarCreated(ListView listItems){

        final ArrayList<Integer> myListId = new ArrayList<>();
        final ArrayList<String> list_Name = new ArrayList<>();

        Cursor getMyList = myDB.getLists();
        while (getMyList.moveToNext()) {

            myListId.add(getMyList.getInt(0));
            list_Name.add(getMyList.getString(1));

        }

        listItemAdapter = new ListAdapter(MyApplication.getAppContext(),myListId,list_Name);
        listItems.setAdapter(listItemAdapter);

        //list longpress listener
        listItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get the list_id
                list_ID = myListId.get(position);

                Intent list_detail = new  Intent(view.getContext(),ListDetail.class);
                list_detail.putExtra("LIST_ID",list_ID);
                startActivity(list_detail);
                return true;
            }
        });

        //list click listener
        //click the list --> show corresponding list of tasks
        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                list_ID = myListId.get(position);
                listbar.setText(list_Name.get(position));
                //Toast.makeText(MyApplication.getAppContext(), "List" + list_id + "Clicked", Toast.LENGTH_LONG).show();

                final ArrayList<Integer> todoTasksId = new ArrayList<>();
                ArrayList<String> todoTasks = new ArrayList<>();
                ArrayList<String> todoTasksDes = new ArrayList<>();
                ArrayList<Integer> taskStatus = new ArrayList<>();

                //database query: get corresponding task by list_id
                Cursor list_task = myDB.getTaskByList(list_ID,0);

                while(list_task.moveToNext()) {
                    todoTasksId.add(list_task.getInt(0));
                    todoTasks.add(list_task.getString(1));
                    todoTasksDes.add(list_task.getString(2));
                    taskStatus.add(list_task.getInt(4));
                }
                ItemAdapterTest taskItemAdapter = new ItemAdapterTest(MyApplication.getAppContext(),todoTasksId,todoTasks,todoTasksDes,taskStatus);
                tasksOfTheList_todo.setAdapter(taskItemAdapter);
                setDynamicHeight(tasksOfTheList_todo); //set dynmic height for listview

                //listviewClicksetting(tasksOfTheList_todo,list_ID,0); //click to-do task action

                final ArrayList<Integer> ComTasksId = new ArrayList<>();
                ArrayList<String> completeTask = new ArrayList<>();
                ArrayList<String> completeTaskDes = new ArrayList<>();
                ArrayList<Integer> CompletetaskStatus = new ArrayList<>();

                Cursor list_task_completed = myDB.getTaskByList(list_ID,1);

                while(list_task_completed.moveToNext()) {
                    ComTasksId.add(list_task_completed.getInt(0));
                    completeTask.add(list_task_completed.getString(1));
                    completeTaskDes.add(list_task_completed.getString(2));
                    CompletetaskStatus.add(list_task_completed.getInt(4));
                }
                ItemAdapterTest taskItemAdapter2 = new ItemAdapterTest(MyApplication.getAppContext(),ComTasksId,completeTask,completeTaskDes,CompletetaskStatus);
                getTasksOfTheList_complete.setAdapter(taskItemAdapter2);
                setDynamicHeight(getTasksOfTheList_complete); //set dynmic height for listview

                //listviewClicksetting(getTasksOfTheList_complete,list_ID,1); //click complete task action

                /*
                getTasksOfTheList_complete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int task_detail_2 = ComTasksId.get(position);
                        Intent task = new Intent(view.getContext(), ItemDetail_List.class);
                        task.putExtra("LIST_TASK_ID",task_detail_2);
                        startActivity(task);
                    }
                });
                */

            }
        });

    }

    //click the task, jump to the task detail page
    public void listviewClicksetting(ListView temp, final int listID, final int status){

        temp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final ArrayList<Integer> TasksId = new ArrayList<>();

                //database query: get corresponding task by list_id
                Cursor list_task = myDB.getTaskByList(listID,status);
                while(list_task.moveToNext()) {
                    TasksId.add(list_task.getInt(0));
                }

                int taskid = TasksId.get(position);

                //Toast.makeText(MyApplication.getAppContext(), "task" + taskid + "Clicked", Toast.LENGTH_LONG).show();
                Intent task_detail = new Intent(view.getContext(), ItemDetail_List.class);
                task_detail.putExtra("LIST_TASK_ID",taskid);
                startActivity(task_detail);
            }
        });


    }
}
