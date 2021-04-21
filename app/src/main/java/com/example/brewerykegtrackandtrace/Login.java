package com.example.brewerykegtrackandtrace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAndGo(View view){
        // Get Phone and Password
        String mobile = ((EditText)findViewById(R.id.editTextPhone)).getText().toString().trim();
        String user_pwd = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString().trim();

        // Validations
        if(mobile.equals("") || mobile.length() != 10) {
            Toast.makeText(this,"Please Enter Valid mobile number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(user_pwd.equals("")) {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        // Encrypt the password
        String user_pwd_encrypt = User.md5(user_pwd);

        // Authenticate User
        userAuthenticate(mobile,user_pwd_encrypt);
    }

    private void userAuthenticate(String mobile,String user_pwd_encrypt)
    {
        // Create the Request

        ProgressDialog progressDialog;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.USER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            // Get data from Response
                            JSONObject jsonResponse = new JSONObject(response);

                            // If there is no error, that is "user" is Authenticated
                            if (!jsonResponse.getBoolean("error")) {

                                // Getting data into JSON Format
                                JSONObject userFromResponse = jsonResponse.getJSONObject("data");

                                // Check user account is active or not
                                if (userFromResponse.getString("ACTIVE").equals("0")) {
                                    Toast.makeText(getApplicationContext(),"Your account is not active",Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                // Update the User Session
                                User.user_fname = userFromResponse.getString("USER_FNAME");
                                User.user_lname = userFromResponse.getString("USER_LNAME");
                                User.user_fname = userFromResponse.getString("USER_FNAME");
                                User.user_type =  userFromResponse.getString("USER_TYPE").equals("ADMIN");
                                User.grant_um =  userFromResponse.getString("GRANT_UM").equals("1");
                                User.grant_lm =  userFromResponse.getString("GRANT_LM").equals("1");
                                User.grant_tm =  userFromResponse.getString("GRANT_TM").equals("1");
                                User.grant_rm =  userFromResponse.getString("GRANT_RM").equals("1");
                                User.grant_km =  userFromResponse.getString("GRANT_KM").equals("1");
                                User.mobile =  userFromResponse.getString("MOBILE");


                                // Direct to next Activity depending on User type
                                Intent intent;

                                if(User.user_type) {
                                    // USER IS ADMIN
                                    intent = new Intent(Login.this, Admin.class);
                                    startActivity(intent);
                                }
                                    else {
                                    // USER IS TRUCK DRIVER
                                    String truck = userFromResponse.getString("TRUCK");
                                    if (truck.equals("null"))
                                        Toast.makeText(getApplicationContext(),"Sorry, You have not been assigned any Truck",Toast.LENGTH_SHORT).show();
                                    else {
                                        User.truckno = truck;
                                        if(userFromResponse.getString("TRANS_ACTIVE").equals("0")) {
                                            Toast.makeText(getApplicationContext(), "Assigned Truck is not active, Please contact Admin.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            intent = new Intent(Login.this, LocationAutoManual.class);
                                            startActivity(intent);
                                        }
                                    }
                                }
                            }
                            else // Show error message
                                Toast.makeText(getApplicationContext(),jsonResponse.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // FOR DEBUGGING
                        Toast.makeText(getApplicationContext(),"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR",error.getMessage());
                        progressDialog.dismiss();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Put Parameters in Request
                Map<String,String> param = new HashMap<>();
                param.put("mobile",mobile);
                param.put("user_pwd",user_pwd_encrypt);
                return param;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}