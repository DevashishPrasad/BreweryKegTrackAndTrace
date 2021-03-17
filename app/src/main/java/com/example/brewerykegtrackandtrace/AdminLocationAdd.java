 package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AdminLocationAdd extends AppCompatActivity {

    EditText locNameET,locAddET,locPhoneET,latET,lonET;
    Switch IsActive;
    boolean IsActiveBool;
    String locNameETS,locAddETS,locPhoneETS,latETS,lonETS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_add);

        locNameET = findViewById(R.id.locationNameId);
        locAddET = findViewById(R.id.locAddressId);
        locPhoneET = findViewById(R.id.locPhoneID);
        latET = findViewById(R.id.locLatId);
        lonET = findViewById(R.id.locLonID);

        IsActive = findViewById(R.id.activeloCSwID);

    }

    public void AddLocation(View view) {

        // TODO DB
        locNameETS = locNameET.getText().toString();
        locAddETS = locAddET.getText().toString();
        locPhoneETS = locPhoneET.getText().toString();
        latETS = latET.getText().toString();
        lonETS = lonET.getText().toString();
        IsActiveBool  = IsActive.isChecked();

        if(locNameETS.equals("") || locAddETS.equals("")  || locPhoneETS.equals("")  || latETS.equals("") )
            Toast.makeText(this,"Please, Fill all information",Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this,"Location Added", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
}