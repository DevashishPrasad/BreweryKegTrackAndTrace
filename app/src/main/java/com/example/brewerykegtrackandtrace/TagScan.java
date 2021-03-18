package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TagScan extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem k50, k30, kempty, kCO2, kDispenser;
    ViewPager vp;
    PageAdapter pageAdapter;
    AlertDialog.Builder builder;

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

        builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Toast.makeText(getApplicationContext(),"Put the tag in database and reflect the tag on screen",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),"Reset the text view and don't put anything in the database",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Scanned RF ID");
        alert.setMessage("Here the Tag ID will appear");
        alert.show();
    }
}