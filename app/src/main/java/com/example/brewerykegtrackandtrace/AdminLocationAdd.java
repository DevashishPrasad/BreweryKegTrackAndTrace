package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AdminLocationAdd extends AppCompatActivity {

    EditText locNameET, locAddET, latET, lonET;
    Switch active_ui;
    Spinner locGrpSPN;
    boolean IsActiveBool, isEditing;
    Button locationSubmitBtn;
    String locNameETS, locAddETS, latETS, lonETS, locGrpSPNS;
    final String[] entries = {"Factory", "City", "Rest Of Town"};
    LocationManager mlocManager;
    Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_add);

        User.setActionbar(AdminLocationAdd.this);
        User.goHome(AdminLocationAdd.this);

        locNameET = findViewById(R.id.locationNameId);
        locAddET = findViewById(R.id.locAddressId);
        latET = findViewById(R.id.locLatId);
        lonET = findViewById(R.id.locLonID);
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        locGrpSPN = (Spinner) findViewById(R.id.loc_spinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, entries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        locGrpSPN.setAdapter(aa);

        isEditing = false;
        active_ui = findViewById(R.id.activeloCSwID);
        locationSubmitBtn = findViewById(R.id.locationSubmitBtn);
        if (User.isEdit) {
            User.isEdit = false;
            isEditing = true;
            findViewById(R.id.btn_get_location).setEnabled(false);
            fillUiWithLocationInfo();
        }
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(this,"Please turn on your GPS", Toast.LENGTH_LONG).show();
        }
        else{
            mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener mlocListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            }
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
        }


    }

    public void fillUiWithLocationInfo() {
        locationSubmitBtn.setText("Edit Location");
        locNameET.setText(User.editData.get("location"));
        locAddET.setText(User.editData.get("address"));
        latET.setText(User.editData.get("latitude"));
        lonET.setText(User.editData.get("longitude"));

        active_ui.setChecked(User.editData.get("active").equals("1"));
        locGrpSPN.setSelection(Arrays.asList(entries).indexOf(User.editData.get("loc_group")));
        latET.setEnabled(false);
        lonET.setEnabled(false);
        int gray = Color.parseColor("#808080");
        latET.setTextColor(gray);
        lonET.setTextColor(gray);

    }

    public void AddLocation(View view) {
        locNameETS = locNameET.getText().toString().trim();
        locAddETS = locAddET.getText().toString().trim();
        latETS = latET.getText().toString().trim();
        lonETS = lonET.getText().toString().trim();
        IsActiveBool = active_ui.isChecked();

        if ( lonETS.equals("") || latETS.equals(""))
        {
            Toast.makeText(this, "Please, Fill all information", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (Float.parseFloat(lonETS) > 100 || Float.parseFloat(latETS) > 100) {
            Toast.makeText(this, "Enter Valid Coordinates", Toast.LENGTH_SHORT).show();
            return;
        }
        if (locNameETS.equals("") || locAddETS.equals("") ||  locGrpSPN.getSelectedItem() == null)
            Toast.makeText(this, "Please, Fill all information", Toast.LENGTH_SHORT).show();
        else {
            locGrpSPNS = locGrpSPN.getSelectedItem().toString().trim();

            // Confirmation Dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            registerOrEditLocation();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            String message;
            if (isEditing)
                message = "Do you want to edit this Location?";
            else
                message = "Do you want to add this Location?";

            AlertDialog alert = builder.create();
            alert.setTitle("Are you sure?");
            alert.setMessage(message);
            alert.show();
        }
    }

    private void registerOrEditLocation() {
        Map<String, String> param = new HashMap<>();

        param.put("location", locNameETS);
        param.put("address", locAddETS);
        param.put("latitude", latETS);
        param.put("longitude", lonETS);
        param.put("loc_group", locGrpSPNS);
        param.put("active", IsActiveBool ? "1" : "0");

        String URL = isEditing ? Constants.LOCATIONS_EDIT_URL : Constants.LOCATIONS_REGISTER_URL;
        StringRequester.getData(AdminLocationAdd.this, URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) throws JSONException {
                        if (!jsonResponse.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), "Location " + (isEditing ? "Edited" : "Created") + " Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else // Show error message
                            Toast.makeText(getApplicationContext(), jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getLocation(View view) {

        // now get the lat/lon from the location and do something with it.

        String local_lat = String.valueOf(location.getLatitude());
        String local_long =  String.valueOf(location.getLongitude());
        latET.setText(local_lat);
        lonET.setText(local_long);

     }

    @AfterPermissionGranted(1)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {

        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location loc) {
//            String message = String.format(
//                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
//                    loc.getLongitude(), loc.getLatitude()
//            );
//            Toast.makeText(LbsGeocodingActivity.this, message, Toast.LENGTH_LONG).show();
            location = loc;
        }
        public void onProviderDisabled(String arg0) {

        }
        public void onProviderEnabled(String provider) {

        }
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
 }