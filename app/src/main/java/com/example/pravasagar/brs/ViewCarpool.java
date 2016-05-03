package com.example.pravasagar.brs;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class ViewCarpool extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final String tag = "Widgets";
    String user = null;
    //Get my address from the database
    ArrayList<String> wholeData = new ArrayList<>();
    String phno = null;
    Statement stmt = null;
    ListView listView;
    Button btn;
    ProgressBar progressBar;
    private Thread t = null;
    private String days, timings, DriverID,riders,Name,phone;
    private List<ScheduleView> scheduleItems = new ArrayList<ScheduleView>();
    private ScheduleListAdapter SchduleItemsAdapter;
    private TextToSpeech speaker;
    //Create Handler object to handle messages placed on queue
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            SchduleItemsAdapter = new ScheduleListAdapter(getBaseContext(), R.layout.scheduleitem, (List<ScheduleView>) msg.obj);
            listView = (ListView) findViewById(R.id.rideList);
            listView.setAdapter(SchduleItemsAdapter);
            t.interrupt();
            progressBar.setVisibility(View.INVISIBLE);
            speak("You have "+scheduleItems.size()+" carpools");
        }
    };
    private ScheduleView carpool;
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
                        " SELECT u.phone, u.FirstName, r.day, r.Route_id, r.timing, r.Driver_Id ,r.riders FROM user u,route_details r WHERE u.User_Id = r.Driver_Id AND r.Driver_Id = '"+user+"'; ");
                //for each record in route_details
                while (result.next()) {
                    days = result.getString("day");
                    timings = result.getString("timing");
                    DriverID = result.getString("Driver_Id");
                    riders = result.getString("Route_id");
                    Name = result.getString("u.FirstName");
                    phone = result.getString("u.phone");
                    ScheduleView schedule = new ScheduleView(days,timings,DriverID,riders,Name,phone);
                    scheduleItems.add(schedule);
                }
                ResultSet result2 = stmt.executeQuery(
                        " SELECT u.phone,u.FirstName,t.day,t.Route_id,t.timing, t.Driver_Id from user u,\n" +
                                "\n" +
                                "(select riders.User_id,r.day, r.Route_id, r.timing, r.Driver_Id from riders, route_details r where riders.Route_Id = r.Route_Id) t\n" +
                                "\n" +
                                "where u.User_Id = t.Driver_Id;");
                //for each record in riders.
                while (result2.next()) {
                    days = result2.getString("day");
                    timings = result2.getString("timing");
                    DriverID = result2.getString("Driver_Id");
                    riders = result2.getString("Route_id");
                    Name = result2.getString("FirstName");
                    phone = result2.getString("phone");
                    ScheduleView schedule = new ScheduleView(days,timings,DriverID,riders,Name,phone);
                    scheduleItems.add(schedule);
                }
                // send the scheduleItems list to the handler
                Message msg = handler.obtainMessage(1, scheduleItems);
                handler.sendMessage(msg);
                result.close();
                result2.close();
                con.close();
                stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_carpool);

        listView = (ListView) findViewById(R.id.rideList);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);



        //Speaker Object intialized
        speaker = new TextToSpeech(this,this);
        speaker.setSpeechRate(0.8f);
        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");
        user= wholeData.get(2);

        //database thread started
        t = new Thread(background);
        t.start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // this is how you get an object from an ArrayList for ListView
                carpool = scheduleItems.get(position);
                registerForContextMenu(view);
            }
        });
    }

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
            callOwner(item);
        } else if (item.getTitle() == "SMS the owner") {
            SMSOwner();
        } else if (item.getTitle() == "Opt out of the group") {
            optOut();
        } else {
            return false;
        }
        return true;
    }

    private void optOut() {

        System.out.println(carpool.getRoute_id());
        int result;
        final AtomicBoolean terminate = new AtomicBoolean(false);
        //the Yes/No message box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Opt out of the group")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        //Yes button clicked, do something

                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                while (!terminate.get()) {
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
                                        int result = stmt.executeUpdate(
                                                "UPDATE route_details SET Riders = Riders+1 where Route_id = '" + carpool.getRoute_id() + "';");

                                            // the notification code
                                        String query="insert into notifications(sender,receiver,notif_Code,notif_Msg,route_Id) values('"
                                                +user
                                                +"','"+carpool.getDriverId()
                                                +"','"+1
                                                +"','"+"Opt out"
                                                +"',"+carpool.getRoute_id()+")";
                                        int result2 = stmt.executeUpdate(
                                                "insert into notifications(sender,receiver,notif_Code,notif_Msg,route_Id) values('"
                                                        +user
                                                        +"','"+carpool.getDriverId()
                                                        +"','"+1
                                                        +"','"+"Opt out"
                                                        +"',"+carpool.getRoute_id()+")");
                                        Log.i("Notify: Opt out: ",query);
                                        stmt.close();
                                        con.close();

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    terminate.set(true);
                                }
                            }
                        };

                        new Thread(r, "Thread_1").start();
                        scheduleItems.remove(carpool);
                        listView.setAdapter(SchduleItemsAdapter);
                    }
                })
                .setNegativeButton("No", null)						//Do nothing on no
                .show();


    }

    // calls the Driver of the selected ride
    private void callOwner(MenuItem item) {

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

   // opens the SMS messenger to send SMS to the driver of the selected ride
    private void SMSOwner() {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"+ carpool.getPhoneno()));
        startActivity(sendIntent);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set preferred language to US english.
            int result = speaker.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Language data is missing or the language is not supported.
                Log.e(tag, "Language is not available.");
            } else {
                // The TTS engine has been successfully initialized
                Log.i(tag, "TTS Initialization successful.");
            }
        } else {
            // Initialization failed.
            Log.e(tag, "Could not initialize TextToSpeech.");
        }
    }

    /**
     * Speaks the specified string
     * @param text - the text to speak
     */
    public void speak(String text){
        speaker.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    // the option menu
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
