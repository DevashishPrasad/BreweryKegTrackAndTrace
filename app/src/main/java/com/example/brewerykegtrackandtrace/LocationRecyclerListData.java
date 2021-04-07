package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class LocationRecyclerListData {
    private String location_name;
    private String latitude;
    private String longitude;
    public HashMap<String,String> locationData;
    public boolean isFactory;

    public LocationRecyclerListData(HashMap<String, String> locationData) {
        this.locationData = locationData;
        this.location_name = locationData.get("location");
        this.latitude = locationData.get("latitude");
        this.longitude = locationData.get("longitude");
        isFactory = locationData.get("row_no").equals("1");
    }


    public LocationRecyclerListData(String location_name, String latitude, String longitude) {
        this.location_name = location_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
