package com.example.pravasagar.brs;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    Button see;
    Button make;
    Button find;
    TextView welCome;
    Handler handler;
    ArrayList<String> wholeData = new ArrayList<>();
    String usernamePassword;
    Message msg;
    int mNotificationId = 00;
    private Runnable generateNotification = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            String username = "asalvatori";
            String password = "cs680";
            //Check if present
            Log.i("Notif: ", "Reached!");
            //Else Insert into DB
            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver" + e.getMessage());
            }
            Statement stmt = null;
            Connection con = null;
            try { //create connection to database
                con = DriverManager.getConnection(URL, username, password);
                stmt = con.createStatement();
                ResultSet result = stmt.executeQuery("select * from Notifications where receiver='shru'");
                Log.i("Notif: ","ResultSet ready!");
                while(result.next()){
                    String notifMsg=result.getString("notif_msg");
                    String notifCode=result.getString("notif_Code");
                    String notifSender=result.getString("sender");
                    Log.i("Notif: ",notifMsg+", "+notifSender);

                    //send message
                    msg = handler.obtainMessage(1,notifSender+" wants a carpool ride!");
                    handler.sendMessage(msg);
                }
                //String query="select * from Notifications where receiver='"+username+"'";
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Get Extra value from login
        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");

        //Set reference for the buttons
        find = (Button) findViewById(R.id.find);
        make = (Button) findViewById(R.id.make);
        see = (Button) findViewById(R.id.see);
        welCome = (TextView) findViewById(R.id.welcome);
        welCome.setText("Welcome To Dashboard");
        welCome.startAnimation(AnimationUtils.loadAnimation(Dashboard.this, android.R.anim.slide_in_left));

        Thread notifier = new Thread(generateNotification);
        notifier.start();
        handler=new Handler() {
            @Override
            public void handleMessage(Message msg){
                String messageContent=msg.obj.toString();
                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("BRS Request")
                                .setContentText(messageContent);

// Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
                mNotifyMgr.notify(++mNotificationId, mBuilder.build());
            };
        };
    }

    //Onclick listener for making carpool
    public void seeCarpool(View view) {
        Intent lauchSee = new Intent(Dashboard.this, ViewCarpool.class);
        lauchSee.putExtra("wholeData", wholeData);
        startActivity(lauchSee);


    }

    //Onclick listener for make carpool

    //Onclick listener for find carpool
    public void findCarpool(View view) {
        Intent launchFind = new Intent(Dashboard.this, FindCarpool.class);
        launchFind.putExtra("wholeData", wholeData);
        startActivity(launchFind);

    }

    public void makeCarpool(View view) {
        Intent launchMake = new Intent(Dashboard.this, MakeCarpool.class);
        launchMake.putExtra("wholeData", wholeData);
        startActivity(launchMake);


    }
}
