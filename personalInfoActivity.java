package com.example.violetang.navigationbuttom;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class personalInfoActivity extends AppCompatActivity {
    private ImageView editPage;
    private ImageView goBack;
    private Cursor cursor;
    private DatabaseHelper myDB;
    private TextView username;
    private TextView nickname;
    private TextView email;
    private TextView instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        goBack();
        edit();
        initView();
    }

    private void goBack(){
        goBack = (ImageView) findViewById(R.id.personal_info_bar_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            // listen the click action, if click, finish this page and go back to main page
            public void onClick(View v) {
                personalInfoActivity.this.finish();
            }
        });
    }

    private void edit(){
        editPage = (ImageView) findViewById(R.id.personal_info_bar_edit);
        editPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent edit = new Intent(editPage.getContext(), EditPersonalInfoActivity.class);
                startActivityForResult(edit,1);
            }
        });
    }

    private void initView(){
        username = (TextView) findViewById(R.id.personal_user_name);
        nickname = (TextView) findViewById(R.id.personal_info_name);
        email = (TextView) findViewById(R.id.personal_info_email);
        instruction = (TextView) findViewById(R.id.personal_info_instruction);
        myDB = new DatabaseHelper(this);
        cursor = myDB.showUserData();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                username.setText(cursor.getString(1));
                nickname.setText(cursor.getString(2));
                email.setText(cursor.getString(3));
                instruction.setText(cursor.getString(4));
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        initView();
    }
}
