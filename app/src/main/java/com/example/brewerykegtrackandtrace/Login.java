package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        Log.d("DEBUG_US", " ENTER LOGIN");
    }

    public void loginAndGo(View view){
        Intent intent = new Intent(Login.this,SelectTruck.class);
        startActivity(intent);
    }

    // TODO :
    //  Devashish : Set the flow properly (session management)
    //              Footer set
    //  Ayan :      Spinner value send to new
    //              Username, Truck Number & Time on action bar
    //  Future :    Actionbar color
}