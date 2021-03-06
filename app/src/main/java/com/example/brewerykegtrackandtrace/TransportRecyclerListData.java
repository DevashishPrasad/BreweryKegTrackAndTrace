package com.example.brewerykegtrackandtrace;

public class TransportRecyclerListData {
    private String vehicleno;
    private String vehiclename;
    private int delete;
    private int update;

    public TransportRecyclerListData(String vehicleno, String vehiclename) {
        this.vehicleno = vehicleno;
        this.vehiclename = vehiclename;
    }

    public String getVehicleno() {
        return vehicleno;
    }

    public void setVehicleno(String vehicleno) {
        this.vehicleno = vehicleno;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public void setVehiclename(String vehiclename) {
        this.vehiclename = vehiclename;
    }

    public void deleteTransport(String vehicleno) {
        //TODO delete user in database
    }

    public void updateTransport(String vehicleno) {
        //TODO delete user in database
    }

}
