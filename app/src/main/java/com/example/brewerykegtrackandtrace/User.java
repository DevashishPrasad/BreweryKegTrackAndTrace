package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.location.Location;
import android.widget.TextView;

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
    public static String location;
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

//    public static void liveTime()
//    {
//        Thread thread = new Thread() {
//
//            @Override
//            public void run() {
//                try {
//                    while (!thread.isInterrupted()) {
//                        Thread.sleep(1000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // update TextView here!
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                }
//            }
//        };
//
//        thread.start();
//    }

}
