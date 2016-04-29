package com.example.pravasagar.brs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SignUp extends AppCompatActivity {
    private EditText uname;
    private EditText email;
    private EditText pwd;
    private EditText cnfPwd;
    private EditText phone;
    private EditText street;
    private EditText city;
    private EditText zipCode;
    private EditText fname;
    private EditText lname;
    private EditText state;
    private Thread insertThread = null;
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            if(msg.what==1){
                Toast.makeText(getApplicationContext(),"Record inserted!",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Please login!",Toast.LENGTH_SHORT).show();
                insertThread.interrupt();
                Intent logInPage = new Intent(SignUp.this, LogIn.class);
                //start intent
                startActivity(logInPage);

            }
        }
    };
    private String TAG="SignUp TAG: ";
    private Button btnSignup;
    private Runnable background = new Runnable() {
        public void run() {
            //Toast.makeText(getApplicationContext(),"Welcome "+uname.getText().toString(),Toast.LENGTH_SHORT).show();
            String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
            String username = "asalvatori";
            String password = "cs680";
            //Check if present

            //Else Insert into DB
            try { //load driver into VM memory
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                Log.e("JDBC", "Did not load driver"+e.getMessage());
            }
            Statement stmt = null;
            Connection con = null;
            try { //create connection to database
                con = DriverManager.getConnection(URL,username,password);
                stmt = con.createStatement();
                //ResultSet result = stmt.executeQuery("");

                String query="insert into user(User_Id,Pass,LastName,FirstName,phone,Street,Zip,email,city,state) values('"
                        +uname.getText()+"','"
                        +pwd.getText()+"','"
                        +lname.getText()+"','"
                        +fname.getText()+"',"
                        +phone.getText()+",'"
                        +street.getText()+"','"
                        +zipCode.getText()+"','"
                        +email.getText()+"','"
                        +city.getText()+"','"
                        +state.getText()+"'"
                        +")";
                int result=stmt.executeUpdate(query);
                Message msg = handler.obtainMessage(1, (Object)result);
                handler.sendMessage(msg);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        uname=(EditText)findViewById(R.id.etUsername);
        email=(EditText)findViewById(R.id.etEmail);
        pwd=(EditText)findViewById(R.id.etPassword);
        cnfPwd=(EditText)findViewById(R.id.etCnfPassword);
        phone=(EditText)findViewById(R.id.etPhone);
        street=(EditText)findViewById(R.id.etStreet);
        city=(EditText)findViewById(R.id.etCity);
        zipCode=(EditText)findViewById(R.id.etZip);
        fname=(EditText)findViewById(R.id.etFName);
        lname=(EditText)findViewById(R.id.etLName);
        state=(EditText)findViewById(R.id.etState);

        btnSignup=(Button)findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                insertThread = new Thread(background);
                insertThread.start();
            }

        });
    }
}
