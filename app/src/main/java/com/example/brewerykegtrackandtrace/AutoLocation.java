package com.example.brewerykegtrackandtrace;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

class Place{
    public String name;
    public Location location;
    Place(String name,Location loc)
    {
        this.name = name;
        this.location = loc;
    }
}

public class AutoLocation extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mlocationCallback;
    private LocationSettingsRequest.Builder builder;
    private static final int REQUEST_CHECK_SETTINGS = 102;
    String result;
    public static final int THRESHOLD = 100;
    double LATITUDE;
    double LONGITUDE;
    ProgressDialog progressDialog;
    boolean GotLocation;
    Place place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_location);
        Log.d("DEBUG_US", " ENTER LIVE LOC");
        progressDialog = new ProgressDialog(this);
        Location auth_location = new Location("point A");

        double LATITUDE = 21.0915219;
        double LONGITUDE = 79.1253902;
        progressDialog.setTitle("Getting Location...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        auth_location.setLatitude(LATITUDE);
        auth_location.setLongitude(LONGITUDE);
        GotLocation = false;
        setContentView(R.layout.activity_main);

        requestLocationPermission();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        mlocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    if (!GotLocation) {
                        place = getPlaceByLocation(location);
                        progressDialog.dismiss();

                        if (place == null) {
                            // Handle Place not found
                            // todo bahar fekna
                        }
                        else {
                            // todo get data from DB
                            GotLocation = true;
                            Intent i = new Intent(AutoLocation.this, LoadUnload.class);
                            i.putExtra("place", place.name);
                            startActivity(i);
                        }
                    }

                    else {

                        // Get Current Distance
//                        float distance = place.location.distanceTo(location);
                        float distance = location.distanceTo(location);

                        result = "USER Location is \n" +
                                location.getLatitude() +
                                ", " +
                                location.getLongitude() +

                                "\n is in " + distance + " meters from Target";
                        Log.d("USER_LOCATION: ", result);


                        if (distance > THRESHOLD) {
//                        Logout Code
                            stopLocationUpdates();
                            Log.e("LOC_THRESHOLD_CROSS: ", result);

                            Intent intent = new Intent(AutoLocation.this, Login.class);
                            startActivity(intent);
                        }

                    }
//                    Toast.makeText(getApplicationContext(),location.toString(),Toast.LENGTH_SHORT).show();
                }
            };
        };

        mLocationRequest = createLocationRequest();
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        checkLocationSetting(builder);


    }
//
    private Place getPlaceByLocation(Location loc)
    {
        Location auth_location = new Location("point A");
        double LATITUDE =  21.0915219;
        double LONGITUDE = 79.1253902;
        auth_location.setLatitude(LATITUDE);
        auth_location.setLongitude(LONGITUDE);

        Place Dummy = new Place("Harry's Pub",auth_location);
        return Dummy;
    }

    private void fetchLastLocation() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
//                    Toast.makeText(MainActivity.this, "Permission not granted, Kindly allow permission", Toast.LENGTH_LONG).show();
                requestLocationPermission();
                return;
            }
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("LAST LOCATION: ", location.toString()); // You will get your last location here
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    // permission was denied, show alert to explain permission
                    showPermissionAlert();
                }else{
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fetchLastLocation();
                    }
                }
            }
        }
    }

    private void showPermissionAlert(){
        if (ActivityCompat.checkSelfPermission(AutoLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(AutoLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AutoLocation.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }


    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void checkLocationSetting(LocationSettingsRequest.Builder builder) {

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                startLocationUpdates();
                return;
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull final Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                    builder1.setTitle("Continious Location Request");
                    builder1.setMessage("This request is essential to get location update continiously");
                    builder1.create();
                    builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            try {
                                resolvable.startResolutionForResult(AutoLocation.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                    builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Location update permission not granted", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder1.show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                startLocationUpdates();
            } else {
                checkLocationSetting(builder);
            }
        }
    }
    public void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        fusedLocationClient.requestLocationUpdates(mLocationRequest,
                mlocationCallback,
                null /* Looper */);
    }



    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(mlocationCallback);
    }

    @AfterPermissionGranted(1)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", 1, perms);
        }
    }

}