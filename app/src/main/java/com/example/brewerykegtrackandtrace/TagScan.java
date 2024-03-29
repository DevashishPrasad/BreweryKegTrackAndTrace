package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TagScan extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem k50, k30_tab, kCO2, kDispenser;
    ViewPager vp;
    PageAdapter pageAdapter;
    AlertDialog.Builder builder;
    TextView userRfidTV, k30_count_tv, k50_count_tv, co2_count_tv, disp_count_tv;
    String userRfid, objectType, tagSerial;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    Tag myTag;
    boolean writeMode;
    int k30_count, k50_count, co2_count, disp_count;
    double ass_latitude,ass_longitude;
    ToneGenerator toneGen1;

    // Life cycle methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_scan);

        User.k30_list =  new ArrayList<TagScanKegListData>();
        User.k50_list =  new ArrayList<TagScanKegListData>();
        User.CO2_list =  new ArrayList<TagScanKegListData>();
        User.disp_list =  new ArrayList<TagScanKegListData>();

        // Set actionbar and footer
        User user = new User();
        User.setActionbar(TagScan.this);
        User.goHome(TagScan.this);
        // Initialize all views
        TextView loadunload = (TextView) findViewById(R.id.loadunload);
        TextView locTV = (TextView) findViewById(R.id.locationTV);

        // RFID Textview
        userRfidTV = (TextView) findViewById(R.id.user_rfid_tv);

        // Counter TextViews
        k30_count_tv = (TextView) findViewById(R.id.k30_tv);
        k50_count_tv = (TextView) findViewById(R.id.k50_tv);
        co2_count_tv = (TextView) findViewById(R.id.CO2_tv);
        disp_count_tv = (TextView) findViewById(R.id.disp_tv);

        // Counters
        k30_count = 0;
        k50_count = 0;
        co2_count = 0;
        disp_count = 0;

        // -------- Tab views ---------
        tabLayout = findViewById(R.id.tabLayout);
        k50 = findViewById(R.id.tab50);
        k30_tab =findViewById(R.id.tab30);
        kCO2 = findViewById(R.id.tabCO2);
        kDispenser = findViewById(R.id.tabDispenser);
        vp = findViewById(R.id.viewPaperVP);

        // Set Location and Loading unloading status
        locTV.setText(User.place.name);

        if(user.loadunload.equals("load"))
            loadunload.setText("Loading");
        else if(user.loadunload.equals("unload"))
            loadunload.setText("Unloading");

        // Tab Listeners
        pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        vp.setAdapter(pageAdapter);
        
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        tab.select();
        vp.setCurrentItem(tab.getPosition());
        pageAdapter.notifyDataSetChanged();

        tab = tabLayout.getTabAt(2);
        tab.select();
        vp.setCurrentItem(tab.getPosition());
        pageAdapter.notifyDataSetChanged();

        tab = tabLayout.getTabAt(3);
        tab.select();
        vp.setCurrentItem(tab.getPosition());
        pageAdapter.notifyDataSetChanged();

        tab = tabLayout.getTabAt(0);
        tab.select();
        vp.setCurrentItem(tab.getPosition());
        pageAdapter.notifyDataSetChanged();
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
                int pos = tab.getPosition();
                if (pos>=0 && pos <= 4) {
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
                    int auto_manual,load_unload,keg_status;
                    if(User.automanual.equals("manual"))
                        auto_manual=0;
                    else
                        auto_manual=1;

                    if(User.loadunload.equals("load"))
                        load_unload = 1;
                    else
                        load_unload = 0;

                    if((User.isFactory == 1 && load_unload == 1) || (User.isFactory == 0 && load_unload == 0))
                        keg_status = 1;
                    else
                        keg_status = 0;

                    putInDatabase(
                            keg_status,
                            tagSerial,
                            User.isFactory,
                            auto_manual,
                            User.place.location.getLatitude(),
                            User.place.location.getLongitude(),
                            User.mobile,
                            User.truckno,
                            load_unload
                    );

                    updateTab("Done");
                }
            })
            .setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //  Action for 'NO' Button
                    dialog.cancel();
                    userRfidTV.setText(" ");
                    updateTab("Rescanned");
                }
            });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    // NFC Methods
    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/

    private Tag currentTag;
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            tagSerial = bytesToString(tagId);

            NdefMessage[] msgs = null;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }

            if (msgs == null || msgs.length == 0) return;

            String text = "";
            String stagId = new String(msgs[0].getRecords()[0].getType());
            byte[] payload = msgs[0].getRecords()[0].getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
            int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

            try {
                // Get the Text
                text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            } catch (UnsupportedEncodingException e) {
                Log.e("UnsupportedEncoding", e.toString());
            }

            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
            userRfid = text;
            userRfidTV.setText(text);

            Map<String, String> param = new HashMap<>();
            param.put("ass_tag", tagSerial);
            StringRequester.getData(TagScan.this, Constants.ASSET_URL, param,
                    new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject jsonResponse) throws JSONException {
                            if (!jsonResponse.getBoolean("error")) {
                                JSONObject jsonObj = jsonResponse.getJSONObject("message");
                                objectType = jsonObj.getString("ASS_TYPE");

                                ass_latitude = jsonObj.getDouble("LATITUDE");
                                ass_longitude = jsonObj.getDouble("LONGITUDE");

                                // Validation
                                if (jsonObj.getInt("ASS_ACTIVE") == 1) {
                                    if (User.loadunload.equals("load")) {
                                        if ((ass_latitude != User.place.location.getLatitude() ||
                                                ass_longitude != User.place.location.getLongitude())
                                                &&
                                                (ass_latitude != 0.000000 && ass_longitude != 0.000000)
                                        ) {
                                            createAlert("The keg must be loaded from same location where it was unloaded");
                                        }
                                    }

                                    if (jsonObj.getInt("ASS_STOCK") == 1 && User.loadunload.equals("load")) {
                                        createAlert("The keg is already loaded in the truck");
                                        return;
                                    } else if (jsonObj.getInt("ASS_STOCK") == 0 && User.loadunload.equals("unload")) {
                                        createAlert("The keg is already unloaded from the truck");
                                        return;
                                    }

                                    if (jsonObj.getInt("ASS_STATUS") == 1) {
                                        if (User.isFactory == 1) {
                                            createAlert("A filled Keg cannot be unloaded at Factory");
                                            return;
                                        }
                                    } else if (jsonObj.getInt("ASS_STATUS") == 0) {
                                        if (User.isFactory == 0) {
                                            createAlert("An empty Keg should be unloaded at Factory");
                                            return;
                                        }
                                    }

                                    //Creating dialog box
                                    AlertDialog alert = builder.create();
                                    //Setting the title manually
                                    alert.setTitle("Scanned RF ID : " + userRfid);
                                    alert.setMessage("Object Type : " + objectType);
                                    alert.show();
                                } else {
                                    createAlert("The scanned Tag is not Active");
                                    return;
                                }

                            } else // Show error message
                                Toast.makeText(getApplicationContext(), "Transaction failure! Tag might not be registered", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(String message) {
                            Toast.makeText(getApplicationContext(), "Transaction Failure! Tag might not be registered", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }
    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff(){
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void updateTab(String status) {

        int tab_id;
        String dateTime = new SimpleDateFormat("EE MMM dd yyyy hh:mm:ss aaa", Locale.getDefault()).format(new Date());

        Fragment fragment =  null;

        switch(objectType){
            case "k50":
                tab_id = 0;
                User.k50_list.add(new TagScanKegListData(dateTime, userRfid, status));
                fragment =  (k50) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:"+R.id.viewPaperVP+":"+tab_id);
                if(status.equals("Done")) {
                    k50_count += 1;
                    k50_count_tv.setText(String.valueOf(k50_count));
                }
                break;
            case "k30":
                tab_id = 1;
                User.k30_list.add(new TagScanKegListData(dateTime, userRfid, status));
                fragment =  (k30) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:"+R.id.viewPaperVP+":"+tab_id);
                if(status.equals("Done")) {
                    k30_count += 1;
                    k30_count_tv.setText(String.valueOf(k30_count));
                }
                break;
            case "CO2":
                tab_id = 2;
                User.CO2_list.add(new TagScanKegListData(dateTime, userRfid, status));
                fragment =  (kCO2) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:"+R.id.viewPaperVP+":"+tab_id);
                if(status.equals("Done")) {
                    co2_count += 1;
                    co2_count_tv.setText(String.valueOf(co2_count));
                }
                break;
            case "Dispenser":
                tab_id = 3;
                User.disp_list.add(new TagScanKegListData(dateTime, userRfid, status));
                fragment =  (kDispenser) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:"+R.id.viewPaperVP+":"+tab_id);
                if(status.equals("Done")) {
                    disp_count += 1;
                    disp_count_tv.setText(String.valueOf(disp_count));
                }
                break;

            default:
                tab_id = 0;
        }

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        if (fragment != null) {
            ft.detach(fragment).attach(fragment).commit();
        }
    }

    public void putInDatabase(int keg_status, String tagSerial,
                              int isFactory, int auto_manual, double latitude, double longitude,
                              String mobile, String truckno, int load_unload) {

        Map<String,String> param = new HashMap<>();

        // Put data into tagscan api
        param.put("t_type",String.valueOf(load_unload));
        param.put("t_asset_tag",tagSerial);
        param.put("t_asset_name",userRfid);
        param.put("t_keg_status",String.valueOf(isFactory));
        param.put("t_loc_frm_scan_type",String.valueOf(auto_manual));
        param.put("t_latitude",String.valueOf(latitude));
        param.put("t_longitude",String.valueOf(longitude));
        param.put("t_user_mobile",String.valueOf(mobile));
        param.put("t_trans_rn",String.valueOf(truckno));

        StringRequester.getData(TagScan.this, Constants.TRANSACTION_URL, param,
            new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    try {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(TagScan.this,message,Toast.LENGTH_SHORT).show();
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

        Log.d("data-", objectType + "-" + tagSerial + "-" + userRfid  + "-" +
                load_unload + "-" + keg_status);
        if(User.loadunload.equals("unload")){
            ass_latitude=latitude;
            ass_longitude=longitude;
        }

        // Put data into asset api
        param.put("ass_type",objectType);
        param.put("ass_tag",tagSerial);
        param.put("ass_name",userRfid);
        param.put("ass_active",String.valueOf(1));
        param.put("ass_stock",String.valueOf(load_unload));
        param.put("ass_status",String.valueOf(keg_status));
        param.put("latitude",String.valueOf(ass_latitude));
        param.put("longitude",String.valueOf(ass_longitude));

        StringRequester.getData(TagScan.this, Constants.ASSETS_EDIT_URL, param,
            new VolleyCallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    try {
                        String message = jsonResponse.getString("message");
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            });
    }

    void createAlert(String message) {
        // Confirmation Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.show();
    }

    private static String bytesToString(byte[] tagId){
        String hexdump = new String();
        for (int i = 0; i < tagId.length; i++) {
            String x = Integer.toHexString(((int) tagId[i] & 0xff));
            if (x.length() == 1)
                x = '0' + x;
            hexdump += x;
        }
        return hexdump;
    }
}