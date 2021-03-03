package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class User {
    public static String username;
    public static String truckno;
    public static String location;
    public static String loadunload;
    public static String automanual;
    public static String reportpermission;

    public static void setActionbar(Activity activity)
    {
        TextView actionbar_truck = activity.findViewById(R.id.action_bar);
//        Intent intent = getIntent();
//        String truck = intent.getExtras().getString("TRUCK");
        actionbar_truck.setText(truckno);
    }

}
