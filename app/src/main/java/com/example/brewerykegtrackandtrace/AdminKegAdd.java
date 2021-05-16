package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.io.UnsupportedEncodingException;
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
    ToneGenerator toneGen1 ;
    // Data
    String tagSerialNo;
    final String[] entries = {"30 Liters", "50 Liters", "CO2", "Dispenser"};
    final String[] db_objects = {"k30","k50","CO2","Dispenser"};
    boolean writeMode, updateDb, dbOperationCompleted;

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
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100); // For Beep sound

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

    // READ FLOW
    private void readFromIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            String tempTSN = bytesToString(tagId);

            if(tagSerialNo.equals(tempTSN))
                return;
            tagSerialNo = tempTSN;
            tagSerialNumber_UI.setText(tagSerialNo);
            toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

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

            Log.d("stagId",stagId);

            checkDBNUpdateUI(text);
        }
    }

    public void checkDBNUpdateUI(String data) {
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
                    writeKegID.setText(data);
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
            alert.setMessage("Do you want to write this NFC Tag?");
            alert.show();

        }
    }

    public void isPresentDB() {

        // Complete this function
        Map<String,String> param = new HashMap<>();
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

    private void writeOrUpdateDatabase(boolean isEdit) {
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
        param.put("latitude","0");
        param.put("longitude","0");
        param.put("ass_type",db_objects[spinner_UI.getSelectedItemPosition()].trim());

        StringRequester.getData(this, URL, param, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                try {
                    if(myTag ==null) {
                        Toast.makeText(AdminKegAdd.this, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(myTag,isEdit);
                        Toast.makeText(AdminKegAdd.this, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AdminKegAdd.this, WRITE_ERROR, Toast.LENGTH_LONG ).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void write(Tag tag, boolean isEdit) throws IOException, FormatException {
        String text= writeKegID.getText().toString().trim();
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);

        NdefFormatable formatable = NdefFormatable.get(tag);

        if (formatable != null) {
            formatable.connect();
            formatable.format(message);
            formatable.close();
        } else {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(message);
            ndef.close();
        }

        String success_msg = isEdit ? "Tag was updated successfully!" : "Tag was created successfully!";
        Toast.makeText(getApplicationContext(), success_msg, Toast.LENGTH_LONG).show();
        finish();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
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
//    public static String bytesToHex(byte[] bytes) {
//        byte[] hexChars = new byte[bytes.length * 2];
//        for (int j = 0; j < bytes.length; j++) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
//        return new String(hexChars, StandardCharsets.UTF_8);
//    }
//
//    private static String HexToString(String hex)
//    {
//        StringBuilder output = new StringBuilder();
//        for (int i = 2; i < hex.length(); i+=2) {
//            String str = hex.substring(i, i+2);
//            output.append((char)Integer.parseInt(str, 16));
//        }
//        return output.toString();
//    }

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