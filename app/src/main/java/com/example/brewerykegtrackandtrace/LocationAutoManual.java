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
        intent.putExtra("AutoManual", "manual");
        startActivity(intent);
    }

    public void autoAndGo(View view){
        Intent intent = new Intent(LocationAutoManual.this,LoadUnload.class);
        intent.putExtra("AutoManual", "auto");
        startActivity(intent);
    }
}