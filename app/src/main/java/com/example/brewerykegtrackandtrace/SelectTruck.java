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
import java.util.List;


public class SelectTruck extends AppCompatActivity  {

    Spinner spinner;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_truck);
        spinner = findViewById(R.id.spinnerPart);
        textView = findViewById(R.id.selectTruckTV);

        ArrayList<String> numbers = new ArrayList<>();

        numbers.add("TM-12-2343");
        numbers.add("TM-12-2340");
        numbers.add("TM-12-1233");
        numbers.add("TM-12-2343");

    }

    public void selectAndGo(View view){
        Intent intent = new Intent(SelectTruck.this,LocationAutoManual.class);
        startActivity(intent);
    }

}