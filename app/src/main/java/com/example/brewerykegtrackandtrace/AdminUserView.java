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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO Delete and edit user
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
        recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUsersDataFromDB();

    }

    private void getUsersDataFromDB()
    {
        Map<String,String> param = new HashMap<>();
        StringRequester.getData(AdminUserView.this, Constants.USER_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<UserRecyclerListData> userListData = new ArrayList<>();

                            // Create Array of Users
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
//                                String username = objects.getString("USER_FNAME") + " " + objects.getString("USER_LNAME");
//                                String mobile = objects.getString("MOBILE");
                                userListData.add(new UserRecyclerListData(User.jsonToMap(objects)));
                            }

                            // Populate the UI with Users
                            UserRecyclerAdapter adapter = new UserRecyclerAdapter(userListData);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(adapter);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void gotoAddUser(View view) {
        Intent intent = new Intent(AdminUserView.this,AdminUserAdd.class);
        startActivity(intent);
    }
}