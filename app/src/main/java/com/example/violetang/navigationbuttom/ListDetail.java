package com.example.violetang.navigationbuttom;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author: Jiali
 * Date: Nov. 2018
 * Description: list detail class
 */
public class ListDetail extends AppCompatActivity {

    private DatabaseHelper myDB;
    private Integer listid;
    private ImageView IconBack;

    EditText listName;
    EditText listDes;

    Button btnSave;
    Button btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_detail);

        myDB = new DatabaseHelper(this);

        listName = (EditText) findViewById(R.id.listdetail_name);
        listDes = (EditText) findViewById(R.id.listdetail_des);

        btnSave = (Button) findViewById(R.id.listdetail_save);
        btnDelete = (Button) findViewById(R.id.listdetail_delete);

        IconBack = (ImageView)findViewById(R.id.listdetail_toolbar_back);
        TextView bar_name = (TextView) findViewById(R.id.listdetail_toolbar_barname);

        Intent intent = getIntent();
        listid = intent.getIntExtra("LIST_ID", -1);


        Cursor list_detail = myDB.getListById(listid);
        if(list_detail.getCount() == 0){
            display("Error","NO Data Found.");
            return;
        }

        while(list_detail.moveToNext()){
            String one = list_detail.getString(1);
            listName.setText(one);
            bar_name.setText(list_detail.getString(1));
            listDes.setText(list_detail.getString(2));
        }

        IconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDB.deleteListbyId(listid);
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listname = listName.getText().toString();
                String listdes = listDes.getText().toString();
                myDB.updateListById(listid, listname, listdes);
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
}
