package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SelectTruck extends AppCompatActivity  {

    Spinner spinner;
    TextView textView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_truck);
        spinner = findViewById(R.id.spinnerPart);

        populateSpinnerWithTruck();

        intent = new Intent(SelectTruck.this,LocationAutoManual.class);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    Toast.makeText(getApplicationContext(),"Select Truck",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String sN = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Please your truck no.",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void populateSpinnerWithTruck()
    {
        ArrayList<String> numbers = new ArrayList<>();

        Map<String,String> param = new HashMap<>();
        StringRequester.getData(SelectTruck.this, Constants.TRANSPORTS_LIST_URL, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int users_len = jsonArray.length();
                            ArrayList<TransportRecyclerListData> transportList = new ArrayList<>();

                            // Create Array of Assets
                            for (int i = 0; i < users_len; i++) {
                                JSONObject objects = jsonArray.getJSONObject(i);
                                transportList.add(new TransportRecyclerListData(User.jsonToMap(objects)));
                                numbers.add(objects.getString("TRANS_RN"));
                            }

                            // Populate the UI with Trucks
                            spinner.setAdapter(new ArrayAdapter<>(SelectTruck.this, android.R.layout.simple_spinner_dropdown_item,numbers));
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


    public void selectAndGo(View view){
        String name = null;
        if(spinner != null && spinner.getSelectedItem() !=null){
            name = spinner.getSelectedItem().toString();
            User user = new User();
            user.truckno = name;
            startActivity(intent);
            finish();
        } else  {
            Toast.makeText(getApplicationContext(),"Please select the truck no.",Toast.LENGTH_SHORT).show();
        }
    }
}