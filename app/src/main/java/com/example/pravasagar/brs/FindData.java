package com.example.pravasagar.brs;

/**
 * Created by pravasagar on 4/28/16.
 */
class FindData {
    private String membersName;
    private String membersAddress;
    private String userId;
    private String rideTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public FindData(String membersName, String membersAddress, String userId, String rideTime){
        super();
        this.membersName = membersName;
        this.membersAddress = membersAddress;
        this.userId = userId;
        this.rideTime = rideTime;
    }
    @Override
    public String toString() {
        return membersName + "  " + membersAddress + "  " + userId + "  " + rideTime;
    }


}
