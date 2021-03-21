package com.example.brewerykegtrackandtrace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminUserAdd extends AppCompatActivity {

    boolean isAdmin, hasSelectedUser;
    Button adminBtnView,truckBtnView;
    EditText user_fname_ui, user_lname_ui, user_pwd_ui, mobile_ui;
    Switch active_ui;
    String active,user_type;
    String user_fname, user_lname, user_pwd, mobile;
    String grant_um,grant_lm,grant_km,grant_tm,grant_rm;

    CheckBox user_p,keg_p,loc_p,report_p,transport_p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_add);

        // Init Data Variables
        isAdmin=true;
        hasSelectedUser = false;

        // Init View Elements
        adminBtnView = findViewById(R.id.adminBtn);
        truckBtnView = findViewById(R.id.truckBtn);
        // initiate a Switch
        active_ui = findViewById(R.id.userStatus);
        // check current state of a Switch (true or false).

        user_fname_ui = findViewById(R.id.firstNameUser);
        user_lname_ui = findViewById(R.id.lastNameUser);
        user_pwd_ui = findViewById(R.id.passwordUser);
        mobile_ui = findViewById(R.id.PhoneUser);
        user_p = findViewById(R.id.UserPermission);
        keg_p = findViewById(R.id.kegPermission);
        loc_p = findViewById(R.id.locationPermission);
        report_p = findViewById(R.id.reportPermission);
        transport_p = findViewById(R.id.transportPermission);

        // Footer and Actionbar
        User.setActionbar(AdminUserAdd.this);
        User.goHome(AdminUserAdd.this);

    }


    public void adminBtn(View view) {
        isAdmin = true;
        hasSelectedUser = true;
        adminBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        adminBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        truckBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        truckBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void truckBtn(View view) {
        isAdmin = false;
        hasSelectedUser = true;
        truckBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        truckBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        adminBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        adminBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void createUser(View view) {

        // Get Data from UI
        user_fname = user_fname_ui.getText().toString().trim();
        user_lname = user_lname_ui.getText().toString().trim();
        user_pwd = user_pwd_ui.getText().toString().trim();;
        mobile = mobile_ui.getText().toString();
        active = active_ui.isChecked() ? "1" : "0";
        grant_um = user_p.isChecked() ? "1" : "0";
        grant_km = keg_p.isChecked() ? "1" : "0";
        grant_lm = loc_p.isChecked() ? "1" : "0";
        grant_tm = transport_p.isChecked() ? "1" : "0";
        grant_rm = report_p.isChecked() ? "1" : "0";

        // Check
        if(user_fname.equals("") || user_lname.equals("")  || user_pwd.equals("")  || mobile.equals("") || !hasSelectedUser)
        {
            Toast.makeText(this, "Please Enter All Information", Toast.LENGTH_SHORT).show();
        }
        else {
            if(mobile_ui.length() != 10) {
                Toast.makeText(this, "Please enter valid Phone no.", Toast.LENGTH_SHORT).show();
                return;
            }

            user_type = isAdmin ? "ADMIN" : "USER";
            user_pwd = User.md5(user_pwd);
            userAuthenticate();
        }
    }

    private void userAuthenticate()
    {
        // Create the Request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.USER_REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        {
                            try {
                                // Get data from Response
                                JSONObject jsonResponse = new JSONObject(response);

                                // If there is no error, that is "user" is Authenticated
                                if (!jsonResponse.getBoolean("error"))
                                {
                                    Toast.makeText(getApplicationContext(),"User Created Successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else // Show error message
                                    Toast.makeText(getApplicationContext(),jsonResponse.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // FOR DEBUGGING
                        Toast.makeText(getApplicationContext(),"ERROR : " + error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR",error.getMessage());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Put Parameters in Request
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
                return param;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}