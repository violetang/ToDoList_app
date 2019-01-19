package com.example.violetang.navigationbuttom;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    /**
        Author: Jiali
        Date: Nov. 2018
        Description: MainActivity, using BottomNavigationView and FrameLayout, Users can click
        the tabs to switch different sections(Tasks, Lists, Calendar)
     */
    private BottomNavigationView mainNav;
    private FrameLayout mainFrame;
    private TaskFragment taskFragment;
    private ListFragment listFragment;
    private calenderFragment calenderFragment;

    DatabaseHelper myDB;    //SQLite database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //match the component in layout files
        mainNav =(BottomNavigationView) findViewById(R.id.main_nav);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        taskFragment = new TaskFragment();
        listFragment = new ListFragment();
        calenderFragment = new calenderFragment();

        myDB = new DatabaseHelper(this);    //check DatabaseHelper.java

        setFragment(taskFragment);

        //Buttom navigation Selected Listerner
        //set the tabs corresponding to the fragment
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){

                    case R.id.nav_task:
                        setFragment(taskFragment);
                        return true;

                    case R.id.nav_list:
                        setFragment(listFragment);
                        return true;

                    case R.id.nav_calender:
                        setFragment(calenderFragment);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    ////set current fragment
    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();

    }


}
