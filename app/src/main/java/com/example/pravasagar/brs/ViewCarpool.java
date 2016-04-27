package com.example.pravasagar.brs;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewCarpool extends AppCompatActivity {

    private Thread t = null;
    private String days, timings, DriverID;
    private List<ScheduleView> scheduleItems = new ArrayList<ScheduleView>();
    private ScheduleListAdapter SchduleItemsAdapter;

    //Create Handler object to handle messages placed on queue
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            Log.i("Magic number found1","calling the handler1.");
            SchduleItemsAdapter = new ScheduleListAdapter(getBaseContext(), R.layout.scheduleitem, (List<ScheduleView>) msg.obj);
            ListView listView = (ListView) findViewById(R.id.rideList);
            listView.setAdapter(SchduleItemsAdapter);
                Log.i("Magic number found", String.valueOf(msg.obj));
            t.interrupt();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_carpool);


        t = new Thread(background);
        t.start();
    }

    private Runnable background = new Runnable() {
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            String username = "asalvatori";
            String password = "cs680";

            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver");

            }
            Statement stmt = null;
            Connection con = null;
            try { //create connection to database
                con = DriverManager.getConnection(
                        URL,
                        username,
                        password);
                stmt = con.createStatement();

                ResultSet result = stmt.executeQuery(
                        "Select s.Schd_Id,s.Route_Id,s.Days,s.Timing,r.Driver_Id " +
                                "FROM schedules s,route_details r where r.Schd_Id = s.Schd_Id " +
                                "AND r.Driver_Id = 'shru'; ");
                //for each record in schedule
                while (result.next()) {
                    days = result.getString("Days");
                    timings = result.getString("Timing");
                    DriverID = result.getString("Driver_Id");
                }
                ScheduleView schedule = new ScheduleView(days,timings,DriverID);
                scheduleItems.add(schedule);
                Message msg = handler.obtainMessage(1, scheduleItems);
                handler.sendMessage(msg);
                Log.e("Data", days + " " + timings);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };


}
