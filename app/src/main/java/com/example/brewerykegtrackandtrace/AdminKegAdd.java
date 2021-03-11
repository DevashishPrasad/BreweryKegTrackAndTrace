package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AdminKegAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_keg_add);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.keg_spinner);

        String[] entries = {"30 Liters", "50 Liters", "Empty", "CO2", "Dispenser"};

        //Creating the ArrayAdapter instance having the list of entries
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, entries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        User.setActionbar(AdminKegAdd.this);
        User.goHome(AdminKegAdd.this);
    }

    public void createKeg(View view) {
        String readID = ((TextView) findViewById(R.id.readKegID)).getText().toString();
        String writeID = ((TextView) findViewById(R.id.writeKegID)).getText().toString();
        String rescanID = ((TextView) findViewById(R.id.rescannedKegID)).getText().toString();
        boolean activeStatus = ((Switch) findViewById(R.id.kegSwitch)).isChecked();
        String spinner = ((Spinner) findViewById(R.id.keg_spinner)).getSelectedItem().toString();

        Toast.makeText(this,readID+" "+writeID+" "+rescanID+" "+activeStatus+" "+spinner,Toast.LENGTH_LONG).show();
    }
}