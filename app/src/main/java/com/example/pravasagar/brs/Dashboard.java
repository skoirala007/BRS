package com.example.pravasagar.brs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class Dashboard extends AppCompatActivity {
    Button see;
    Button make;
    Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Set reference for the buttons
        find = (Button) findViewById(R.id.find);
        make =(Button) findViewById(R.id.make);
        see = (Button) findViewById(R.id.see);
    }

    //Onclick listener for making carpool
    public void seeCarpool(View view){
        Intent lauchSee = new Intent(Dashboard.this,ViewCarpool.class);
        startActivity(lauchSee);


    }

    //Onclick listener for find carpool
    public void findCarpool(View view){
        Intent launchFind = new Intent(Dashboard.this, FindCarpool.class);
        startActivity(launchFind);

    }

    //Onclick listener for make carpool

    public void makeCarpool(View view){
        Intent lauchMake = new Intent(Dashboard.this, MakeCarpool.class);
        startActivity(lauchMake);


    }


}
