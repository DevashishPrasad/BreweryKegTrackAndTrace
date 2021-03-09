package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

// TODO Add conversion Excel

public class ViewReport extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText fromDate,toDate;
    SimpleDateFormat s_fromDate,s_todate;
    Spinner objects_spinner,Spinner_Location;
    final String[] objects = {"Keg 30 Ltrs","Keg 50 Ltrs","CO2","Empty","Dispenser"};
    public String selected_object,selected_location;
    private EditText editTextExcel;
    private File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo2.xls");


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        User.setActionbar(ViewReport.this);
        User.goHome(ViewReport.this);
        requestFilePermission();
        selected_object = null;
        selected_location = null;
        objects_spinner = (Spinner) findViewById(R.id.object_spinner_SP);

        fromDate = (EditText) findViewById(R.id.fromDateET);
        toDate = (EditText) findViewById(R.id.toDateET);
        setDatePicker();
        setLocation();
    }

    private void updateLabelFromdate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        s_fromDate = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        fromDate.setText(s_fromDate.format(myCalendar.getTime()));
    }
    private void updateLabeltoDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        s_todate = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        toDate.setText(s_todate.format(myCalendar.getTime()));
    }

    private void setDatePicker()
    {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFromdate();
            }

        };

        DatePickerDialog.OnDateSetListener to_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabeltoDate();
            }

        };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ViewReport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(ViewReport.this, to_date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, objects);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        objects_spinner.setAdapter(dataAdapter);

        objects_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),objects[position],Toast.LENGTH_SHORT).show();
                // TODO (DB integration)
                selected_object = objects[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setLocation()
    {
        Spinner_Location = findViewById(R.id.location_spinner_report);

        Spinner_Location.setVisibility(View.VISIBLE);
        ArrayList<String> places = getLocationsFromDB();
        Spinner_Location.setAdapter(new ArrayAdapter<>(ViewReport.this, android.R.layout.simple_spinner_dropdown_item, places));
        Spinner_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_location = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please your truck no.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public ArrayList<String> getLocationsFromDB()
    {
        ArrayList<String> places = new ArrayList<>();

        // TODO (DB Intergration): Replace with DB method
        places.add("Nagpur Pub");
        places.add("Harrie's Club");
        places.add("Beer Factory Pune");
        places.add("Wine Factory Nagpur");

        return places;
    }

    @AfterPermissionGranted(1)
    public void requestFilePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the File permission", 1, perms);
        }

    }

    public void exportExcel(View view) {
                // Show an explanation to the user *asynchronously* -- don't block
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
            HSSFSheet hssfSheet = hssfWorkbook.createSheet("Customw Sheet");

            HSSFRow hssfRow = hssfSheet.createRow(0);
            HSSFCell hssfCell = hssfRow.createCell(0);
            // Todo CHANGE THIS
            hssfCell.setCellValue("WE ARE LALA MONSTERS");

            try {


                if (!filePath.exists()){
//                    filePath.mkdirs();
                    filePath.createNewFile();
                }


                FileOutputStream fileOutputStream= new FileOutputStream(filePath);
                hssfWorkbook.write(fileOutputStream);

                if (fileOutputStream!=null){
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                Toast.makeText(getApplicationContext(),"DONE LALA",Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage()+"\n"+e.getStackTrace(),Toast.LENGTH_SHORT).show();
            }
            // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.



    }
}