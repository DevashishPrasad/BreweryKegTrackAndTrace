package com.example.brewerykegtrackandtrace;

public class LocationRecyclerListData {
    private String location_name;
    private String latitude;
    private String longitude;
    private int delete;
    private int update;

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

    public void deleteLocation(String latitude, String longitude) {
        //TODO delete user in database
    }

    public void updateLocation(String latitude, String longitude) {
        //TODO delete user in database
    }

}