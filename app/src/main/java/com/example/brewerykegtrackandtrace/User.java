package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.widget.TextView;

public class User {
    public static String username;
    public static String truckno;
    public static String phone;
    public static String location;
    public static String loadunload;
    public static String automanual;
    public static String reportpermission;

    public static void setActionbar(Activity activity)
    {
        // TODO Set live Time
        TextView actionbar_truck = activity.findViewById(R.id.action_bar);
        TextView usernameActionBar = activity.findViewById(R.id.usernameActionbar);
//        Intent intent = getIntent();
//        String truck = intent.getExtras().getString("TRUCK");
        actionbar_truck.setText(truckno);
        usernameActionBar.setText(username);
    }

}
