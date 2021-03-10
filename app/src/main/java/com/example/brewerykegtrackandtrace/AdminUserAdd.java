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

    boolean isAdmin, hasSelectedUser;
    Button adminBtnView,truckBtnView;
    EditText fn,ls,ps,phone;
    Switch simpleSwitch;
    Boolean isActive;
    String first_name_Str, last_name_Str, password_Str, phone_Str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_add);

        // Init Data Variables
        isAdmin=true;
        hasSelectedUser = false;

        // Init View Elements
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

        // Footer and Actionbar
        User.setActionbar(AdminUserAdd.this);
        User.goHome(AdminUserAdd.this);

    }


    public void adminBtn(View view) {
        isAdmin = true;
        hasSelectedUser = true;
        adminBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        adminBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        truckBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        truckBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void truckBtn(View view) {
        isAdmin = false;
        hasSelectedUser = true;
        truckBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        truckBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        adminBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        adminBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void createUser(View view) {
        first_name_Str = fn.getText().toString();
        last_name_Str = ls.getText().toString();
        password_Str = ps.getText().toString();
        phone_Str = phone.getText().toString();

        if(first_name_Str.equals("") || last_name_Str.equals("")  || password_Str.equals("")  || phone_Str.equals("") || !hasSelectedUser)
            Toast.makeText(this,"Please, Fill all information",Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, (isAdmin ? "Admin" : "Truck") + " User Created", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}