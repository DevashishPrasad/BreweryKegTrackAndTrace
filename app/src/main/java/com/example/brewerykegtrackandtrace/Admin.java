package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

public class Admin extends AppCompatActivity {

    MaterialCardView user,keg,loc,tran,report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        User.setActionbar(Admin.this);
        User.onlyLogout(Admin.this);

        // Initialize
        user = findViewById(R.id.dashboard_btn_as);
        keg = findViewById(R.id.keg_admin_home);
        loc = findViewById(R.id.location_admin_home);
        tran = findViewById(R.id.transport_admin_home);
        report = findViewById(R.id.report_admin_home);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String gray = "#808080";
        if(!User.grant_um) {
            user.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.user_group);
            int color = Color.parseColor(gray);
            lineColorCode.setColorFilter(color);
        }
        if(!User.grant_km) {
            keg.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.keg_home);
            int color = Color.parseColor(gray);
            lineColorCode.setColorFilter(color);
        }
        if(!User.grant_lm) {
            loc.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.location_home);
            int color = Color.parseColor(gray);
            lineColorCode.setColorFilter(color);
        }
        if(!User.grant_tm) {
            tran.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.trans_home);
            int color = Color.parseColor(gray);
            lineColorCode.setColorFilter(color);
        }
        if(!User.grant_rm) {
            report.setEnabled(false);
            ImageView lineColorCode = (ImageView) findViewById(R.id.report_home);
            int color = Color.parseColor(gray);
            lineColorCode.setColorFilter(color);
        }
    }

    public void goUserview(View view) {
        Intent intent = new Intent(Admin.this, AdminUserView.class);
        startActivity(intent);
    }

    public void goReport(View view) {
        Intent intent = new Intent(Admin.this, ViewReport.class);
        startActivity(intent);
    }

    public void goKeg(View view) {
        Intent intent = new Intent(Admin.this, AdminKegView.class);
        startActivity(intent);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(Admin.this, ViewPermission.class);
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

    public void goDashboard(View view) {
        Intent intent = new Intent(Admin.this, Dashboard.class);
        startActivity(intent);
    }
}