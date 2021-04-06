package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoadUnload extends AppCompatActivity {
    Spinner spinner;
    ArrayList<Place> locations;
    public int selected_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_unload);
        spinner = findViewById(R.id.manualLocationSpinnner);
        if (User.automanual.equals("manual")) {
            spinner.setVisibility(View.VISIBLE);
            populateSpinnerWithLocationsFromDB();
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selected_location = position;
                    User.place = locations.get(position);

                    if(locations.get(position).row_no == 1)
                        User.isFactory = 1;
                    else
                        User.isFactory = 0;
                    Log.d("Factory", String.valueOf(User.isFactory));
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Please your truck no.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        User.goHome(LoadUnload.this);
    }
    public void populateSpinnerWithLocationsFromDB()
    {
        ArrayList<String> places = new ArrayList<>();
        locations = new ArrayList<>();

        Map<String,String> param = new HashMap<>();
        StringRequester.getData(LoadUnload.this, Constants.LOCATIONS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int locations_len = jsonArray.length();

                            // Create Array of Assets
                            for (int i=0; i<locations_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                // Check location is active or not
                                if (objects.getString("ACTIVE").equals("1")) {
                                    Place temp_place = new Place(User.jsonToMap(objects));
                                    locations.add(temp_place);
                                    places.add(temp_place.name);
                                }
                            }
                            spinner.setAdapter(new ArrayAdapter<>(LoadUnload.this, android.R.layout.simple_spinner_dropdown_item,places));
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

    public void loadAndGo(View view){
        if(User.place.name.equals("Default")) {
            Toast.makeText(this,"Please Select Location!",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "load";
        startActivity(intent);
    }

    public void unloadAndGo(View view){
        if(User.place.name.equals("Default")) {
            Toast.makeText(this,"Please Select Location!",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "unload";
//        if()
//            user.isFactory = 0;
        startActivity(intent);
    }
}