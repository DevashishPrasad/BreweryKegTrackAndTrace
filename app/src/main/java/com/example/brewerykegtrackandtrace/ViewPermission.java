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
        ImageView img= (ImageView) findViewById(R.id.user_permission_image);
        img.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        ImageView img2= (ImageView) findViewById(R.id.transport_permission_image);
        img2.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
        ImageView img1= (ImageView) findViewById(R.id.location_permission_image);
        img1.setImageResource(R.drawable.ic_baseline_check_circle_24);
        ImageView img3= (ImageView) findViewById(R.id.keg_permission_image);
        img3.setImageResource(R.drawable.ic_baseline_check_circle_24);
        ImageView img4= (ImageView) findViewById(R.id.report_permission_image);
        img4.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24);
    }
}