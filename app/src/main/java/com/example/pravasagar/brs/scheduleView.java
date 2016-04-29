package com.example.pravasagar.brs;

/**
 * Created by shruti on 4/26/2016.
 */
class ScheduleView {

    private String day;
    private String time;
    private String driverId;
    private String riders;
    private String phono;

    public String getRiders() {
        return riders;
    }

    public void setRiders(String riders) {
        this.riders = riders;
    }

    public String getPhono() {
        return phono;
    }

    public void setPhono(String phono) {
        this.phono = phono;
    }

    public ScheduleView(String day, String time, String driverId, String riders, String phono) {
        this.day = day;
        this.time = time;
        this.driverId = driverId;
        this.riders = riders;
        this.phono = phono;
    }

    @Override
    public String toString() {
        return "ScheduleView{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", driverId='" + driverId + '\'' +
                ", riders='" + riders + '\'' +
                ", phono='" + phono + '\'' +
                '}';
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
