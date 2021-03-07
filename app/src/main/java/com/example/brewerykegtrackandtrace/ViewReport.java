package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

// TODO Add conversion Excel

public class ViewReport extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText fromDate,toDate;
    SimpleDateFormat s_fromDate,s_todate;
    Spinner objects_spinner,Spinner_Location;
    final String[] objects = {"Keg 30 Ltrs","Keg 50 Ltrs","CO2","Empty","Dispenser"};
    public String selected_object,selected_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        User.setActionbar(ViewReport.this);
        User.goHome(ViewReport.this);
        selected_object = null;
        selected_location = null;

        objects_spinner = (Spinner) findViewById(R.id.object_spinner_SP);




        fromDate = (EditText) findViewById(R.id.fromDateET);
        toDate = (EditText) findViewById(R.id.toDateET);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabeltoDate();
            }

        };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ViewReport.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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

}