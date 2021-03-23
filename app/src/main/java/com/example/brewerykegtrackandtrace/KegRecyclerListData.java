package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class KegRecyclerListData {
    private String kegid;
    private String kegtype;

    public KegRecyclerListData(HashMap<String, String> assetData) {
        this.assetData = assetData;
        this.kegid = assetData.get("ASS_NAME");
        this.kegtype = assetData.get("ASS_TYPE");
    }

    public HashMap<String,String> assetData;

    public KegRecyclerListData(String kegid, String kegtype) {
        this.kegid = kegid;
        this.kegtype = kegtype;
    }

    public String getKegid() {
        return kegid;
    }

    public void setKegid(String kegid) {
        this.kegid = kegid;
    }

    public String getKegtype() {
        return kegtype;
    }

    public void setKegtype(String kegtype) {
        this.kegtype = kegtype;
    }

    public void deleteKeg(String kegid) {
        //TODO delete user in database
    }

    public void updateKeg(String kegid) {
        //TODO delete user in database
    }
}
