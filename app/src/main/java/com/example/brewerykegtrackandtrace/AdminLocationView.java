package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO cannot delete location number 1, only edit

public class AdminLocationView extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_view);

        User.setActionbar(AdminLocationView.this);
        User.goHome(this);
        recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoactionDataFromDB();
    }

    private void getLoactionDataFromDB()
    {
        Map<String,String> param = new HashMap<>();
        StringRequester.getData(AdminLocationView.this, Constants.LOCATIONS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<LocationRecyclerListData> locationList = new ArrayList<>();

                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                locationList.add(new LocationRecyclerListData(User.jsonToMap(objects)));
                            }

                            // Populate the UI with Assets
                            LocationRecyclerAdapter adapter = new LocationRecyclerAdapter(locationList);
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



    public void toLocationAdd(View view) {
        Intent intent = new Intent(AdminLocationView.this,AdminLocationAdd.class);
        startActivity(intent);
    }

}