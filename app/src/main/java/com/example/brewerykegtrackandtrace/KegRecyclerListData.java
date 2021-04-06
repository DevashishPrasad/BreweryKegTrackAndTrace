package com.example.brewerykegtrackandtrace;

import java.util.HashMap;

public class KegRecyclerListData {
    private String ass_name;


    private String ass_tag;
    private String kegtype;

    public KegRecyclerListData(HashMap<String, String> assetData) {
        this.assetData = assetData;
        this.ass_name = assetData.get("ASS_NAME");
        this.ass_tag = assetData.get("ASS_TAG");
        this.kegtype = assetData.get("ASS_TYPE");
    }

    public HashMap<String,String> assetData;

    public KegRecyclerListData(String kegid, String kegtype) {
        this.ass_name = kegid;
        this.kegtype = kegtype;
    }

    public String getAss_name() {
        return ass_name;
    }
    public String getKegtype() {
        return kegtype;
    }

    public String getAss_tag() {
        return ass_tag;
    }

    public void setAss_tag(String ass_tag) {
        this.ass_tag = ass_tag;
    }

}
