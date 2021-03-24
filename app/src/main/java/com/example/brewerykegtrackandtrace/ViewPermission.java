package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckedTextView;
import android.widget.ImageView;

public class ViewPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_permission);

        // TODO get permissions from database
        int set = R.drawable.ic_baseline_radio_button_unchecked_24;
        int unset = R.drawable.ic_baseline_check_circle_24;

        ImageView img= (ImageView) findViewById(R.id.user_permission_image);
        img.setImageResource(User.grant_um?unset:set);
        ImageView img2= (ImageView) findViewById(R.id.transport_permission_image);
        img2.setImageResource(User.grant_tm?unset:set);
        ImageView img1= (ImageView) findViewById(R.id.location_permission_image);
        img1.setImageResource(User.grant_lm?unset:set);
        ImageView img3= (ImageView) findViewById(R.id.keg_permission_image);
        img3.setImageResource(User.grant_km?unset:set);
        ImageView img4= (ImageView) findViewById(R.id.report_permission_image);
        img4.setImageResource(User.grant_rm?unset:set);
    }
}