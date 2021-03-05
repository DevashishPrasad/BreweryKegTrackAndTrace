package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SelectTruck extends AppCompatActivity  {

    Spinner spinner;
    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_truck);
        spinner = findViewById(R.id.spinnerPart);


        ArrayList<String> numbers = getTruckFromDB();
        spinner.setAdapter(new ArrayAdapter<>(SelectTruck.this, android.R.layout.simple_spinner_dropdown_item,numbers));

        intent = new Intent(SelectTruck.this,LocationAutoManual.class);

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
                Toast.makeText(getApplicationContext(),"Please your truck no.",Toast.LENGTH_SHORT).show();

            }
        });

        User.goHome(SelectTruck.this);
    }

    public ArrayList<String> getTruckFromDB()
    {
        ArrayList<String> numbers = new ArrayList<>();

        // TODO (DB Intergration): Replace with DB method
        numbers.add("TM-12-2343");
        numbers.add("KJ-12-2340");
        numbers.add("MH-12-1233");
        numbers.add("GM-12-2343");

        return numbers;
    }

    public void selectAndGo(View view){
        String name = null;
        if(spinner != null && spinner.getSelectedItem() !=null){
            name = spinner.getSelectedItem().toString();
            User user = new User();
            user.truckno = name;
            startActivity(intent);
        } else  {
            Toast.makeText(getApplicationContext(),"Please select the truck no.",Toast.LENGTH_SHORT).show();
        }
    }
}