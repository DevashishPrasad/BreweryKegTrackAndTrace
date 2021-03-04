package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LocationAutoManual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_driver);
    }

    public void manualAndGo(View view){
        Intent intent = new Intent(LocationAutoManual.this,LoadUnload.class);
        User user = new User();
        user.automanual = "manual";
        startActivity(intent);
    }

    public void autoAndGo(View view){
        Intent intent = new Intent(LocationAutoManual.this,AutoLocation.class);
        User user = new User();
        user.automanual = "auto";
        startActivity(intent);
    }

    public void goToReport(View view){
        // add report class here
        Intent intent = new Intent(LocationAutoManual.this,LoadUnload.class);
        startActivity(intent);
    }
}