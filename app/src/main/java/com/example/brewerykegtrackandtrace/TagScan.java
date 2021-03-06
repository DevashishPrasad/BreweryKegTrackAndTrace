package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TagScan extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem k50,k30,kempty,kCO2,kDispenser;
    ViewPager vp;
    PageAdapter pageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_scan);

        // Set actionbar and footer
        User user = new User();
        User.setActionbar(TagScan.this);
        User.goHome(TagScan.this);

        // Initialize all views
        TextView loadunload = (TextView) findViewById(R.id.loadunload);
        TextView locTV = (TextView) findViewById(R.id.locationTV);

        // -------- Tab views ---------
        tabLayout = findViewById(R.id.tabLayout);
        k50 = findViewById(R.id.tab50);
        k30 =findViewById(R.id.tab30);
        kempty = findViewById(R.id.tabempty);
        kCO2 = findViewById(R.id.tabCO2);
        kDispenser = findViewById(R.id.tabDispenser);
        vp = findViewById(R.id.viewPaperVP);

        // Set Location and Loading unloading status
        locTV.setText(User.location);
        if(user.loadunload.equals("load"))
            loadunload.setText("Loading");
        else if(user.loadunload.equals("unload"))
            loadunload.setText("Unloading");

        // Tab Listeners
        pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        vp.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                int pos = tab.getPosition();
                if (pos>=0 && pos <= 4)
                {
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

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}