package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SelectTruck extends AppCompatActivity  {

    Spinner spinner;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_truck);
        spinner = findViewById(R.id.spinnerPart);


        ArrayList<String> numbers = getTruckFromDB();
        spinner.setAdapter(new ArrayAdapter<>(SelectTruck.this, android.R.layout.simple_spinner_dropdown_item,numbers));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    Toast.makeText(getApplicationContext(),"Select Truck",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String sN = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Nothing",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public ArrayList<String> getTruckFromDB()
    {
        ArrayList<String> numbers = new ArrayList<>();

        // TODO (DB Intergration): Replace with DB method
        numbers.add("TM-12-2343");
        numbers.add("KJ-12-2340");
        numbers.add("MH-12-1233");
        numbers.add("GM-12-2343");
        numbers.add("TM-12-2343");
        numbers.add("KJ-12-2340");
        numbers.add("MH-12-1233");
        numbers.add("GM-12-2343");
        return numbers;
    }

    public void selectAndGo(View view){
        Intent intent = new Intent(SelectTruck.this,LocationAutoManual.class);
        startActivity(intent);
    }

}