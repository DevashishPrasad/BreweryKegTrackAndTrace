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
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
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
        
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.select();
        pageAdapter.notifyDataSetChanged();

        tab = tabLayout.getTabAt(1);
        tab.select();
        pageAdapter.notifyDataSetChanged();


        tab = tabLayout.getTabAt(2);
        tab.select();
        pageAdapter.notifyDataSetChanged();

        tab = tabLayout.getTabAt(3);
        tab.select();
        pageAdapter.notifyDataSetChanged();


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
                        // TODO put in database

//                        t_type:k50
//                        t_asset_tag:9999999999
//                        t_asset_name:our test asset
//                        t_keg_status:1
//                        t_loc_frm_scan_type:0
//                        t_latitude:18.439421
//                        t_longitude:73.790352
//                        t_user_mobile:7757025466
//                        t_trans_rn:MH-QU 18 7259
//                        t_asset_status

                        int auto_manual,load_unload;
                        if(User.automanual == "manual")
                            auto_manual=0;
                        else
                            auto_manual=1;

                        if(User.loadunload == "load")
                            load_unload = 1;
                        else
                            load_unload = 0;


                        putInDatabase(
                                objectType,
                                tagSerial,
                                userRfid,
                                User.isFactory,
                                auto_manual,
                                User.place.location.getLatitude(),
                                User.place.location.getLongitude(),
                                User.mobile,
                                User.truckno,
                                load_unload
                        );

                        updateTab("Done");
//                        Toast.makeText(getApplicationContext(),"Put the tag in database and reflect the tag on screen",
//                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        userRfidTV.setText(" ");
                        updateTab("Rescanned");
//                        Toast.makeText(getApplicationContext(),"Reset the text view and don't put anything in the database",
//                                Toast.LENGTH_SHORT).show();
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

            currentTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            readTagData(currentTag);
        }
    }

    private void readTagData(Tag tag) {
        boolean techFound = false;
        String data;
        for (String tech : tag.getTechList()) {
            if (tech.equals(NfcV.class.getName())) {
                techFound = true;
                NfcV nfcvTag = NfcV.get(tag);

                byte[] tagUid = tag.getId(); // store tag UID for use in addressed commands
                int blockAddress = 0; // block address that you want to read from/write to

                // Tag Serial Number
                tagSerial = bytesToHex(tagUid);

                try {
                    nfcvTag.connect();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "PROBLEM WHILE CONNECTING TO TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
//                    blockAddress = 0;
//                    // Read single block
//                    byte[] cmd = new byte[]{
//                            (byte) 0x60,  // FLAGS
//                            (byte) 0x20,  // READ_SINGLE_BLOCK
//                            0, 0, 0, 0, 0, 0, 0, 0,
//                            (byte) (blockAddress & 0x0ff)
//                    };
//                    System.arraycopy(tagUid, 0, cmd, 2, 8);
//
//                    byte[] response = nfcvTag.transceive(cmd);
//
//                    data = HexToString(bytesToHex(response));
//
//                    blockAddress = 1;
//                    // Read single block
//                    cmd = new byte[]{
//                            (byte) 0x60,  // FLAGS
//                            (byte) 0x20,  // READ_SINGLE_BLOCK
//                            0, 0, 0, 0, 0, 0, 0, 0,
//                            (byte) (blockAddress & 0x0ff)
//                    };
//                    System.arraycopy(tagUid, 0, cmd, 2, 8);
//
//                    response = nfcvTag.transceive(cmd);
//                    data += HexToString(bytesToHex(response));

                    int offset = 0;  // offset of first block to read
                    int blocks = 4;  // number of blocks to read
                    byte[] cmd = new byte[] {
                            (byte) 0x60, // flags: addressed (= UID field present)
                            (byte) 0x23, // command: READ MULTIPLE BLOCKS
                            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,  // placeholder for tag UID
                            (byte) (offset & 0x0ff),  // first block number
                            (byte) ((blocks - 1) & 0x0ff)  // number of blocks (-1 as 0x00 means one block)
                    };
                    System.arraycopy(tagUid, 0, cmd, 2, 8);
                    byte[] response = nfcvTag.transceive(cmd);
                    data = HexToString(bytesToHex(response));

                    userRfid = data;
                    userRfidTV.setText(data);

                    Map<String,String> param = new HashMap<>();
                    param.put("ass_tag",tagSerial);
                    StringRequester.getData(TagScan.this,Constants.ASSET_URL, param,
                            new VolleyCallback() {
                                @Override
                                public void onSuccess(JSONObject jsonResponse) throws JSONException {
                                    if (!jsonResponse.getBoolean("error")) {
                                        JSONObject jsonObj = jsonResponse.getJSONObject("message");
                                        objectType = jsonObj.getString("ASS_TYPE");

                                        if(jsonObj.getInt("ASS_ACTIVE") == 1){
                                            //Creating dialog box
                                            AlertDialog alert = builder.create();
                                            //Setting the title manually
                                            alert.setTitle("Scanned RF ID : " + userRfid);
                                            alert.setMessage("Object Type : " + objectType);
                                            alert.show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"The scanned Tag is not Active",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else // Show error message
                                        Toast.makeText(getApplicationContext(),jsonResponse.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onFailure(String message) {
                                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ERROR WHILE READING THE TAG", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("ERROR", e.getMessage());
                    return;
                }

                try {
                    nfcvTag.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ERROR WHILE CLOSING THE TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (techFound == false) {
            Log.d("ERROR", "Tech Unkown");
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

    // A custom function to convert bytes to hex
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    private static String HexToString(String hex)
    {

        StringBuilder output = new StringBuilder();
        for (int i = 2; i < hex.length(); i+=2) {
            String str = hex.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString();
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
        ft.detach(fragment).attach(fragment).commit();
    }

    public void putInDatabase(String objectType, String tagSerial, String userRfid,
                              int isFactory, int auto_manual, double latitude, double longitude,
                              String mobile, String truckno, int load_unload) {

        Log.d("data", objectType + " " + tagSerial + " " + userRfid  + " " +
                        isFactory + " " +  auto_manual + " " +  latitude + " " +  longitude + " " +
                mobile + " " +  truckno + " " +  load_unload);

        Map<String,String> param = new HashMap<>();

        // TODO as this old tag is giving a garbage value, we are hard coding it for right now
        userRfid = "aa";

        // Put data into tagscan api
        param.put("t_type",objectType);
        param.put("t_asset_tag",tagSerial);
        param.put("t_asset_name",userRfid);
        param.put("t_keg_status",String.valueOf(isFactory));
        param.put("t_loc_frm_scan_type",String.valueOf(auto_manual));
        param.put("t_latitude",String.valueOf(latitude));
        param.put("t_longitude",String.valueOf(longitude));
        param.put("t_user_mobile",String.valueOf(mobile));
        param.put("t_trans_rn",String.valueOf(truckno));

        // TODO update the Asset stock also using load_unload

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
    }

}