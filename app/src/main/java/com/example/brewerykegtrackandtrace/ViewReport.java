package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

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

// TODO Add conversion Excel
public class ViewReport extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText fromDate,toDate;
    SimpleDateFormat s_fromDate,s_todate;
    Spinner objects_spinner,Spinner_Location;
    final String[] objects = {"Keg 30 Ltrs","Keg 50 Ltrs","CO2","Dispenser"};
    final String[] db_objects = {"k30","k50","CO2","Dispenser"};
    public String selected_object;
    public int selected_location;
    ArrayList<Place> locations;
    private EditText editTextExcel;
    private File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo2.xls");

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

    private void setLocation()
    {
        Spinner_Location = findViewById(R.id.location_spinner_report);

        Spinner_Location.setVisibility(View.VISIBLE);
        ArrayList<String> places = getLocationsFromDB();
        Spinner_Location.setAdapter(new ArrayAdapter<>(ViewReport.this, android.R.layout.simple_spinner_dropdown_item, places));
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
    public ArrayList<String> getLocationsFromDB()
    {
        ArrayList<String> places = new ArrayList<>();
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
                                places.add(temp_place.name);
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

        return places;
    }

    private void getReportDataFromDB(){
        Map<String,String> param = new HashMap<>();
        StringRequester.getData(ViewReport.this, Constants.ASSETS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<KegRecyclerListData> assetList = new ArrayList<>();

                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                assetList.add(new KegRecyclerListData(User.jsonToMap(objects)));
                            }

                            // Populate the UI with Assets
                            KegRecyclerAdapter adapter = new KegRecyclerAdapter(assetList);

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
        String myFormat = "MM/dd/yy"; //In which you need put here
        s_fromDate = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        fromDate.setText(s_fromDate.format(myCalendar.getTime()));
    }

    private void updateLabeltoDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        s_todate = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        toDate.setText(s_todate.format(myCalendar.getTime()));
    }

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
        // Show an explanation to the user *asynchronously* -- don't block
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Report");

        HSSFRow hssfRow = hssfSheet.createRow(0);
        HSSFCell hssfCell = hssfRow.createCell(0);
        // Todo CHANGE THIS
        hssfCell.setCellValue("WE ARE LALA MONSTERS");

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
}