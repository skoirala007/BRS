package com.example.pravasagar.brs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.view.View.OnClickListener;

import android.util.Log;

public class MakeCarpool extends Activity {

    private EditText day_et, time_et, maxSeats_et, licensePlate_et, startStreet_et,
            startCity_et, startState_et, startZIP_et;
    private CheckBox checkbox;
    private String day, licensePlate, startStreet, startCity, startState, startZIP;
    private int time, maxSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_carpool);

        day_et = (EditText)findViewById(R.id.dayOfWeek);
        time_et = (EditText)findViewById(R.id.timeOfDay);
        maxSeats_et = (EditText)findViewById(R.id.numberOfSeats);
        licensePlate_et = (EditText)findViewById(R.id.licensePlate);
        startStreet_et = (EditText)findViewById(R.id.startStreet);
        startCity_et = (EditText)findViewById(R.id.startCity);
        startState_et = (EditText)findViewById(R.id.startState);
        startZIP_et = (EditText)findViewById(R.id.startZIP);

        checkbox = (CheckBox)findViewById(R.id.sameAsHomeAddress);

        Button button = (Button)findViewById(R.id.createCarpoolButton);

        button.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                day = day_et.getText().toString();
                time = Integer.valueOf(time_et.getText().toString());
                maxSeats = Integer.valueOf(maxSeats_et.getText().toString());
                licensePlate = licensePlate_et.getText().toString();
                startStreet = startStreet_et.getText().toString();
                startCity = startCity_et.getText().toString();
                startState = startState_et.getText().toString();
                startZIP = startZIP_et.getText().toString();

                String carpoolSQL = "insert into Carpools values ('" + day + "', " + time + ", " +
                        maxSeats + ", '" + licensePlate + "', '" + startStreet + "', '" +
                        startCity + "', '" + startState + "', " + startZIP + ");";

                Log.i("SQL", carpoolSQL);
            }
        });
    }
}