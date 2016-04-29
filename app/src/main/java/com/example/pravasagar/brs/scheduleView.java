package com.example.pravasagar.brs;

/**
 * Created by shruti on 4/26/2016.
 */
class ScheduleView {

    private String day;
    private String time;
    private String driverId;
    private String riders;
    private String name;
    private String phoneno;

    public ScheduleView(String day, String time, String driverId, String riders, String name, String phoneno) {
        this.day = day;
        this.time = time;
        this.driverId = driverId;
        this.riders = riders;
        this.name = name;
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    @Override
    public String toString() {
        return "ScheduleView{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", driverId='" + driverId + '\'' +
                ", riders='" + riders + '\'' +
                ", name='" + name + '\'' +
                ", phoneno='" + phoneno + '\'' +
                '}';
    }

    public String getRiders() {
        return riders;
    }

    public void setRiders(String riders) {
        this.riders = riders;
    }


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

}
