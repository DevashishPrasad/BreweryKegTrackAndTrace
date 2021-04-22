package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

public class LocationAutoManual extends AppCompatActivity {

    MaterialCardView report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_auto_manual);
        User.setActionbar(this);
        User.onlyLogout(LocationAutoManual.this);
        report = findViewById(R.id.user_report_permission);
        if(!User.grant_rm) {
            report.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.user_report_image);

            int color = Color.parseColor("#808080");
            lineColorCode.setColorFilter(color);
        }

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
        Intent intent = new Intent(LocationAutoManual.this, ViewReport.class);
        startActivity(intent);
    }

    public void goToDashboard(View view) {
        Intent intent = new Intent(LocationAutoManual.this, Dashboard.class);
        startActivity(intent);
    }
}