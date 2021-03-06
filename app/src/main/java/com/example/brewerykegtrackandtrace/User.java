package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.view.View;
import android.widget.Button;
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

    // TODO check user type before sending to home
    public static void goHome(Activity activity){
        Button button= (Button) activity.findViewById(R.id.home_footer);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent(activity,LocationAutoManual.class));
            }
        });
    }

}
