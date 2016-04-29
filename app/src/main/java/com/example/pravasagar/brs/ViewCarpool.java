package com.example.pravasagar.brs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ViewCarpool extends AppCompatActivity {

    private Thread t = null;
    String phno = null;
    Statement stmt = null;
    private String days, timings, DriverID,riders,Name,phone;
    private List<ScheduleView> scheduleItems = new ArrayList<ScheduleView>();
    private ScheduleListAdapter SchduleItemsAdapter;
    ListView listView;
    private ContextMenu.ContextMenuInfo itemMenuInfo;
    private ScheduleView carpool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_carpool);

        listView = (ListView) findViewById(R.id.rideList);

        t = new Thread(background);
        t.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                carpool = scheduleItems.get(position); // this is how you get an object from an ArrayList for a ListView or a Spinner
                registerForContextMenu(view);
                //System.out.println(view);
            }
        });
    }


    //Create Handler object to handle messages placed on queue
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            SchduleItemsAdapter = new ScheduleListAdapter(getBaseContext(), R.layout.scheduleitem, (List<ScheduleView>) msg.obj);
            listView = (ListView) findViewById(R.id.rideList);
            listView.setAdapter(SchduleItemsAdapter);
            t.interrupt();
        }
    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        System.out.println("i am here" + v.toString());
        menu.setHeaderTitle("Select your action");
        menu.add(0, v.getId(), 0, "Call the owner");
        menu.add(0, v.getId(), 0, "SMS the owner");
        menu.add(0, v.getId(), 0, "Opt out of the group");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call the owner") {
            Toast.makeText(this, "Action 1 invoked", Toast.LENGTH_SHORT).show();
            callOwner(item);
        } else if (item.getTitle() == "SMS the owner") {
            Toast.makeText(this, "Action 2 invoked", Toast.LENGTH_SHORT).show();
            SMSOwner();
        } else if (item.getTitle() == "Opt out of the group") {
            Toast.makeText(this, "Action 3 invoked", Toast.LENGTH_SHORT).show();
            optOut(item);
        } else {
            return false;
        }
        return true;
    }

    private void optOut(MenuItem item) {
    }

    private void callOwner(MenuItem item) {

        //System.out.println(item);
        //System.out.println(carpool.getPhoneno());

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ carpool.getPhoneno()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);

    }
    private void SMSOwner() {

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
                        " SELECT u.phone, u.FirstName, r.day, r.timing, r.Driver_Id ,r.riders FROM user u,route_details r WHERE u.User_Id = r.Driver_Id AND r.Driver_Id = 'shru'; ");
                //for each record in schedule
                while (result.next()) {
                    days = result.getString("day");
                    timings = result.getString("timing");
                    DriverID = result.getString("Driver_Id");
                    riders = result.getString("riders");
                    Name = result.getString("u.FirstName");
                    phone = result.getString("u.phone");
                    ScheduleView schedule = new ScheduleView(days,timings,DriverID,riders,Name,phone);
                    scheduleItems.add(schedule);
                }



                for (int i = 0; i < scheduleItems.size(); i++) {
                    System.out.println(scheduleItems.get(i));
                }
                Message msg = handler.obtainMessage(1, scheduleItems);
                handler.sendMessage(msg);
                Log.e("Data", days + " " + timings);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };


}
