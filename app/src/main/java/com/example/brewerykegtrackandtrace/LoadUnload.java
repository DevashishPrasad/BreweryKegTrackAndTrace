package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoadUnload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_unload);
    }

    public void loadAndGo(View view){
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "load";
        startActivity(intent);
    }

    public void unloadAndGo(View view){
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        User user = new User();
        user.loadunload = "unload";
        startActivity(intent);
    }
}