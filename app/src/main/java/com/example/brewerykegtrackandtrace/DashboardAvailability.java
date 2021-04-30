package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class kegTypes
{
    public int k30=0,k50=0,co2=0,disp=0;
    String location_type;
};


public class DashboardAvailability extends AppCompatActivity {
    LinearLayout location_name_dashboard_LL,k30List,k50List,CO2List,dispenserList, loc_type_LL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_availability);
        User.setActionbar(DashboardAvailability.this);
        User.goHome(this);

        location_name_dashboard_LL = findViewById(R.id.location_name_dashboard_tv);
        k30List = findViewById(R.id.k30List);
        k50List = findViewById(R.id.k50List);
        CO2List = findViewById(R.id.CO2List);
        dispenserList = findViewById(R.id.dispenserList);
        loc_type_LL = findViewById(R.id.loc_type_tv);


        Map<String,String> param = new HashMap<>();
        StringRequester.getData(DashboardAvailability.this, Constants.DASHBOARD_AVAILABILITY, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int locations_len = jsonArray.length();
                            // Create Array of Assets

                            HashMap<String, kegTypes> dataHolder = new HashMap<>();

                            // Todo, Coding HERE
                            for (int i=0; i<locations_len; i++) {
                                //  Step 1. Check if location is present in map
                                JSONObject current = jsonArray.getJSONObject(i);
                                String loc = current.getString("location");
                                kegTypes k;
                                if (dataHolder.containsKey(loc)){
                                    // Step 2. if Present then change the assign the value to the kegTypes obj by referring it by location name
                                    k = dataHolder.get(loc);
                                }
                                else { // Step 3. if not, create a kegTypes object, and assign that to new key
                                    k = new kegTypes();
                                    k.location_type = current.getString("loc_group");
                                }
                                // Update
                                switch (current.getString("ass_type")){
                                    case "k30":
                                        k.k30 = current.getInt("count");
                                        break;
                                    case "k50":
                                        k.k50 = current.getInt("count");
                                        break;
                                    case "CO2":
                                        k.co2 = current.getInt("count");
                                        break;
                                    case "Dispenser":
                                        k.disp = current.getInt("count");
                                        break;
                                }
                                dataHolder.put(loc,k);
                            }
                            updateUI(dataHolder);
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

    private void updateUI(HashMap<String, kegTypes> dataHolder) {

        Iterator it = dataHolder.entrySet().iterator();

        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();

            kegTypes k = (kegTypes) pair.getValue();

            TextView location = setTextView((String) pair.getKey());
            TextView loc_type = setTextView(k.location_type);
            TextView k30_t = setTextView(String.valueOf(k.k30));
            TextView k50_t = setTextView(String.valueOf(k.k50));
            TextView CO2_t = setTextView(String.valueOf(k.co2));
            TextView disp_t = setTextView(String.valueOf(k.disp));

            location_name_dashboard_LL.addView(location);
            loc_type_LL.addView(loc_type);
            k30List.addView(k30_t);
            k50List.addView(k50_t);
            CO2List.addView(CO2_t);
            dispenserList.addView(disp_t);

            it.remove();
        }
    }

    public TextView setTextView(String text)
    {
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.setText(text);
        return tv;
    }
}