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

    public void selectAndGo(View view){
        Intent intent = new Intent(LoadUnload.this,TagScan.class);
        startActivity(intent);
    }
}