package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    public static String user_fname;
    public static String user_lname;
    public static String truckno;
    public static String mobile;

    public static Place place = new Place("Default");

    public static boolean user_type; // True for Admin
    public static String loadunload;
    public static String automanual = "manual";
    public static boolean grant_um,grant_lm,grant_km,grant_tm,grant_rm;
    public static ArrayList<TagScanKegListData> k30_list,k50_list,empty_list,CO2_list,disp_list;
    public static boolean isEdit = false;
    public static HashMap<String,String> editData;
    public static int isFactory;

    public static HashMap<String, TransKegType> dataHolder_daily,dataHolder_weekly,dataHolder_Monthly;
    public static JSONArray reportJson;

    public static void setActionbar(Activity activity) {
        TextView actionbar_truck = activity.findViewById(R.id.action_bar);
        TextView usernameActionBar = activity.findViewById(R.id.usernameActionbar);
        if(user_type)
            actionbar_truck.setText("Administrator");
        else
            actionbar_truck.setText(truckno);

        usernameActionBar.setText(user_fname+" "+user_lname);
    }

    // For Encryption
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

    public static String filterGarbage(String text)
    {
        String pattern = "[A-Za-z0-9!@#$%^&*()-_=+;:',./?\\ ]*";
        Pattern p = Pattern.compile( pattern );
        Matcher m = p.matcher( text );

        StringBuilder sb = new StringBuilder();
        while( m.find() )
        {
            sb.append( m.group() );
        }

        return sb.toString();

    }

    public static void clear() {
        user_fname = null;
        user_lname = null;
        truckno = null;
        mobile = null;
        loadunload = null;
        automanual = null;
        user_type = false;
        grant_um = false;
        grant_lm = false;
        grant_km = false;
        grant_tm = false;
        grant_rm = false;
        place = new Place("Default");
    }

    public static void goHome(Activity activity){
        Button button= (Button) activity.findViewById(R.id.home_footer);
        Button LogoutBtn= (Button) activity.findViewById(R.id.LogoutBtn);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(User.user_type) {
//                    activity.finishAffinity();
                    activity.startActivity(new Intent(activity, Admin.class));
                }
                else {
                    activity.startActivity(new Intent(activity, LocationAutoManual.class));
                }
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Confirmation Dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent it = new Intent(activity, Login.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                clear();
                                activity.startActivity(it);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                String message;

                message = "Do you want Logout?";

                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure?");
                alert.setMessage(message);
                alert.show();

            }
        });
    }

    public static void onlyLogout(Activity activity){
        Button button= (Button) activity.findViewById(R.id.home_footer);
        Button LogoutBtn= (Button) activity.findViewById(R.id.LogoutBtn);

        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(User.user_type) {
                    activity.startActivity(new Intent(activity, Admin.class));
                }
                else {
                    activity.startActivity(new Intent(activity, LocationAutoManual.class));
                }
            }
        });

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent it = new Intent(activity, Login.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                clear();
                                activity.startActivity(it);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                String message;

                message = "Do you want Logout?";

                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure?");
                alert.setMessage(message);
                alert.show();
            }
        });
    }

    // UTILS
    public static HashMap<String, String>  jsonToMap(JSONObject jObject) throws JSONException {

        HashMap<String, String> map = new HashMap<String, String>();
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

        return map;

    }

}
