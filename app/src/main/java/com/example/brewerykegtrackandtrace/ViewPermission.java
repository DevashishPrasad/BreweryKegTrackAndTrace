package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckedTextView;

public class ViewPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_permission);

        // TODO get permissions from database
        CheckedTextView userChecked = (CheckedTextView) findViewById(R.id.user_checked);
        userChecked.setChecked(true);
        CheckedTextView transportChecked = (CheckedTextView) findViewById(R.id.transport_checked);
        transportChecked.setChecked(true);
        CheckedTextView locationChecked = (CheckedTextView) findViewById(R.id.location_checked);
        locationChecked.setChecked(true);
        CheckedTextView kegChecked = (CheckedTextView) findViewById(R.id.keg_checked);
        kegChecked.setChecked(false);
        CheckedTextView reportChecked = (CheckedTextView) findViewById(R.id.report_checked);
        reportChecked.setChecked(false);

    }


}