package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class UserRecyclerListData {
    public String username;
    public String mobileno;
    public String department;
    public String user_type;



    public HashMap<String,String> userData;


    public UserRecyclerListData(String username, String mobileno) {
        this.username = username;
        this.mobileno = mobileno;
    }

    public UserRecyclerListData(HashMap<String, String> userData) {
        this.userData = userData;
        this.username = userData.get("USER_FNAME") + " "+ userData.get("USER_LNAME");
        this.mobileno = userData.get("MOBILE");
        this.department = userData.get("DEPT");
        this.user_type = userData.get("USER_TYPE");
    }


    public String getUsername() {
        return username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public String getDepartment() {return department;}

    public String getUser_type() {return user_type;}
}
