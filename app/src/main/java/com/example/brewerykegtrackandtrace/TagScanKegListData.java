package com.example.brewerykegtrackandtrace;

public class TagScanKegListData {

    private String datetime;
    private String keg_id;
    private String status;

    public TagScanKegListData(String datetime, String keg_id, String status) {
        this.datetime = datetime;
        this.keg_id = keg_id;
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getKeg_id() {
        return keg_id;
    }

    public void setKeg_id(String keg_id) {
        this.keg_id = keg_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
