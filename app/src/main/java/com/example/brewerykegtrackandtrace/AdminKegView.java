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

public class AdminKegView extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_keg_view);

        User.goHome(AdminKegView.this);
        KegRecyclerListData[] myListData = new KegRecyclerListData[] {
                new KegRecyclerListData("user name 1", "3457823894"),
                new KegRecyclerListData("user name 2", "8337823828"),
                new KegRecyclerListData("user name 3", "3457826485"),
                new KegRecyclerListData("user name 4", "1434732842"),
                new KegRecyclerListData("user name 5", "2462823863"),
                new KegRecyclerListData("user name 6", "3456925865"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888")
        };

        recyclerView = (RecyclerView) findViewById(R.id.keg_recycler_view);
        getAssetsDataFromDB();
    }


    private void getAssetsDataFromDB()
    {
        Map<String,String> param = new HashMap<>();
        StringRequester.getData(AdminKegView.this, Constants.ASSETS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<KegRecyclerListData> assetList = new ArrayList<>();

                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                assetList.add(new KegRecyclerListData(User.jsonToMap(objects)));
                            }

                            // Populate the UI with Assets
                            KegRecyclerAdapter adapter = new KegRecyclerAdapter(assetList);
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
    public void goToAddKeg(View view) {
        Intent i = new Intent(this,AdminKegAdd.class);
        startActivity(i);
    }
}