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
        img.setImageResource(unset);
        ImageView img2= (ImageView) findViewById(R.id.transport_permission_image);
        img2.setImageResource(set);
        ImageView img1= (ImageView) findViewById(R.id.location_permission_image);
        img1.setImageResource(unset);
        ImageView img3= (ImageView) findViewById(R.id.keg_permission_image);
        img3.setImageResource(set);
        ImageView img4= (ImageView) findViewById(R.id.report_permission_image);
        img4.setImageResource(unset);
    }
}