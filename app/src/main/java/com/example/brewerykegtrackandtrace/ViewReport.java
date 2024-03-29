package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ViewReport extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText fromDate, toDate;
    SimpleDateFormat s_fromDate,s_todate;
    Spinner objects_spinner,Spinner_Location;
    final String[] objects = {"Keg 30 Ltrs","Keg 50 Ltrs","CO2","Dispenser","All"};
    final String[] db_objects = {"k30","k50","CO2","Dispenser","All"};
    public String selected_object = "none";
    public int selected_location = -1;
    ArrayList<String> places_locations;
    ArrayList<Place> locations;
    private File filePath;
    String cust_file_name;
    HashMap<String,String> param = new HashMap<>();

    // Life cycle methods
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        User.setActionbar(ViewReport.this);
        User.goHome(ViewReport.this);
        requestFilePermission();
        selected_object = null;
        objects_spinner = (Spinner) findViewById(R.id.object_spinner_SP);

        fromDate = (EditText) findViewById(R.id.fromDateET);
        toDate = (EditText) findViewById(R.id.toDateET);
        setDatePicker();
        setAssettype();
        setLocation();
    }

    // Set Methods
    private void setAssettype(){
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, objects);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        objects_spinner.setAdapter(dataAdapter);

        objects_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_object = db_objects[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDatePicker() {
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
    }

    private void setLocation(){
        Spinner_Location = findViewById(R.id.location_spinner_report);
        getLocationsFromDB();
        Spinner_Location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_location = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getApplicationContext(), "Please your truck no.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Get Methods
    public void getLocationsFromDB(){
        places_locations = new ArrayList<>();
        locations = new ArrayList<>();

        Map<String,String> param = new HashMap<>();
        StringRequester.getData(ViewReport.this, Constants.LOCATIONS_LIST_URL, param,
            new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    try {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");
                        int locations_len = jsonArray.length();

                        // Create Array of Assets
                        for (int i=0; i<locations_len; i++) {
                            JSONObject objects = jsonArray.getJSONObject(i);
                            Place temp_place = new Place(User.jsonToMap(objects));
                            locations.add(temp_place);
                            places_locations.add(temp_place.name);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Spinner_Location = findViewById(R.id.location_spinner_report);
                    Spinner_Location.setVisibility(View.VISIBLE);
                    Spinner_Location.setAdapter(new ArrayAdapter<>(ViewReport.this, android.R.layout.simple_spinner_dropdown_item, places_locations));
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void getReportDataFromDB(boolean isExport){
        cust_file_name = "";

        param.put("start_date",fromDate.getText().toString());
        cust_file_name += fromDate.getText().toString();
        param.put("end_date",toDate.getText().toString());
        cust_file_name += "_"+toDate.getText().toString();

        if (toDate.getText().toString().trim().equals("") || fromDate.getText().toString().trim().equals("")){
            Toast.makeText(ViewReport.this, "Please select start date and end date", Toast.LENGTH_SHORT).show();
            return;
        }

        if(selected_object != "none") {
            param.put("keg_type", selected_object);
            cust_file_name += "_"+selected_object;
        }
        else {
            Toast.makeText(this,"Please Enter Keg Type",Toast.LENGTH_LONG).show();
            return;
        }

        if(selected_location != -1) {
            param.put("latitude", String.valueOf(locations.get(selected_location).location.getLatitude()));
            param.put("longitude", String.valueOf(locations.get(selected_location).location.getLongitude()));
            cust_file_name += "_"+String.valueOf(locations.get(selected_location).name);
        }
        else{
            Toast.makeText(this,"Please Enter Location",Toast.LENGTH_LONG).show();
            return;
        }

        StringRequester.getData(ViewReport.this, Constants.REPORT_LIST_URL, param,
            new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    try {
                        JSONArray jsonArray = jsonResponse.getJSONArray("data");

                        if(isExport)
                            writeExcel(jsonArray);
                        else {
                            User.reportJson = jsonArray;
                            User.editData = param;
                            User.editData.put("location",places_locations.get(selected_location));

                            goToViewReport();
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            });
    }

    // Utility methods
    private void updateLabelFromdate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        s_fromDate = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        fromDate.setText(s_fromDate.format(myCalendar.getTime()));
    }

    private void updateLabeltoDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        s_todate = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        toDate.setText(s_todate.format(myCalendar.getTime()));
    }

    public void writeExcel(JSONArray jsonArray) throws JSONException {
        filePath = new File(Environment.getExternalStorageDirectory() + "/"+cust_file_name+".xls");
        // Show an explanation to the user *asynchronously* -- don't block
        // Create workbook
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Report");

        // Init Row
        HSSFRow hssfRow = hssfSheet.createRow(0);
        hssfRow.createCell(0).setCellValue("START DATE");
        hssfRow.createCell(1).setCellValue(param.get("start_date"));

        hssfRow = hssfSheet.createRow(1);
        hssfRow.createCell(0).setCellValue("END DATE");
        hssfRow.createCell(1).setCellValue(param.get("end_date"));

        hssfRow = hssfSheet.createRow(2);
        hssfRow.createCell(0).setCellValue("KEG TYPE");
        hssfRow.createCell(1).setCellValue(param.get("keg_type"));

        hssfRow = hssfSheet.createRow(3);
        hssfRow.createCell(0).setCellValue("LOCATION");
        hssfRow.createCell(1).setCellValue(places_locations.get(selected_location));

        hssfRow = hssfSheet.createRow(4);
        hssfRow.createCell(0).setCellValue("Number of Record");
        int len = jsonArray.length();
        hssfRow.createCell(1).setCellValue(len);

        hssfRow = hssfSheet.createRow(6);

        hssfRow.createCell(0).setCellValue("Record ID");
        hssfRow.createCell(1).setCellValue("Tag serial no.");
        hssfRow.createCell(2).setCellValue("Tag name");
        hssfRow.createCell(3).setCellValue("Days Laying");

        // Write Data

        // Create Array of Assets
        for (int i = 6; i < 6+len; i++) {
            hssfRow = hssfSheet.createRow(i + 1);
            JSONObject objects = jsonArray.getJSONObject(i - 6);
            hssfRow.createCell(0).setCellValue(i - 5);
            hssfRow.createCell(1).setCellValue(objects.getString("t_asset_tag"));
            hssfRow.createCell(2).setCellValue(objects.getString("t_asset_name"));
            hssfRow.createCell(3).setCellValue(objects.getString("days_laying"));
        }

        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage()+"\n"+e.getStackTrace(),Toast.LENGTH_SHORT).show();
        }
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
    }

    // Event based methods
    @AfterPermissionGranted(1)
    public void requestFilePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if(EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the File permission", 1, perms);
        }
    }

    public void exportExcel(View view) {
        getReportDataFromDB(true);
        Toast.makeText(ViewReport.this, "Report saved in Root folder", Toast.LENGTH_SHORT).show();
    }

    public void goToViewReport(View view) {
        getReportDataFromDB(false);
    }
    public void goToViewReport() {
        Intent i = new Intent(ViewReport.this,ReportTextView.class);
        startActivity(i);
    }
}