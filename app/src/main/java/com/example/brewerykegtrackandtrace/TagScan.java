package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TagScan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_scan);

        User user = new User();

        TextView loadunload = (TextView) findViewById(R.id.loadunload);
        if(user.loadunload.equals("load"))
            loadunload.setText("Load");
        else if(user.loadunload.equals("unload"))
            loadunload.setText("Unload");
    }
}