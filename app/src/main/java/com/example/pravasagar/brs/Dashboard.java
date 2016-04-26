package com.example.pravasagar.brs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;


public class Dashboard extends AppCompatActivity {
    Button see;
    Button make;
    Button find;
    TextView welCome;

    String wholeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Get Extra value from login
        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getString("wholeData");




        //Set reference for the buttons
        find = (Button) findViewById(R.id.find);
        make =(Button) findViewById(R.id.make);
        see = (Button) findViewById(R.id.see);
        welCome = (TextView) findViewById(R.id.welcome);
        welCome.setText("Welcome To Dashboard");
        welCome.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, android.R.anim.slide_in_left));
    }

    //Onclick listener for making carpool
    public void seeCarpool(View view){
        Intent lauchSee = new Intent(Dashboard.this,ViewCarpool.class);
        startActivity(lauchSee);


    }

    //Onclick listener for find carpool
    public void findCarpool(View view){
        Intent launchFind = new Intent(Dashboard.this, FindCarpool.class);
        launchFind.putExtra("wholeData", wholeData);
        startActivity(launchFind);

    }

    //Onclick listener for make carpool

    public void makeCarpool(View view){
        Intent lauchMake = new Intent(Dashboard.this, MakeCarpool.class);
        startActivity(lauchMake);


    }


}
