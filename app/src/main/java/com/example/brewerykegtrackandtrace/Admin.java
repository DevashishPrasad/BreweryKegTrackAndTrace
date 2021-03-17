package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Admin extends AppCompatActivity {
    private AlertDialog.Builder builder;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        User.goHome(Admin.this);
        context = this;
        builder = new AlertDialog.Builder(this);
    }


    public void goUserview(View view) {
//        READ MEEE vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
//        TODO : Deva bro, Just uncomment intend after you cut pasted the code
//        READ MEE ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
//        Intent intent = new Intent(Admin.this, AdminUserView.class);
//        startActivity(intent);

        builder.setMessage("IS THIS A GOOD POP UP?") .setTitle("POP UP");

        //Setting message manually and performing action on button click
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Toast.makeText(getApplicationContext(),"you choose yes action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("AlertDialogExample");
        alert.show();
    }




    public void goReport(View view) {
        Intent intent = new Intent(Admin.this, ViewReport.class);
        startActivity(intent);
    }

    public void goKeg(View view) {
        Intent intent = new Intent(Admin.this, AdminKegView.class);
        startActivity(intent);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(Admin.this, AdminUserView.class);
        startActivity(intent);
    }

    public void goTransport(View view) {
        Intent intent = new Intent(Admin.this, AdminTransportView.class);
        startActivity(intent);
    }

    public void goLocation(View view) {
        Intent intent = new Intent(Admin.this, AdminLocationView.class);
        startActivity(intent);
    }
}