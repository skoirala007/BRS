package com.example.pravasagar.brs;

/**
 * Created by pravasagar on 4/28/16.
 */
class FindData {
    private String membersName;
    private String membersAddress;

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

    public FindData(String membersName, String membersAddress){
        super();
        this.membersName = membersName;
        this.membersAddress = membersAddress;
    }
    @Override
    public String toString() {
        return membersName + "  " + membersAddress;
    }


}
