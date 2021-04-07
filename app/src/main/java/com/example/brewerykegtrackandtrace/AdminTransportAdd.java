package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminTransportAdd extends AppCompatActivity {

    boolean isContract,hasSelected;
    Button contactBtn, companyBtn,submitBtn;
    EditText truck1,truck2,truck3,truck4,trans_name;
    Switch active_ui;
    boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_add);

        User.setActionbar(AdminTransportAdd.this);
        User.goHome(AdminTransportAdd.this);

        contactBtn = findViewById(R.id.contactBtnTrans);
        companyBtn = findViewById(R.id.companyBtnTrans);
        truck1 = findViewById(R.id.numberplate1);
        truck2 = findViewById(R.id.numberplate2);
        truck3 = findViewById(R.id.numberplate3);
        truck4 = findViewById(R.id.numberplate4);
        trans_name = findViewById(R.id.TransVehicleName);
        hasSelected = false;
        submitBtn = findViewById(R.id.TransSubmitButton);

        active_ui = findViewById(R.id.TransSwitch);
        if (User.isEdit)
        {
            User.isEdit = false;
            hasSelected = true;
            isEditing = true;
            fillUiWithTransportInfo();
        }

    }

    public void fillUiWithTransportInfo()
    {
        submitBtn.setText("Update Transport");
        trans_name.setText(User.editData.get("TRANS_NAME"));

        String[] truckNo =  User.editData.get("TRANS_RN").split("-");

        Log.e("truck", truckNo[3]);
        truck1.setText(truckNo[0]);
        truck2.setText(truckNo[1]);
        truck3.setText(truckNo[2]);
        truck4.setText(truckNo[3]);

        truck1.setEnabled(false);
        truck2.setEnabled(false);
        truck3.setEnabled(false);
        truck4.setEnabled(false);

        isContract = User.editData.get("TRANS_TYPE").equals("1");
        if(isContract)
            setContactBtnActive();
        else
            setCompanyBtnActive();


        active_ui.setChecked(User.editData.get("ACTIVE").equals("1"));
    }

    public void contractBtn(View view) {
        isContract = true;
        hasSelected = true;
        setContactBtnActive();
    }

    public void companyBtn(View view) {
        isContract = false;
        hasSelected = true;
        setCompanyBtnActive();
    }

    public String transVehicleName;
    public String transVehicleReg;
    public void createTransport(View view) {
         transVehicleName = trans_name.getText().toString().trim();
         transVehicleReg = truck1.getText().toString()+"-"+truck2.getText().toString()+"-"+truck3.getText().toString()+"-"+truck4.getText().toString();

        // Validation
        if (transVehicleReg.length() != 13) {
            Toast.makeText(this,"Please Enter Truck no. correctly",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!hasSelected) {
            Toast.makeText(this,"Please Select Company or Contract",Toast.LENGTH_SHORT).show();
            return;
        }

        // Confirmation Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        registerOrEditTransport();
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
            message = "Do you want to edit this Transport Vehicle?";
        else
            message = "Do you want to add this Transport Vehicle?";

        AlertDialog alert = builder.create();
        alert.setTitle("Are you sure?");
        alert.setMessage(message);
        alert.show();
    }

    public void registerOrEditTransport()
    {
        boolean activeStatus = active_ui.isChecked();
        Map<String,String> param = new HashMap<>();

        param.put("trans_rn",transVehicleReg);
        param.put("trans_name",transVehicleName);
        param.put("trans_type", isContract ? "1" : "0");
        param.put("active", activeStatus ? "1" : "0");

        String URL = isEditing ? Constants.TRANSPORTS_EDIT_URL : Constants.TRANSPORTS_REGISTER_URL ;

        StringRequester.getData(AdminTransportAdd.this,URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) throws JSONException {
                        if (!jsonResponse.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(),"Transport "+ ( isEditing?"Updated":"Registered") + " Successfully",Toast.LENGTH_SHORT).show();
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


    public void setContactBtnActive()
    {
        contactBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        contactBtn.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        companyBtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        companyBtn.setTextColor(Color.parseColor("#636363"));
    }

    public void setCompanyBtnActive()
    {
        companyBtn.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        companyBtn.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        contactBtn.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        contactBtn.setTextColor(Color.parseColor("#636363"));
    }

}