package com.example.pravasagar.brs;

/**
 * Created by shruti on 4/26/2016.
 */
public class ScheduleView {

    private String day;
    private String time;
    private String driverId;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        driverId = driverId;
    }

    public ScheduleView(String day, String time, String driverId) {
        this.day = day;
        this.time = time;
        this.driverId = driverId;
    }

    @Override
    public String toString() {
        return "ScheduleView{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", driverId='" + driverId + '\'' +
                '}';
    }
}
