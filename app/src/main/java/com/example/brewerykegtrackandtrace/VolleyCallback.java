package com.example.brewerykegtrackandtrace;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException;
    void onFailure(String message);
}
