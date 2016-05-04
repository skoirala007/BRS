package com.example.pravasagar.brs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MakeCarpool extends AppCompatActivity {

    ArrayList<String> wholeData = new ArrayList<>();
    // the handler is only used for the checkbox
    Handler handler = new Handler() {
        public void handleMessage (Message msg) {
            startStreet_et.setText(startStreet);
            startCity_et.setText(startCity);
            startState_et.setText(startState);
            startZIP_et.setText(startZIP);
        }
    };
    private EditText day_et, time_et, maxSeats_et, licensePlate_et, startStreet_et,
            startCity_et, startState_et, startZIP_et;
    private CheckBox checkbox;
    private String userID, day, licensePlate, startStreet, startCity, startState, startZIP;
    private int time, maxSeats;
    private Thread t = null;
    private Thread s = null;
    private String carpoolSQL;
    private Runnable insert = new Runnable() {
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

                stmt.executeUpdate(carpoolSQL);

                t = null;
                con.close();
            }
            catch (SQLException e) {

            }
        }
    };
    private Runnable select = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            String username = "asalvatori";
            String password = "cs680";

            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (ClassNotFoundException e) {
                Log.i("SQL", "Did not load driver");
            }

            Statement stmt = null;
            Connection con = null;
            try {
                con = DriverManager.getConnection(URL, username, password);
                stmt = con.createStatement();

                String query = "select Street, city, state, Zip from user where User_Id = '" + userID + "';";
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    startStreet = rs.getString("Street");
                    startCity = rs.getString("city");
                    startState = rs.getString("state");
                    startZIP = rs.getString("Zip");
                }

                Log.i("SQL", startStreet + " " + startCity + " " + startState + " " + startZIP);

                // a generic message sent to the main thread to fill in the user's address
                Message msg = handler.obtainMessage(0, "");
                handler.sendMessage(msg);

                s = null;
                con.close();
            }
            catch (SQLException e) {
                Log.i("SQL", "Something wrong happened");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_carpool);

        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");

        userID = wholeData.get(2); // gets the username

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

                t = new Thread(insert);
                t.start();

                Log.i("SQL", carpoolSQL);

                Toast.makeText(MakeCarpool.this, "Carpool was Created", Toast.LENGTH_LONG).show();

                // Goes back to the dashboard
                Intent launchDash = new Intent(MakeCarpool.this, Dashboard.class);
                launchDash.putExtra("wholeData", wholeData);
                startActivity(launchDash);
            }
        });

        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (checkbox.isChecked()){
                    s = new Thread(select);
                    s.start();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:
                finish();
                return true;
            case R.id.exit:
                Intent exitIntent = new Intent(Intent.ACTION_MAIN);
                exitIntent.addCategory(Intent.CATEGORY_HOME);
                exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exitIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}