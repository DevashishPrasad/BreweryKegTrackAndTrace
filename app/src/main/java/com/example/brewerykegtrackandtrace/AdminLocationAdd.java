 package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

 public class AdminLocationAdd extends AppCompatActivity {

    EditText locNameET,locAddET,latET,lonET;
    Switch active_ui;
    boolean IsActiveBool,isEditing;
    Button locationSubmitBtn;
    String locNameETS,locAddETS,latETS,lonETS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_add);

        locNameET = findViewById(R.id.locationNameId);
        locAddET = findViewById(R.id.locAddressId);
        latET = findViewById(R.id.locLatId);
        lonET = findViewById(R.id.locLonID);
        isEditing = false;
        active_ui = findViewById(R.id.activeloCSwID);
        locationSubmitBtn = findViewById(R.id.locationSubmitBtn);
        if (User.isEdit)
        {
            User.isEdit = false;
            isEditing = true;
            fillUiWithLocationInfo();
        }

    }

    public void fillUiWithLocationInfo(){

        locationSubmitBtn.setText("Edit Location");
        locNameET.setText(User.editData.get("location"));
        locAddET.setText(User.editData.get("address"));
        latET.setText(User.editData.get("latitude"));
        lonET.setText(User.editData.get("longitude"));
        active_ui.setChecked(User.editData.get("active").equals("1"));

        latET.setEnabled(false);
        lonET.setEnabled(false);
    }
    public void AddLocation(View view) {

        locNameETS = locNameET.getText().toString();
        locAddETS = locAddET.getText().toString();
        latETS = latET.getText().toString();
        lonETS = lonET.getText().toString();
        IsActiveBool  = active_ui.isChecked();

        if(locNameETS.equals("") || locAddETS.equals("")  || latETS.equals("") || lonETS.equals("") )
            Toast.makeText(this,"Please, Fill all information",Toast.LENGTH_SHORT).show();
        else {

            // Confirmation Dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            registerOrEditLocation();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            String message;
            if(isEditing)
                message = "Do you want to edit this Location?";
            else
                message = "Do you want to add this Location?";

            AlertDialog alert = builder.create();
            alert.setTitle("Are you sure?");
            alert.setMessage(message);
            alert.show();
        }
    }

     private void registerOrEditLocation()
     {
         Map<String,String> param = new HashMap<>();

         param.put("location",locNameETS);
         param.put("address",locAddETS);
         param.put("latitude",latETS);
         param.put("longitude",lonETS);
         param.put("active",IsActiveBool?"1":"0");

         String URL = isEditing ? Constants.LOCATIONS_EDIT_URL : Constants.LOCATIONS_REGISTER_URL ;
         StringRequester.getData(AdminLocationAdd.this,URL, param,
                 new VolleyCallback() {
                     @Override
                     public void onSuccess(JSONObject jsonResponse) throws JSONException {
                         if (!jsonResponse.getBoolean("error"))
                         {
                             Toast.makeText(getApplicationContext(),"User "+ (isEditing ? "Edited" : "Created") + " Successfully",Toast.LENGTH_SHORT).show();
                             finish();
                         }
                         else // Show error message
                             Toast.makeText(getApplicationContext(),jsonResponse.getString("message"),Toast.LENGTH_SHORT).show();
                     }
                     @Override
                     public void onFailure(String message) {
                         Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                     }
                 });
     }
}