package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminUserAdd extends AppCompatActivity {

    boolean isAdmin, hasSelectedUser;
    Button adminBtnView,truckBtnView;
    EditText user_fname_ui, user_lname_ui, user_pwd_ui, mobile_ui;
    final String[] department = {"Manufacturing","Dispatch","Sales","Head Office"};
    public String selected_department,truck_no;
    ArrayList<TransportRecyclerListData> transportList;
    ArrayList<String> truck_numbers;
    ArrayList<String> transportName;
    Switch active_ui;
    Button addUserbtn_ui;
    String active,user_type;
    String user_fname, user_lname, user_pwd, mobile;
    String grant_um,grant_lm,grant_km,grant_tm,grant_rm;
    boolean isEditing;
    Spinner departmentSpinner,truckSpinner;
    CheckBox user_p,keg_p,loc_p,report_p,transport_p;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_add);

        // Footer and Actionbar
        User.setActionbar(AdminUserAdd.this);
        User.goHome(AdminUserAdd.this);

        // Init Data Variables
        isAdmin=true;
        hasSelectedUser = false;

        // Init View Elements
        adminBtnView = findViewById(R.id.adminBtn);
        truckBtnView = findViewById(R.id.truckBtn);
        addUserbtn_ui = findViewById(R.id.addUserbtn);
        // initiate a Switch
        active_ui = findViewById(R.id.userStatus);
        // Check current state of a Switch (true or false).

        user_fname_ui = findViewById(R.id.firstNameUser);
        user_lname_ui = findViewById(R.id.lastNameUser);
        user_pwd_ui = findViewById(R.id.passwordUser);
        mobile_ui = findViewById(R.id.PhoneUser);
        user_p = findViewById(R.id.UserPermission);
        keg_p = findViewById(R.id.kegPermission);
        loc_p = findViewById(R.id.locationPermission);
        report_p = findViewById(R.id.reportPermission);
        transport_p = findViewById(R.id.transportPermission);
        truckSpinner = findViewById(R.id.spinnerPart);
        departmentSpinner = findViewById(R.id.department_spinner);

        isEditing=false;
        selected_department = null;
        truck_no = null;

        if (User.isEdit)
        {
            isEditing=true;
            User.isEdit = false;
            fillUiWithUserInfo();
        }

        populateSpinnerWithTruck();
        setDepartmentSpinner();

        // If called by Edit

    }

    public void populateSpinnerWithTruck()
    {
        truck_numbers = new ArrayList<>();

        Map<String,String> param = new HashMap<>();
        StringRequester.getData(AdminUserAdd.this, Constants.TRANSPORTS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            transportList = new ArrayList<>();
                            transportName = new ArrayList<>();
                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                // Check truck is active or not
                                HashMap<String,String> jsonMap;
                                jsonMap = User.jsonToMap(objects);
                                transportList.add(new TransportRecyclerListData(jsonMap));
//                                transportName.add(objects.getString("TRUCK"));
                                truck_numbers.add(objects.getString("TRANS_RN"));

                            }

                            // Populate the UI with Trucks
                            truckSpinner.setAdapter(new ArrayAdapter<>(AdminUserAdd.this, android.R.layout.simple_spinner_dropdown_item,truck_numbers));
                            if (isEditing) {
                                selectTruck();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void selectTruck()
    {
        int index = truck_numbers.indexOf(User.editData.get("TRUCK"));
        Toast.makeText(getApplicationContext(),User.editData.get("TRUCK")+" "+index,Toast.LENGTH_SHORT).show();
        if (index != -1) {
            truckSpinner.setSelection(index);
        }
    }

    // Set Methods
    private void setDepartmentSpinner(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, department);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        departmentSpinner.setAdapter(dataAdapter);

        departmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_department = department[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void createUser(View view) {

        // Get Data from UI
        user_fname = user_fname_ui.getText().toString().trim();
        user_lname = user_lname_ui.getText().toString().trim();
        mobile = mobile_ui.getText().toString().trim();
        active = active_ui.isChecked() ? "1" : "0";
        grant_um = user_p.isChecked() ? "1" : "0";
        grant_km = keg_p.isChecked() ? "1" : "0";
        grant_lm = loc_p.isChecked() ? "1" : "0";
        grant_tm = transport_p.isChecked() ? "1" : "0";
        grant_rm = report_p.isChecked() ? "1" : "0";
        String temp_pwd = user_pwd_ui.getText().toString().trim();

        // Get the selected truck no.
        if(truckSpinner != null && truckSpinner.getSelectedItem() !=null) {
            truck_no = truckSpinner.getSelectedItem().toString();
        }

        // Get the Department
        if (temp_pwd.equals("") && !isEditing) {
                Toast.makeText(this, "Please Enter All Information", Toast.LENGTH_SHORT).show();
                return;
        }

        if (truck_no == null)
            truck_no = "null";

        // Check
        if(user_fname.equals("") || user_lname.equals("")  || mobile.equals("") || !hasSelectedUser || selected_department == null ) {
            Toast.makeText(this, "Please Enter All Information", Toast.LENGTH_SHORT).show();
        }
        else {
            if(mobile_ui.length() != 10) {
                Toast.makeText(this, "Please enter valid Phone no.", Toast.LENGTH_SHORT).show();
                return;
            }

            user_type = isAdmin ? "ADMIN" : "USER";

            if (isEditing && !temp_pwd.equals(""))
                user_pwd = User.md5(temp_pwd);
            if(!isEditing)
                user_pwd = User.md5(temp_pwd);

            // Confirmation Dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            registerOrEditUser();
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
                message = "Do you want to edit this User?";
            else
                message = "Do you want to add this User?";

            AlertDialog alert = builder.create();
            alert.setTitle("Are you sure?");
            alert.setMessage(message);
            alert.show();
        }
    }

    private void registerOrEditUser() {
        Map<String,String> param = new HashMap<>();

        param.put("mobile",mobile);
        param.put("user_pwd",user_pwd);
        param.put("user_fname",user_fname);
        param.put("user_lname",user_lname);
        param.put("user_type",user_type);
        param.put("active",active);
        param.put("grant_um",grant_um);
        param.put("grant_lm",grant_lm);
        param.put("grant_km",grant_km);
        param.put("grant_tm",grant_tm);
        param.put("grant_rm",grant_rm);
        param.put("dept",selected_department);
        param.put("truck",truck_no);


        String URL = isEditing ? Constants.USER_EDIT_URL : Constants.USER_REGISTER_URL ;
        StringRequester.getData(AdminUserAdd.this,URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) throws JSONException {
                            if (!jsonResponse.getBoolean("error"))
                                Toast.makeText(getApplicationContext(),"User "+ (isEditing ? "Edited" : "Created") + " Successfully",Toast.LENGTH_SHORT).show();
                            else // Show error message
                                Toast.makeText(getApplicationContext(),jsonResponse.getString("message"),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // UTILS

    public void adminBtn(View view) {
        isAdmin = true;
        hasSelectedUser = true;
        setAdminBtnActive();
    }

    public void truckBtn(View view) {
        isAdmin = false;
        hasSelectedUser = true;
        setTruckBtnActive();
    }

    public void fillUiWithUserInfo() {
        addUserbtn_ui.setText("Update User");
        mobile_ui.setText(User.editData.get("MOBILE"));
        mobile_ui.setEnabled(false);
        user_fname_ui.setText(User.editData.get("USER_FNAME"));
        user_lname_ui.setText(User.editData.get("USER_LNAME"));
        user_pwd = User.editData.get("USER_PWD");
        isAdmin = User.editData.get("USER_TYPE").equals("ADMIN");
        hasSelectedUser = true;
        if(isAdmin)
            setAdminBtnActive();
        else
            setTruckBtnActive();

        user_p.setChecked(User.editData.get("GRANT_UM").equals("1"));
        keg_p.setChecked(User.editData.get("GRANT_KM").equals("1"));
        loc_p.setChecked(User.editData.get("GRANT_LM").equals("1"));
        report_p.setChecked(User.editData.get("GRANT_RM").equals("1"));
        transport_p.setChecked(User.editData.get("GRANT_TM").equals("1"));
        active_ui.setChecked(User.editData.get("ACTIVE").equals("1"));

        departmentSpinner.setSelection(Arrays.asList(department).indexOf(User.editData.get("DEPT")));


    }

    public void setAdminBtnActive() {
        adminBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        adminBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        truckBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        truckBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void setTruckBtnActive() {
        truckBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        truckBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        adminBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        adminBtnView.setTextColor(Color.parseColor("#636363"));
    }
}