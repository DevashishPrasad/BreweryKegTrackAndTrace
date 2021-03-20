package com.example.brewerykegtrackandtrace;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String[] JSONArrayToString(JSONArray jsonArray) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i=0; i < jsonArray.length(); i++) {
            list.add(jsonArray.getString(i));
        }
        String[] stringArray = list.toArray(new String[list.size()]);
        return stringArray;
    }

    public void loginAndGo(View view){
        // Get Phone and Password
        String mobile = ((EditText)findViewById(R.id.editTextPhone)).getText().toString();
        String user_pwd = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();

//        mobile = "7757025466";
//        user_pwd = "admin";

        if(mobile.equals("") || mobile.length() != 10)
        {
            Toast.makeText(this,"Please Enter Valid mobile number",Toast.LENGTH_SHORT).show();
            return;
        }
        if(user_pwd.equals(""))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        String user_pwd_encrypt = md5(user_pwd);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.USER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                if (!jsonResponse.getBoolean("error"))
                                {

                                    JSONObject userFromResponse = jsonResponse.getJSONObject("data");
                                    User.user_fname = userFromResponse.getString("USER_FNAME");
                                    User.user_lname = userFromResponse.getString("USER_LNAME");                                    User.user_fname = userFromResponse.getString("USER_FNAME");

                                    User.user_type =  userFromResponse.getString("USER_LNAME").equals("ADMIN");

                                    User.grant_um =  userFromResponse.getString("GRANT_UM").equals("1");
                                    User.grant_lm =  userFromResponse.getString("GRANT_UM").equals("1");
                                    User.grant_tm =  userFromResponse.getString("GRANT_UM").equals("1");
                                    User.grant_rm =  userFromResponse.getString("GRANT_UM").equals("1");
                                    User.grant_km =  userFromResponse.getString("GRANT_UM").equals("1");


                                    User.user_type =  userFromResponse.getString("USER_LNAME").equals("ADMIN");
                                    // TODO Active status
//                                    String[] userFromResponseString = JSONArrayToString(userFromResponse);
                                    Intent intent;
                                    if(User.user_type)
                                        intent = new Intent(Login.this, Admin.class);
                                    else
                                        intent = new Intent(Login.this, SelectTruck.class);

                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),userFromResponse.getString("USER_FNAME")  +userFromResponse.getString("USER_LNAME") ,Toast.LENGTH_SHORT).show();

                                }
                                else
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
                        Toast.makeText(getApplicationContext(),"ERROR : " + error.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.e("DB_ERROR",error.getMessage());
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("mobile",mobile);
                param.put("user_pwd",user_pwd_encrypt);
                return param;
            }
        };
//        User.phone = mobile;
//        // TODO user.username = username;
//        User.username = "Default Name";
//
//        // Temporary jugaad
//        if(user_pwd.equals("admin"))
//            User.isAdmin = true;
//        else
//            User.isAdmin = false;
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }

    // TODO : Tomorrow's Tasks
    //  Devashish :
    //  Ayan : 2. splash screen on auto location
    //
    //
}