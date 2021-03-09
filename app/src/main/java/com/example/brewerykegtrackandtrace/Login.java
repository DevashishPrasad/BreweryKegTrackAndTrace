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

        User.phone = phone;
        // TODO user.username = username;
        User.username = "Default Name";

        // Temporary jugaad
        if(password.equals("admin"))
            User.isAdmin = true;
        else
            User.isAdmin = false;

        Intent intent;

        if(User.isAdmin)
            intent = new Intent(Login.this, Admin.class);
        else
            intent = new Intent(Login.this, SelectTruck.class);

        startActivity(intent);
    }

    // TODO : Tomorrow's Tasks
    //  Devashish : Recycler View
    //  Ayan : Complete AddLocationAdd
}