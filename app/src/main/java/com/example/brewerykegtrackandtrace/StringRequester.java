package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StringRequester {

    public static void getData(Activity activity,String URL, Map<String,String> param,final VolleyCallback callback) {
        // This causes error on auto location
        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        {
                            progressDialog.dismiss();

                            try {
                                // Get data from Response
                                JSONObject jsonResponse = new JSONObject(response);

                                callback.onSuccess(jsonResponse);

                            }
                            catch (Exception e)
                            {
//                                Log.e("JSON_ERROR",e.getMessage());
                                e.printStackTrace();
                                callback.onFailure(e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        // FOR DEBUGGING
//                        Log.e("DB_ERROR",error.getMessage());
                        callback.onFailure("Check Your Internet Connection");

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Put Parameters in Request

                return param;
            }
        };
        RequestHandler.getInstance(activity).addToRequestQueue(stringRequest);
    }
    public static void getLocation(Activity activity,String URL, Map<String,String> param,final VolleyCallback callback) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        {

                            try {
                                // Get data from Response
                                JSONObject jsonResponse = new JSONObject(response);
                                callback.onSuccess(jsonResponse);

                            }
                            catch (Exception e)
                            {
                                Log.e("JSON_ERROR",e.getMessage());
                                e.printStackTrace();
                                callback.onFailure(e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // FOR DEBUGGING
                        Toast.makeText(activity.getApplicationContext(),"Check Your Internet Connection" + error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR",error.getMessage());
//                        callback.onFailure(error.getMessage());

                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Put Parameters in Request

                return param;
            }
        };
        RequestHandler.getInstance(activity).addToRequestQueue(stringRequest);
    }

}
