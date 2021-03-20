package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

class Place{
    // Todo modify according to DB Schema
    public String name;
    public Location location;
    Place(String name,Location loc)
    {
        this.name = name;
        this.location = loc;
    }
}

public class User {
    public static String user_fname;
    public static String user_lname;
    public static String truckno;
    public static String phone;
    public static String location = "Default";
    public static boolean user_type; // True for Admin
    public static String loadunload;
    public static String automanual = "manual";
    public static boolean grant_um,grant_lm,grant_km,grant_tm,grant_rm;
    public static ArrayList<TagScanKegListData> k30_list,k50_list,empty_list,CO2_list,disp_list;

    public static void setActionbar(Activity activity)
    {
        TextView actionbar_truck = activity.findViewById(R.id.action_bar);
        TextView usernameActionBar = activity.findViewById(R.id.usernameActionbar);

        if(user_type)
            actionbar_truck.setText("Administrator");
        else
            actionbar_truck.setText(truckno);

        usernameActionBar.setText(user_fname+" "+user_lname);
    }

    private static void clear()
    {
        user_fname = null;
        user_lname = null;
        truckno = null;
        phone= null;
        loadunload = null;
        automanual = null;
        user_type = false;
        location = "Default";
        grant_um = false;
        grant_lm = false;
        grant_km = false;
        grant_tm = false;
        grant_rm = false;

    }

    // TODO Add in Permission activity
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
                Intent it = new Intent(activity, Login.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                clear();
                activity.startActivity(it);
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
                Intent it = new Intent(activity, Login.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                clear();
                activity.startActivity(it);
            }
        });
    }

}
