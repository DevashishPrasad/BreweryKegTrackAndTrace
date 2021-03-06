package com.example.brewerykegtrackandtrace;

public class KegRecyclerListData {
    private String kegid;
    private String kegtype;
    private int delete;
    private int update;

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
