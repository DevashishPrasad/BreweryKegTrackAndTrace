package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        User.setActionbar(Dashboard.this);
        User.goHome(this);
    }

    public void goAvailability(View view) {
        Intent i = new Intent(Dashboard.this,
                DashboardAvailability.class);
        startActivity(i);
    }

    public void goTransport(View view) {
        Intent i = new Intent(Dashboard.this,
                DashboardTransaction.class);
        startActivity(i);
    }
}