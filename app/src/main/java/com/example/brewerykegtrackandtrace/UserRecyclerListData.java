package com.example.brewerykegtrackandtrace;

public class UserRecyclerListData {
    private String username;
    private String mobileno;
    private int delete;
    private int update;

    public UserRecyclerListData(String username, String mobileno) {
        this.username = username;
        this.mobileno = mobileno;
    }

    public String getUsername() {
        return username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void deleteUser(String mobileno) {
        //TODO delete user in database
    }

    public void updateUser(String mobileno) {
        //TODO delete user in database
    }

}
