package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminTransportView extends AppCompatActivity {
    private  RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_view);

        User.goHome(AdminTransportView.this);

        recyclerView = findViewById(R.id.transport_recycler_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTransportDataFromDB();

    }

    private void getTransportDataFromDB()
    {
        Map<String,String> param = new HashMap<>();
        StringRequester.getData(AdminTransportView.this, Constants.TRANSPORTS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<TransportRecyclerListData> transportList = new ArrayList<>();

                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                transportList.add(new TransportRecyclerListData(User.jsonToMap(objects)));
                            }

                            // Populate the UI with Assets
                            TransportRecyclerAdapter adapter = new TransportRecyclerAdapter(transportList);
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

    public void goToAddTransport(View view) {
        Intent i = new Intent(this, AdminTransportAdd.class);
        startActivity(i);
    }
}