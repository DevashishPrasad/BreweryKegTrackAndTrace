package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    public static String username;
    public static String truckno;
    public static String phone;
    public static String location = "Default";
    public static boolean isAdmin;
    public static String loadunload;
    public static String automanual;
    public static String reportpermission;

    public static void setActionbar(Activity activity)
    {
        TextView actionbar_truck = activity.findViewById(R.id.action_bar);
        TextView usernameActionBar = activity.findViewById(R.id.usernameActionbar);

        if(isAdmin)
            actionbar_truck.setText("Administrator");
        else
            actionbar_truck.setText(truckno);

        usernameActionBar.setText(username);
    }

    private static void clear()
    {
        username = null;
        truckno = null;
        phone= null;
        loadunload = null;
        automanual = null;
        reportpermission = null;
        isAdmin = false;
        location = "Default";
    }

    // TODO Add in Permission activity
    public static void goHome(Activity activity){
        Button button= (Button) activity.findViewById(R.id.home_footer);
        Button LogoutBtn= (Button) activity.findViewById(R.id.LogoutBtn);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(User.isAdmin) {
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
                if(User.isAdmin) {
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
