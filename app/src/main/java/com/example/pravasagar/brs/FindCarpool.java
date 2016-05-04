package com.example.pravasagar.brs;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;



public class FindCarpool extends AppCompatActivity implements OnMapReadyCallback{

    private static final float zoom = 10.0f;
    //Get my address from the database
    ArrayList<String> wholeData = new ArrayList<>();
    //Create ArrayList for Address
    ArrayList<String> messageData = new ArrayList<String>();
    ArrayList<String> driverData = new ArrayList<String>();
    String driverId;
    Handler handler,requestHandler;
    String myUsername;
    String myPassword;
    String myAddress;
    String myName;
    TextView tvDay;
    boolean threadRunning1 = false;
    Message msg;
    TextView tvShowData;
    Button bSendRequest;
    EditText stateDay;
    MapFragment mapFragment;
    Runnable background = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            Connection con=null;
            Statement stmt = null;
            String dbUsername = "asalvatori";
            String dbPassword = "cs680";
            try{
                Class.forName("com.mysql.jdbc.Driver");

            }catch (ClassNotFoundException e){
                Log.e("JDBC", "Did not load driver");
            }
            try {
                //create connection and statement objects
                con = DriverManager.getConnection(URL, dbUsername, dbPassword);
                stmt = con.createStatement();

                String query="insert into notifications(sender,receiver,notif_Code,notif_Msg,route_Id) values("
                        +"'"+myUsername+"',"
                        +"'"+driverId+"',"
                        +0+","
                        +"'Get RideShare',"
                        +1+")";
                int resultVal=stmt.executeUpdate(query);
                Message msg = requestHandler.obtainMessage(1, resultVal);
                requestHandler.sendMessage(msg);
                stmt.close();
                con.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    };
    Runnable rMembersAddress = new Runnable() {
        @Override
        public void run() {
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            Connection con;
            String dbUsername = "asalvatori";
            String dbPassword = "cs680";
            String userFullName = null;
            String userFullAddress = null;
            String userId = null;
            String userFullEmail = null;
            Statement stmt = null;
            ArrayList<String> sendData = new ArrayList<String>();

            try{
                while (threadRunning1){
                    //load driver to memory
                    try{
                        Class.forName("com.mysql.jdbc.Driver");

                    }catch (ClassNotFoundException e){
                        Log.e("JDBC", "Did not load driver");
                    }
                    try{
                        //create connection and statement objects
                        con = DriverManager.getConnection(URL,dbUsername,dbPassword);
                        stmt = con.createStatement();

                        //Do query
                        ResultSet result = stmt.executeQuery("Select * from user" + " " +
                                //","+ " " + "route_details" +
                                "Where User_Id != " + "'" + myUsername  + "'" +" ");
                                //+"And User_Id = Driver_Id");

                        while (result.next()) {
                            userFullName = (result.getString("FirstName") + " " + result.getString("LastName"));
                            userFullAddress = (result.getString("Street") + " " + result.getString("city") +
                                    result.getString("state"));

                            userId = (result.getString("User_Id"));
                            userFullEmail = (result.getString("email"));//time from route_details


                            FindData findData = new FindData(userFullName, userFullAddress, userId, userFullEmail);

                            sendData.add(findData.toString());

                        }
                        msg = handler.obtainMessage(1,sendData);



                    }catch (SQLException e) {
                        e.printStackTrace();
                    }

                    //Send Message
                    handler.sendMessage(msg);
                }

                threadRunning1 = false;
            }catch (Exception e){

            }
            finally {
                threadRunning1 = false;
            }

        }
    };
    private GoogleMap myMap;

    //Create Runnable Object for getting Name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_carpool);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getView().setVisibility(View.INVISIBLE);


