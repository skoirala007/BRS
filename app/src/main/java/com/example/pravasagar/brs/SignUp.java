package com.example.pravasagar.brs;

import android.os.Bundle;
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

    private String URL = "jdbc:mysql://frodo.bentley.edu:3306/bentleycarpool";
    private String username = "asalvatori";
    private String password = "cs680";
    private String TAG="SignUp: ";
    private Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        uname=(EditText)findViewById(R.id.etUsername);
        email=(EditText)findViewById(R.id.etEmail);
        pwd=(EditText)findViewById(R.id.etPassword);
        cnfPwd=(EditText)findViewById(R.id.etCnfPassword);
        phone=(EditText)findViewById(R.id.etFName);
        street=(EditText)findViewById(R.id.etStreet);
        city=(EditText)findViewById(R.id.etCity);
        zipCode=(EditText)findViewById(R.id.etZip);
        fname=(EditText)findViewById(R.id.etFName);
        lname=(EditText)findViewById(R.id.etLName);

        btnSignup=(Button)findViewById(R.id.btnSignUp);
        btnSignup.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){

                Toast.makeText(getApplicationContext(),"Welcome "+uname.getText().toString(),Toast.LENGTH_SHORT).show();

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
                    con = DriverManager.getConnection(
                            URL,
                            username,
                            password);
                    stmt = con.createStatement();
                    //ResultSet result = stmt.executeQuery("");
                    String query="insert into user(UserId,Pass,LastName,FirstName,Street,Zip,email,city)"
                            +" values('"+uname.getText()+"','"
                            +uname.getText()+"',"
                            +pwd.getText()+"',"
                            +lname.getText()+"',"
                            +fname.getText()+"',"
                            +street.getText()+"',"
                            +zipCode.getText()+"',"
                            +email.getText()+"',"
                            +city.getText()+"'"
                            +")";
                    Toast.makeText(getApplicationContext(),query,Toast.LENGTH_LONG).show();
                    //Log.i(TAG,query);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
