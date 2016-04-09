package com.example.pravasagar.brs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                        String convertedUsername = etUsername.getText().toString();
                        String convertedPassword = etPassword.getText().toString();

                        if(convertedUsername.equals("admin") && convertedPassword.equals("admin") && failedAttempt !=0){
                            Toast.makeText(getApplicationContext(),"Log In Succesfull", Toast.LENGTH_SHORT).show();

                            //New Activity
                            //To Do
                        }
                        else if (!convertedUsername.equals("admin") || !convertedPassword.equals("admin")){
                            if (failedAttempt != 0) {
                                failedAttempt--;
                                Toast.makeText(getApplicationContext(), "Incorrect Username/Password, Please Try Again",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Too Many Failed Attepts", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }
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