        //get Reference
        tvDay =(TextView) findViewById(R.id.tvDay);
        tvShowData = (TextView) findViewById(R.id.tvShowData);
        bSendRequest = (Button) findViewById(R.id.bsendRequest);
        stateDay = (EditText) findViewById(R.id.stateDay);
        //make the text and button invisible
        tvShowData.setVisibility(View.INVISIBLE);
        bSendRequest.setVisibility(View.INVISIBLE);

        //get extra data sent
        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");
        myName = wholeData.get(0);
        myAddress = wholeData.get(1);
        myUsername= wholeData.get(2);
        myPassword= wholeData.get(3);

        //Create and start Thread on start

        Thread threadMemberAddress = new Thread(rMembersAddress);
        threadRunning1 = true;
        threadMemberAddress.start();
        requestHandler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                Toast.makeText(getApplicationContext(),"Request sent!",Toast.LENGTH_SHORT).show();
            }
        };
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                messageData =(ArrayList<String>) msg.obj;
                threadRunning1 = false;

            }
        };
        bSendRequest.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                                             //Create and start thread
                        Thread sendRequest = new Thread(background);
                        //threadRunning = true;
                        sendRequest.start();
                    }
        });
    }

    public void searchMembers(View view){
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        // mapFragment.getView().setVisibility(View.INVISIBLE);
        mapFragment.getMapAsync(this);

        mapFragment.getView().setVisibility(View.VISIBLE);
        tvShowData.setVisibility(View.VISIBLE);
        bSendRequest.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        threadRunning1 = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap=googleMap;
        setUpMap();

    }

    //Method to add marker, title, snippet, and icon

    public void setUpMap() {

        //Setup map as normal google map
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //get latlong for my address
        LatLng myAddressLatLong = getLocation(getApplicationContext(),myAddress);
        //Center the map and adjust the zoom level in regards to my address
        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myAddressLatLong, zoom));


        //Add markers to the map and show only people withing 3 miles
        for (int i =0; i< messageData.size();i++){
            String singleData = messageData.get(i);
            String [ ]separatedData = singleData.split("  ");
            String singleName = separatedData [0];
            String singleAddress = separatedData[1];
            String singleUserId = separatedData[2];
            String singleEmail = separatedData [3];
            LatLng memberLatitudeLongitude = getLocation(getApplicationContext(), singleAddress);
            double distance = getDistance(myAddressLatLong, memberLatitudeLongitude);
            //So only drivers who resides less than 3 miles apart
            if(distance <= 3 ) {
                myMap.addMarker(new MarkerOptions()
                        .position((memberLatitudeLongitude))
                        .title("Name: "+ singleName +"\n"+ "User Id: "+ singleUserId + "\n" +"Email: " + singleEmail)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                driverData.add(singleUserId);
            }
        }
        //Setup Onclick listener to the marker;
        try {
            myMap.setOnMarkerClickListener(
                    new GoogleMap.OnMarkerClickListener() {

                        public boolean onMarkerClick(Marker m) {
                            String title = m.getTitle();
                            driverId="";
                            for(int i=0;i<driverData.size();i++){
                                if(title.contains(driverData.get(i))){
                                    driverId=driverData.get(i);
                                }
                            }
                            Log.i("SendRequest: ",driverId);
                            tvShowData.setText(title);
                            return true;
                        }
                    }
            );

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }

    //Method to get latitude and longitude

    public LatLng getLocation (Context context, String streetAddress){
        LatLng latiLong = null;
        Geocoder coder = new Geocoder(context);
        List<Address>address;
        try {
            address = coder.getFromLocationName(streetAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            latiLong = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return latiLong;

    }

    //Method to calculate distance between two points

    public double getDistance (LatLng first, LatLng second){
        double distance =0;

        Location locationA = new Location("A");
        locationA.setLatitude(first.latitude);
        locationA.setLongitude(first.longitude);

        Location locationB = new Location("B");
        locationB.setLatitude(second.latitude);
        locationB.setLongitude(second.longitude);

        //calculate distance
        distance = (locationA.distanceTo(locationB))/1609.344;

        return distance;
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

