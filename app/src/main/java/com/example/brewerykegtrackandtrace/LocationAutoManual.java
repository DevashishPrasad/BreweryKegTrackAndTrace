package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LocationAutoManual extends AppCompatActivity {

    TextView actionbar_truck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_auto_manual);
//        setActionbar();
        User.setActionbar(this);

    }

    public void setActionbar()
    {
        actionbar_truck = findViewById(R.id.action_bar);
        Intent intent = getIntent();
        String truck = intent.getExtras().getString("TRUCK");
        actionbar_truck.setText(truck);
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