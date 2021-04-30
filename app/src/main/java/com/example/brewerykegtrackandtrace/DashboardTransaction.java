package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class TransKegType{
    public int k30_l=0,k30_un=0;
    public int k50_l=0,k50_un=0;
    public int co2_l=0,co2_un=0;
    public int disp_l=0,disp_un=0;
    String t_trans_rn;
};

public class DashboardTransaction extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem dailyTI,weeklyTI,MonthlyTI;
    ViewPager viewPager;
    DashboardPageAdapter pageAdapter;
    HashMap<String, TransKegType> dataHolder_daily,dataHolder_weekly,dataHolder_Monthly;


    boolean daily_done,weekly_done,monthly_done;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_transaction);
        User.setActionbar(DashboardTransaction.this);
        User.goHome(this);

        tabLayout = findViewById(R.id.dashboard_tabLayout);
        dailyTI = findViewById(R.id.daily_tab);
        weeklyTI = findViewById(R.id.weekly_tab);
        MonthlyTI = findViewById(R.id.monthly_tab);
        viewPager = findViewById(R.id.VP_dashboard);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        daily_done = false;
        weekly_done = false;
        monthly_done = false;

        db_request(1);
        db_request(7);
        db_request(30);


    }

    void db_request(int type)
    {
        Map<String,String> param = new HashMap<>();

        switch (type){
            case 1:
                param.put("days","1");
                break;
            case 7:
                param.put("days","7");
                break;
            case 30:
                param.put("days","30");
                break;
        }

        StringRequester.getLocation(this, Constants.DASHBOARD_TRANSACTION, param,
                new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject jsonResponse) {
                        try {
                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
                            int locations_len = jsonArray.length();
                            HashMap<String, TransKegType> dataHolder = new HashMap<>();

                            for (int i=0; i<locations_len; i++) {
                                //  Step 1. Check if location is present in map
                                JSONObject current = jsonArray.getJSONObject(i);
                                String loc = current.getString("location");
                                TransKegType k;
                                if (dataHolder.containsKey(loc)){
                                    // Step 2. if Present then change the assign the value to the kegTypes obj by referring it by location name
                                    k = dataHolder.get(loc);
                                }
                                else { // Step 3. if not, create a kegTypes object, and assign that to new key
                                    k = new TransKegType();
                                    k.t_trans_rn = current.getString("t_trans_rn");
                                }
                                // Update
                                switch (current.getString("ass_type")){
                                    case "k30":
                                        if (current.getString("t_type").equals("0")) // Unload
                                            k.k30_un = current.getInt("count");
                                        else
                                            k.k30_l = current.getInt("count");
                                        break;
                                    case "k50":
                                        if (current.getString("t_type").equals("0")) // Unload
                                            k.k50_un = current.getInt("count");
                                        else
                                            k.k50_l = current.getInt("count");
                                        break;
                                    case "CO2":
                                        if (current.getString("t_type").equals("0")) // Unload
                                            k.co2_un = current.getInt("count");
                                        else
                                            k.co2_l = current.getInt("count");
                                        break;
                                    case "Dispenser":
                                        if (current.getString("t_type").equals("0")) // Unload
                                            k.disp_un = current.getInt("count");
                                        else
                                            k.disp_l = current.getInt("count");

                                        break;
                                }
                                dataHolder.put(loc,k);
                            }

                            switch (type){
                                case 1:
                                    dataHolder_daily = dataHolder;
                                    daily_done = true;
                                    break;
                                case 7:
                                    dataHolder_weekly = dataHolder;
                                    weekly_done = true;
                                    break;
                                case 30:
                                    dataHolder_Monthly= dataHolder;
                                    monthly_done = true;
                                    break;
                            }
                            updateChecker();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onFailure(String message) {
                    }
                });
    }
    void updateChecker()
    {
        if(daily_done && weekly_done && monthly_done)
        {
            progressDialog.dismiss();
            User.dataHolder_daily = dataHolder_daily;
            User.dataHolder_weekly = dataHolder_weekly;
            User.dataHolder_Monthly = dataHolder_Monthly;
            turnTabsOn();
        }
    }
    void turnTabsOn()
    {
        pageAdapter = new DashboardPageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                int pos = tab.getPosition();
                if (pos>=0 && pos <= 3) {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }






}