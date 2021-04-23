package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminKegAdd extends AppCompatActivity {

    // Some Constants
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";

    // Required for NFC
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    Tag myTag;
    private Tag currentTag;


    // UI binders
    TextView tagSerialNumber_UI, currentKegID;
    Switch isActive;
    Spinner spinner_UI;
    EditText writeKegID;

    // Data
    String tagSerialNo;
    final String[] entries = {"30 Liters", "50 Liters", "CO2", "Dispenser"};
    final String[] db_objects = {"k30","k50","CO2","Dispenser"};
    boolean writeMode,isEdit, updateDb, dbOperationCompleted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_keg_add);
        User.setActionbar(AdminKegAdd.this);
        User.goHome(AdminKegAdd.this);

        // Set flags
        updateDb = false; // To indicate the update in DB is successful
        dbOperationCompleted = false; // To indicate the operation with db is completed

        // UI BINDING
        isActive = findViewById(R.id.kegSwitch);
        writeKegID = findViewById(R.id.writeKegID);
        tagSerialNumber_UI = findViewById(R.id.TagSerialNumber);
        currentKegID = findViewById(R.id.currentKegID);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinner_UI = (Spinner) findViewById(R.id.keg_spinner);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, entries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_UI.setAdapter(aa);
        tagSerialNo = "";

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

    public void createKeg(View view) {
        String readID = ((TextView) findViewById(R.id.TagSerialNumber)).getText().toString();
        String writeID = ((TextView) findViewById(R.id.writeKegID)).getText().toString();
        String rescanID = ((TextView) findViewById(R.id.currentKegID)).getText().toString();
        boolean activeStatus = isActive.isChecked();
        String spinner = spinner_UI.getSelectedItem().toString();

        Toast.makeText(this,readID+" -- "+writeID+" -- "+rescanID+" -- "+activeStatus+" -- "+spinner,Toast.LENGTH_LONG).show();
        finish();
    }

    // READ FLOW
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

                // Tag Serial Number
                String tempTSN = bytesToHex(tagUid);
                // Check if the tag is new tag, if it is, then only start the volley thread and
                // update the UI
                if(!tagSerialNo.equals(tempTSN)) {
                    tagSerialNo = tempTSN;
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
                        data = User.filterGarbage(data);

                        Log.e("DATA_NFC",data); // DEBUG
                        data = data.replace("~","");
                        Log.e("DATA_NFC",data); // DEBUG

                        // UPDATE UI
                        writeKegID.setText(data);
                        tagSerialNumber_UI.setText(tagSerialNo);

                        checkDBNUpdateUI(data);

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
        }
        if (!techFound)
            Log.d("ERROR", "Tech Unkown");
    }

    public void checkDBNUpdateUI(String data)
    {
        // Complete this function
        Map<String,String> param = new HashMap<>();
        param.put("ass_name",data);
        param.put("ass_tag", tagSerialNo);
        StringRequester.getData(this, Constants.ASSET_URL, param, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject keg = result.getJSONObject("message");
                    Toast.makeText(getApplicationContext(),"Registered Tag detected",Toast.LENGTH_SHORT).show();

                    // UPDATE UI more
                    int position = Arrays.asList(db_objects).indexOf(keg.getString("ASS_TYPE"));
                    spinner_UI.setSelection(position);
                    isActive.setChecked(keg.getString("ASS_ACTIVE").equals("1"));
                    currentKegID.setText(keg.getString("ASS_NAME"));
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"New tag detected",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // WRITE FLOW
    public void clickAndWrite(View view) {
        if(myTag == null) {
            Toast.makeText(this, ERROR_DETECTED, Toast.LENGTH_LONG).show();
        } else {
            // Confirmation Dialog box
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Validate the user input
                            if(writeKegID.getText().toString().trim().equals("")) {
                                Toast.makeText(AdminKegAdd.this, "Key ID cannot be empty", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            isPresentDB();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setTitle("Are you sure?");
            alert.setMessage("Do you want to delete this NFC Tag?");
            alert.show();

        }
    }

    public void isPresentDB() {
        String kegID = writeKegID.getText().toString().trim();

        // Complete this function
        Map<String,String> param = new HashMap<>();
        param.put("ass_name",kegID);
        param.put("ass_tag", tagSerialNo);
        StringRequester.getData(this, Constants.ASSET_URL, param, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONObject keg = result.getJSONObject("message");
                    writeOrUpdateDatabase(true);
                } catch (JSONException e) {
                    writeOrUpdateDatabase(false);
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeOrUpdateDatabase(Boolean isEdit) {
        String kegID = writeKegID.getText().toString().trim();

        // Complete this function
        Map<String,String> param = new HashMap<>();
        String URL = isEdit ? Constants.ASSETS_EDIT_URL : Constants.ASSETS_REGISTER_URL;

        // Put data into tagscan
        param.put("ass_name",kegID);
        param.put("ass_tag", tagSerialNo);
        param.put("ass_active",isActive.isChecked() ? "1" : "0");
        param.put("ass_status","0");
        param.put("ass_stock","0");
        param.put("ass_type",spinner_UI.getSelectedItem().toString());

        StringRequester.getData(this, URL, param, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                try {
                    writeTagData();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error occurred! Please try again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void writeTagData() throws IOException, FormatException {
        boolean techFound = false;
        String text= writeKegID.getText().toString().trim();

        if(text.length() > 32)
            text = text.substring(0,32);

//        // Pad the string to fixed length
//        int width = 16;
//        char fill = '~';
//        text = new String(new char[width - text.length()]).replace('\0', fill) + text;

        // Convert string to bytes
        byte[] byteText = text.getBytes();

        for (String tech : myTag.getTechList()) {
            if (tech.equals(NfcV.class.getName())) {
                techFound = true;
                NfcV nfcvTag = NfcV.get(myTag);

                byte[] tagUid = myTag.getId();    // store tag UID for use in addressed commands
                String tempTSN = bytesToHex(tagUid);

                if(!tempTSN.equals(tagSerialNo)) {
                    Toast.makeText(getApplicationContext(),"Tag is different",Toast.LENGTH_SHORT).show();
                    return;
                }

                int blockAddress = 0;           // block address that you want to read from/write to

                try {
                    nfcvTag.connect();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "PROBLEM WHILE CONNECTING TO TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    blockAddress=0;
                    // Write single block
                    byte[] cmd = new byte[] {
                            (byte)0x60,  // FLAGS
                            (byte)0x21,  // WRITE_SINGLE_BLOCK
                            0, 0, 0, 0, 0, 0, 0, 0,
                            (byte)(blockAddress & 0x0ff),
                            byteText[0], byteText[1], byteText[2], byteText[3]
                    };

                    System.arraycopy(tagUid, 0, cmd, 2, 8);

                    Log.d("DDD","BEFORE DONE");

                    // For loop over here : 32 bytes to be written
                    byte[] response = nfcvTag.transceive(cmd);

                    Log.d("DDD","DONE WRITING");
                } catch (IOException e) {
//                    Toast.makeText(getApplicationContext(), "ERROR WHILE WRITING THE TAG", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("ERROR 1", e.getMessage());
                }

//                try {
//                    blockAddress=1;
//                    // Write single block
//                    byte[] cmd = new byte[] {
//                            (byte)0x40,  // FLAGS
//                            (byte)0x21,  // WRITE_SINGLE_BLOCK
//                            0, 0, 0, 0, 0, 0, 0, 0,
//                            (byte)(blockAddress & 0x0ff),
//                            byteText[8], byteText[9], byteText[10], byteText[11], byteText[12], byteText[13], byteText[14], byteText[15]
//                    };
//
//                    System.arraycopy(tagUid, 0, cmd, 2, 8);
//
//                    byte[] response = nfcvTag.transceive(cmd);
//
//                } catch (IOException e) {
////                    Toast.makeText(getApplicationContext(), "ERROR WHILE WRITING THE TAG", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                    Log.d("ERROR 2", e.getMessage());
//                }

                try {
                    nfcvTag.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "ERROR WHILE CLOSING THE TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        if (!techFound) {
            Log.d("ERROR", "Tech Unkown");
        }
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

    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn(){
        writeMode = true;
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
        }
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