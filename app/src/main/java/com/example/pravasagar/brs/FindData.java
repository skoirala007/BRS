package com.example.pravasagar.brs;

/**
 * Created by pravasagar on 4/28/16.
 */
class FindData {
    private String membersName;
    private String membersAddress;
    private String rideDate;
    private String rideTime;

    public String getRideDate() {
        return rideDate;
    }

    public void setRideDate(String rideDate) {
        this.rideDate = rideDate;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public String getMembersAddress() {
        return membersAddress;
    }

    public void setMembersAddress(String membersAddress) {
        this.membersAddress = membersAddress;
    }

    public String getMembersName() {
        return membersName;
    }

    public void setMembersName(String membersName) {
        this.membersName = membersName;
    }

    public FindData(String membersName, String membersAddress, String rideDate, String rideTime){
        super();
        this.membersName = membersName;
        this.membersAddress = membersAddress;
        this.rideDate = rideDate;
        this.rideTime = rideTime;
    }
    @Override
    public String toString() {
        return membersName + "  " + membersAddress + "  " + rideDate + "  " + rideTime;
    }


}
