package com.example.pravasagar.brs;

import android.content.Intent;
import android.database.CursorJoiner;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LogIn extends AppCompatActivity {
    Button bLogIn;
    Button bSignUp;
    EditText etUsername;
    EditText etPassword;
    int failedAttempt = 4;
    Message msg;
    int logIn;
    boolean threadRunning = false;
    Connection con = null;
    String convertedUsername;
    String convertedPassword;
    ProgressBar progressBar1;
    ArrayList<String> wholeData = new ArrayList<>();

    //Create Handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String nameAddress= (String) msg.obj;

            //split the name and address
            String splitWholeData [] = nameAddress.split("  ", 2);
            String myName = splitWholeData [0];
            String myAddress = splitWholeData[1];

             if (myName.equals("null")){

                if (failedAttempt != 0) {
                    progressBar1.setVisibility(View.INVISIBLE);
                    failedAttempt--;
                    Toast.makeText(getApplicationContext(), "Incorrect Username/Password, Please Try Again",
                            Toast.LENGTH_SHORT).show();

                }
                else {
                    progressBar1.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Too Many Failed Attepts", Toast.LENGTH_SHORT).show();
                    threadRunning = false;
                    finish();
                }

            }
            else {
                 threadRunning = false;
                 progressBar1.setVisibility(View.INVISIBLE);
                 //Add data to arraylist
                 wholeData.add(myName);
                 wholeData.add(myAddress);
                 wholeData.add(convertedUsername);
                 wholeData.add(convertedPassword);

                 //Start New Activity
                 Intent openDashboard = new Intent(LogIn.this,Dashboard.class);
                 openDashboard.putExtra("wholeData",wholeData);
                 startActivity(openDashboard);
             }
        }
    };

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //set reference
        bLogIn = (Button) findViewById(R.id.bLogIn);
        bSignUp=(Button) findViewById(R.id.bSignUp);
        etUsername =(EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        progressBar1 = (ProgressBar) findViewById(R.id.progress1);
        progressBar1.setVisibility(View.INVISIBLE);


        //Set onclick listener for logIn button

        bLogIn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        progressBar1.setVisibility(View.VISIBLE);
                        convertedUsername = etUsername.getText().toString();
                        convertedPassword = etPassword.getText().toString();

                        //Create and start thread
                        Thread loginThread = new Thread(r);
                        threadRunning = true;
                        loginThread.start();


                    }
                    //Create runnable object
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {

                            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
                            String dbUsername = "asalvatori";
                            String dbPassword = "cs680";
                            String userFullName = null;
                            String userFullAddress = null;
                            Statement stmt = null;

                            try{
                                if (threadRunning){

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
                                        ResultSet result = stmt.executeQuery("Select * from bentleycarpool.user" + " " +
                                                "Where User_Id = " + "'" + convertedUsername + "'");

                                        while (result.next()) {
                                            userFullName = (result.getString("FirstName") + " " + result.getString("LastName"));
                                            userFullAddress = (result.getString("Street") + " " + result.getString("city") +
                                                    result.getString("state"));}

                                            msg = handler.obtainMessage(logIn, userFullName + "  " + userFullAddress);



                                    }catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    //Send Message
                                    handler.sendMessage(msg);

                                }
                            }catch (Exception e){

                            }
                            finally {
                                // end the background thread

                                threadRunning = false;
                                try {
                                    con.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                }
        );



        //Set onclick listener for signup button
        bSignUp.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick (View v){
                        //make new explicit intent
                        Intent signUpPage = new Intent(LogIn.this, SignUp.class);
                        //start intent
                        startActivity(signUpPage);

                    }
                }
        );



    }

}