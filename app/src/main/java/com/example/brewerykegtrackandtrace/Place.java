package com.example.brewerykegtrackandtrace;

import android.location.Location;

import java.util.HashMap;

class Place{
    public String name;
    public String address;
    public Location location;

    public Place(String name) {
        this.name = name;
    }

    Place(String name, String address, Location loc)
    {
        this.name = name;
        this.location = loc;
        this.address = address;
    }

    Place(HashMap<String, String> hm){
        this.name = hm.get("location");
        this.location = new Location("Point A");
        this.address = hm.get("address");
        this.location.setLatitude(Double.valueOf(hm.get("latitude")));
        this.location.setLongitude(Double.valueOf(hm.get("longitude")));
    }
}
