package com.example.violetang.navigationbuttom;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*
AboutActivity class to generate an About page
@author: Runyu Xu
@version: 1.0.1
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    /*
    Override onCreate function to generate an About page
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about2);

        // set a onClickListener to get return action (return to main page)
        // create an ImageView object -- goBack which links to id:about_bar_return in xml file
        final ImageView goBack = (ImageView) findViewById(R.id.about_bar_return);
        goBack.setOnClickListener(new View.OnClickListener() {
            // listen the click action, if click, finish this page and go back to main page
            public void onClick(View v) {
               /*switch (v.getId()){
                   case R.id.about_bar_return:
                       Intent intent = new Intent(goBack.getContext(), MainActivity.class);
                       startActivity(intent);
            }*/
               AboutActivity.this.finish();
               }
        });
        final TextView link = (TextView) findViewById(R.id.url);
        link.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github)));
                        startActivity(browse);
                }
        });
    }
}
