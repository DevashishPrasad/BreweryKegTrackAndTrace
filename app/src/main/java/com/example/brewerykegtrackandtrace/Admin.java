package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Admin extends AppCompatActivity {

    Button user,keg,loc,tran,report;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        User.setActionbar(Admin.this);
        User.onlyLogout(Admin.this);

        // Initialize
        user = findViewById(R.id.user_admin_home);
        keg = findViewById(R.id.keg_admin_home);
        loc = findViewById(R.id.location_admin_home);
        tran = findViewById(R.id.transport_admin_home);
        report = findViewById(R.id.report_admin_home);

        user.setEnabled(User.grant_um);
        keg.setEnabled(User.grant_km);
        loc.setEnabled(User.grant_lm);
        tran.setEnabled(User.grant_tm);
        report.setEnabled(User.grant_rm);

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