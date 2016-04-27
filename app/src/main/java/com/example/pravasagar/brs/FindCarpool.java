package com.example.pravasagar.brs;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.MapFragment;


public class FindCarpool extends AppCompatActivity implements OnMapReadyCallback{

    //Create ArrayList for Address
    ArrayList<String> membersAddress = new ArrayList<String>();
    ArrayList<String> membersName = new ArrayList<String>();
    //Get my address from the database
    ArrayList<String> wholeData = new ArrayList<>();
    String usernamePassword;
    String myUsername;
    String myPassword;
    String myAddress;
    String myName;
    TextView myFullName;
    TextView myFullAddress;
    boolean threadRunning1 = false;
    Message msg;
    private GoogleMap myMap;
    private static final float zoom = 10.0f;
    int logIn;
    String test1;
    String test2;

    //Create ArrayList for test since we do not have database
    ArrayList<String> testMembersAddress = new ArrayList<String>();
    ArrayList<String> testMembersName = new ArrayList<String>();
    //Create handloer
    Handler handler1 = new Handler(){
        @Override
        public void handleMessage(Message msg) {


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_carpool);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //get Reference
        myFullName =(TextView) findViewById(R.id.myFullName);
        myFullAddress= (TextView) findViewById(R.id.myFullAddress);

        //get extra data sent
        Bundle sentData = getIntent().getExtras();
        wholeData = sentData.getStringArrayList("wholeData");
        myName = wholeData.get(0);
        myAddress = wholeData.get(1);
        myUsername= wholeData.get(2);
        myPassword= wholeData.get(3);




        //Set name and address
            //myFullName.setText(myName + " " + myUsername);
            //myFullAddress.setText(myAddress + " "+ myPassword);



        //Add data to test Arraylist
        testMembersAddress.add("20 Wheeler St Somerville, MA 02145");
        testMembersAddress.add("40 Wheeler St Somerville, MA 02145");
        testMembersAddress.add("60 Madison St Malden, MA 02148");
        testMembersAddress.add("7 Border St Boston, MA 02128");
        testMembersAddress.add("26 Elm St Somerville, MA 02143");
        testMembersAddress.add("60 College Avenue Somerville, MA 02144");
        testMembersAddress.add("175 Forest St Waltham, MA 02452");

        testMembersName.add("A");
        testMembersName.add("B");
        testMembersName.add("C");
        testMembersName.add("D");
        testMembersName.add("E");
        testMembersName.add("F");
        testMembersName.add("G");

    }
    //Create and start Thread on start
    @Override
    protected void onStart() {
        super.onStart();
        Thread threadMemberAddress = new Thread(rMembersAddress);
        threadRunning1 = true;
        threadMemberAddress.start();
        Thread threadMemberName = new Thread();

    }

    //Create Runnable Object for getting Name

    Runnable rMembersAddress = new Runnable() {
        @Override
        public void run() {

            try{
                while (threadRunning1){
                    for (int i = 0; i<testMembersAddress.size();i++){
                        membersAddress.add(i,testMembersAddress.get(i));
                    }
                    threadRunning1 = false;
                }
            }catch (Exception e){

            }
            finally {
                threadRunning1 = false;
            }

        }
    };

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
        myFullName.setText(myUsername + " " + myPassword);
        myFullAddress.setText(myAddress);


        //Add markers to the map and show only people withing 3 miles


        for (int i =0; i< membersAddress.size();i++){

            LatLng memberLatitudeLongitude = getLocation(getApplicationContext(), membersAddress.get(i));
            double distance = getDistance(myAddressLatLong, memberLatitudeLongitude);

            if(distance <= 3) {
                myMap.addMarker(new MarkerOptions()
                        .position((memberLatitudeLongitude))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                //myFullAddress.setText(String.valueOf(distance));
            }


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

}
