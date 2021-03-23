package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class UserRecyclerListData {
    public String username;
    public String mobileno;
    public HashMap<String,String> userData;


    public UserRecyclerListData(String username, String mobileno) {
        this.username = username;
        this.mobileno = mobileno;
    }

    public UserRecyclerListData(HashMap<String, String> userData) {
        this.userData = userData;
        this.username = userData.get("USER_FNAME") + " "+ userData.get("USER_LNAME");
        this.mobileno = userData.get("MOBILE");
    }


    public String getUsername() {
        return username;
    }

    public String getMobileno() {
        return mobileno;
    }

}
