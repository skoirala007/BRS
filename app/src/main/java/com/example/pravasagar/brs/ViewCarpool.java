package com.example.pravasagar.brs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ViewCarpool extends AppCompatActivity {

    private Thread t = null;
    private String days, timings, DriverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_carpool);

        t = new Thread(background);
        t.start();
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
                        "Select s.Schd_Id,s.Route_Id,s.Days,s.Timing,r.Driver_Id " +
                                "FROM schedules s,route_details r where r.Schd_Id = s.Schd_Id " +
                                "AND r.Driver_Id = 'shru'; ");
                //for each record in schedule
                while (result.next()) {
                    days = result.getString("Days");
                    timings = result.getString("Timing");
                    DriverID = result.getString("Driver_Id");
                }

                Log.e("Data", days + " " + timings);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    };
}
