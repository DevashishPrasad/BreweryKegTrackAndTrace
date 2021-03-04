package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginAndGo(View view){
        // Get Phone and Password
        String phone = ((EditText)findViewById(R.id.editTextPhone)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();

        // TODO Check the phone and password from database and fetch username
        // TODO Populate this fetched data into static object


        Intent intent = new Intent(Login.this, SelectTruck.class);
        User.phone = phone;
        // TODO user.username = username;
        User.username = "Default Name";
        User.isAdmin = false;
        startActivity(intent);
    }

    // TODO : Tomorrow's Tasks
    //  Devashish : Set the flow properly (session management)
    //              Footer set
    //  Ayan : Time on action bar
    //         NFC Scanning Screen (Table vala)
}