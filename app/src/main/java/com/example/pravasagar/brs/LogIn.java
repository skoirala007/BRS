package com.example.pravasagar.brs;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogIn extends AppCompatActivity {
    Button bLogIn;
    Button bSignUp;
    EditText etUsername;
    EditText etPassword;
    int failedAttempt = 4;
    Message msg;
    int logIn;
    boolean threadRunning = false;
    String convertedUsername;
    String convertedPassword;

    //Create Handler
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                threadRunning = false;

                //Start New Activity
                Intent openDashboard = new Intent(LogIn.this,Dashboard.class);
                startActivity(openDashboard);

            }
            else if (msg.what==0){

                    if (failedAttempt != 0) {
                        failedAttempt--;
                        Toast.makeText(getApplicationContext(), "Incorrect Username/Password, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                    Toast.makeText(getApplicationContext(), "Too Many Failed Attepts", Toast.LENGTH_SHORT).show();
                    threadRunning = false;
                    finish();
                }

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



        //Set onclick listener for logIn button: for now we are checking admin admin until we get the database access
        // Also we can stop numerous failed attempts by setting the counter. For Now we are setting it for five times
        bLogIn.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
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
                            try{
                                if (threadRunning){
                                    if(convertedUsername.equals("admin") && convertedPassword.equals("admin")){
                                        logIn = 1;
                                    }
                                    else if (!convertedUsername.equals("admin") || !convertedPassword.equals("admin")){
                                        logIn = 0;
                                    }
                                    //Create Message
                                    msg = handler.obtainMessage(logIn);
                                    handler.sendMessage(msg);

                                }
                            }catch (Exception e){

                            }
                            finally {
                                // end the background thread
                                threadRunning = false;
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