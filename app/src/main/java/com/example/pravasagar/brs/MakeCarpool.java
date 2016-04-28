package com.example.pravasagar.brs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;

import android.util.Log;
import android.widget.Toast;

import java.sql.*;
import java.util.ArrayList;

public class MakeCarpool extends Activity {

    private EditText day_et, time_et, maxSeats_et, licensePlate_et, startStreet_et,
            startCity_et, startState_et, startZIP_et;
    private CheckBox checkbox; // still need to make this checkbox work
    private String userID, day, licensePlate, startStreet, startCity, startState, startZIP;
    private int time, maxSeats;
    private Thread t = null;
    private String carpoolSQL;

    ArrayList<String> wholeData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_carpool);

        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");

        userID = wholeData.get(2);

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

                // the SQL insert statement for the route_details table
                carpoolSQL = "insert into route_details values (null, '" + userID + "', " + maxSeats + ", '" +
                        day + "', '" + time + "', '" + licensePlate + "', '" + startStreet + "', '" +
                        startZIP + "', '" + startCity + "', '" + startState + "');";

                t = new Thread(background);
                t.start();

                Log.i("SQL", carpoolSQL);

                Toast.makeText(MakeCarpool.this, "Carpool was Created", Toast.LENGTH_LONG).show();

                // I figured a user would want to see their carpool in the View Carpool activity
                // after making one.  Feel free to change where the user is directed
                Intent launchView = new Intent(MakeCarpool.this, ViewCarpool.class);
                startActivity(launchView);
            }
        });
    }

    private Runnable background = new Runnable() {
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            String username = "asalvatori";
            String password = "cs680";

            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (ClassNotFoundException e) {

            }

            Statement stmt = null;
            Connection con = null;
            try {
                con = DriverManager.getConnection(URL, username, password);
                stmt = con.createStatement();
            }
            catch (SQLException e) {

            }

            try {
                stmt.executeUpdate(carpoolSQL);

                t = null;
                con.close();
            }
            catch (SQLException e) {

            }
        }
    };
}