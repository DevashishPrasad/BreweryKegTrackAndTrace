package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class AdminUserAdd extends AppCompatActivity {

    boolean isAdmin;
    Button adminBtnView,truckBtnView;
    EditText fn,ls,ps,phone;
    Switch simpleSwitch;
    Boolean isActive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_add);
        isAdmin=true;

        adminBtnView = findViewById(R.id.adminBtn);
        truckBtnView = findViewById(R.id.truckBtn);

        // initiate a Switch
        simpleSwitch = findViewById(R.id.UserAddView);
        // check current state of a Switch (true or false).
        isActive = simpleSwitch.isChecked();

        fn = findViewById(R.id.firstNameUser);
        ls = findViewById(R.id.lastNameUser);
        ps = findViewById(R.id.passwordUser);
        phone = findViewById(R.id.PhoneUser);

        User.setActionbar(AdminUserAdd.this);
        User.goHome(AdminUserAdd.this);

    }


    public void adminBtn(View view) {
        isAdmin = true;
        adminBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        adminBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        truckBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        truckBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void truckBtn(View view) {
        isAdmin = false;
        truckBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        truckBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        adminBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        adminBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void createUser(View view) {
        Toast.makeText(this,"User Created",Toast.LENGTH_SHORT).show();
        finish();
    }
}