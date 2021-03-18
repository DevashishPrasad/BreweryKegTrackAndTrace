package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TagScan extends AppCompatActivity {

    TabLayout tabLayout;
    TabItem k50, k30, kempty, kCO2, kDispenser;
    ViewPager vp;
    PageAdapter pageAdapter;
    AlertDialog.Builder builder;
    TextView userRfidTV;
    String userRfid, objectType;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;

    // Life cycle methods
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

        // RFID Textview
        userRfidTV = (TextView) findViewById(R.id.user_rfid_tv);

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
                        // TODO put in database
                        Toast.makeText(getApplicationContext(),"Put the tag in database and reflect the tag on screen",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        userRfidTV.setText(" ");
                        Toast.makeText(getApplicationContext(),"Reset the text view and don't put anything in the database",
                                Toast.LENGTH_SHORT).show();
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

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Scanned RF ID : " + userRfid);
        alert.setMessage("Object Type : " + objectType);
        alert.show();

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

                byte[] tagUid = tag.getId();  // store tag UID for use in addressed commands
                int blockAddress = 0; // block address that you want to read from/write to

                try {
                    nfcvTag.connect();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "PROBLEM WHILE CONNECTING TO TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    blockAddress = 0;
                    // Read single block
                    byte[] cmd = new byte[]{
                            (byte) 0x60,  // FLAGS
                            (byte) 0x20,  // READ_SINGLE_BLOCK
                            0, 0, 0, 0, 0, 0, 0, 0,
                            (byte) (blockAddress & 0x0ff)
                    };
                    System.arraycopy(tagUid, 0, cmd, 2, 8);

                    byte[] response = nfcvTag.transceive(cmd);

                    data = HexToString(bytesToHex(response));

//                    Log.d("READ SINGLE BLK", String.valueOf(response));
//                    Log.d("ORIG SIZE", String.valueOf(response.length));
//                    Log.d("SINGLE BLK HEX", bytesToHex(response));

                    blockAddress = 1;
                    // Read single block
                    cmd = new byte[]{
                            (byte) 0x60,  // FLAGS
                            (byte) 0x20,  // READ_SINGLE_BLOCK
                            0, 0, 0, 0, 0, 0, 0, 0,
                            (byte) (blockAddress & 0x0ff)
                    };
                    System.arraycopy(tagUid, 0, cmd, 2, 8);

                    response = nfcvTag.transceive(cmd);

                    data += HexToString(bytesToHex(response));

//                    Log.d("READ SINGLE BLK", String.valueOf(response));
//                    Log.d("ORIG SIZE", String.valueOf(response.length));
//                    Log.d("SINGLE BLK HEX", bytesToHex(response));

                    // TODO extract type from DB and active status
                    objectType = data;
                    userRfid = data;
                    userRfidTV.setText(data);
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
}