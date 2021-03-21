package com.example.brewerykegtrackandtrace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AdminUserView extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        // Footer and Actionbar
        User.setActionbar(AdminUserView.this);
        User.goHome(AdminUserView.this);

        // Show all Users on UI
        // TODO Enhancement HCI: Provide Search, or Sort
        getUsersDataFromDB();
        recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
    }
    private void getUsersDataFromDB()
    {
        // Create the Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.USER_LIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        {
                            try {
                                // Get Users data from Response
                                JSONArray jsonResponse = new JSONArray(response);
                                int users_len = jsonResponse.length();
                                UserRecyclerListData[] userListData = new UserRecyclerListData[users_len];

                                // Create Array of Users
                                for (int i = 0; i < users_len; i++) {
                                    JSONObject objects = jsonResponse.getJSONObject(i);
                                    String username = objects.getString("USER_FNAME") + " " + objects.getString("USER_LNAME");
                                    String mobile = objects.getString("MOBILE");
                                    userListData[i] = new UserRecyclerListData(username,mobile);
                                }

                                // Populate the UI with Users
                                UserRecyclerAdapter adapter = new UserRecyclerAdapter(userListData);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(adapter);
                            }
                            catch (JSONException e)
                            {
                                Log.e("JSON_ERROR",e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // FOR DEBUGGING
                        Toast.makeText(getApplicationContext(),"ERROR : " + error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR",error.getMessage());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Put Parameters in Request
                Map<String,String> param = new HashMap<>();

                return param;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    public void gotoAddUser(View view) {
        Intent intent = new Intent(AdminUserView.this,AdminUserAdd.class);
        startActivity(intent);
    }
}