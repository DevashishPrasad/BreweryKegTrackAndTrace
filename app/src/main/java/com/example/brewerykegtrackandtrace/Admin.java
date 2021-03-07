package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        User.goHome(Admin.this);
    }

    public void goUserview(View view) {
        Intent intent = new Intent(Admin.this, AdminUserView.class);
        startActivity(intent);
    }

    public void goReport(View view) {
        Intent intent = new Intent(Admin.this, TruckReport.class);
        startActivity(intent);
    }

    public void goKeg(View view) {
        Intent intent = new Intent(Admin.this, AdminKegView.class);
        startActivity(intent);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(Admin.this, AdminUserView.class);
        startActivity(intent);
    }

    public void goTransport(View view) {
        Intent intent = new Intent(Admin.this, AdminTransportView.class);
        startActivity(intent);
    }

    public void goLocation(View view) {
        Intent intent = new Intent(Admin.this, AdminLocationView.class);
        startActivity(intent);
    }
}