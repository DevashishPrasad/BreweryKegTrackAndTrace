package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AdminTransportAdd extends AppCompatActivity {

    boolean isContract;
    Button adminBtnView,truckBtnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_add);

        adminBtnView = findViewById(R.id.adminBtnTrans);
        truckBtnView = findViewById(R.id.truckBtnTrans);

        User.setActionbar(AdminTransportAdd.this);
        User.goHome(AdminTransportAdd.this);
    }

    public void contractBtn(View view) {
        isContract = true;
        adminBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        adminBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        truckBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        truckBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void companyBtn(View view) {
        isContract = false;
        truckBtnView.setBackground(ContextCompat.getDrawable(this, R.drawable.selectedbtn));
        truckBtnView.setTextColor(ContextCompat.getColor(this,R.color.purple_700));
        adminBtnView.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        adminBtnView.setTextColor(Color.parseColor("#636363"));
    }

    public void createTransport(View view) {
        String transVehicleName = ((TextView) findViewById(R.id.TransVehicleName)).getText().toString();
        String transVehicleReg = ((TextView) findViewById(R.id.TransVehicleReg)).getText().toString();
        boolean activeStatus = ((Switch) findViewById(R.id.TransSwitch)).isChecked();

        Toast.makeText(this,transVehicleName+" "+transVehicleReg+" "+isContract+" "+activeStatus+" ",Toast.LENGTH_LONG).show();
    }
}