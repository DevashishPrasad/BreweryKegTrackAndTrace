package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class TransportRecyclerListData {
    public String vehicleno;
    public String vehiclename;
    public HashMap<String,String> tranData;

    public TransportRecyclerListData(HashMap<String, String> tranData) {
        this.tranData = tranData;
        this.vehicleno = tranData.get("TRANS_RN");
        this.vehiclename = tranData.get("TRANS_NAME");
    }

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
