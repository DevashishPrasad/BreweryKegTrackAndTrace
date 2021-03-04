package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class LoadUnload extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_unload);
        spinner = findViewById(R.id.manualLocationSpinnner);
        if (User.automanual.equals("manual")) {
            spinner.setVisibility(View.VISIBLE);
            ArrayList<String> places = getLocationsFromDB();
            spinner.setAdapter(new ArrayAdapter<>(LoadUnload.this, android.R.layout.simple_spinner_dropdown_item, places));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        Toast.makeText(getApplicationContext(), "Select Truck", Toast.LENGTH_SHORT).show();
                    } else {
                        String sN = parent.getItemAtPosition(position).toString();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getApplicationContext(), "Please your truck no.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public ArrayList<String> getLocationsFromDB()
    {
        ArrayList<String> places = new ArrayList<>();

        // TODO (DB Intergration): Replace with DB method
        places.add("Nagpur Pub");
        places.add("Harrie's Club");
        places.add("Beer Factory Pune");
        places.add("Wine Factory Nagpur");

        return places;
    }

    public void loadAndGo(View view){
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "load";
        startActivity(intent);
    }

    public void unloadAndGo(View view){
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "unload";
        startActivity(intent);
    }
}