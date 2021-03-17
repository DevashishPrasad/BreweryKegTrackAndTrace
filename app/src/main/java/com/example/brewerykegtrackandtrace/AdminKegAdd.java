package com.example.brewerykegtrackandtrace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AdminKegAdd extends AppCompatActivity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView tagSerialNumber,rescannedKegID;
    EditText writeKegID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_keg_add);

        // INIT
        writeKegID = findViewById(R.id.writeKegID);
        tagSerialNumber = findViewById(R.id.TagSerialNumber);
        rescannedKegID = findViewById(R.id.rescannedKegID);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.keg_spinner);

        String[] entries = {"30 Liters", "50 Liters", "Empty", "CO2", "Dispenser"};

        //Creating the ArrayAdapter instance having the list of entries
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, entries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        User.setActionbar(AdminKegAdd.this);
        User.goHome(AdminKegAdd.this);

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
        String rescanID = ((TextView) findViewById(R.id.rescannedKegID)).getText().toString();
        boolean activeStatus = ((Switch) findViewById(R.id.kegSwitch)).isChecked();
        String spinner = ((Spinner) findViewById(R.id.keg_spinner)).getSelectedItem().toString();

        Toast.makeText(this,readID+" "+writeID+" "+rescanID+" "+activeStatus+" "+spinner,Toast.LENGTH_LONG).show();
        finish();
    }

    public void writeTag(View view) {
        try {
            if(myTag ==null) {
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {

                String kegID = writeKegID.getText().toString().trim();

                // TODO 1. Validation of ID
                //      2. Integrate DB
                if (!isIdPresentInDB(kegID)) {
                    write(kegID, myTag);
                    Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(context, "Keg ID already present", Toast.LENGTH_LONG).show();

            }
        } catch (IOException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        } catch (FormatException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
            e.printStackTrace();
        }
    }

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

                tagSerialNumber.setText(bytesToHex(tagUid));

                int blockAddress = 0; // block address that you want to read from/write to

                try {
                    nfcvTag.connect();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "PROBLEM WHILE CONNECTING TO TAG", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    blockAddress=0;
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

                    blockAddress=1;
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

                    rescannedKegID.setText(data);
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


    public boolean isIdPresentInDB(String kegId)
    {
        // Complete this function
        return false;
    }


    private void write(String text, Tag tag) throws IOException, FormatException {
        boolean techFound = false;

        // Data String
        String data;

        if(text.length() > 15)
            text = text.substring(0,16);

        // Pad the string to fixed length
        int width = 16;
        char fill = '~';
        text = new String(new char[width - text.length()]).replace('\0', fill) + text;

        // Convert string to bytes
        byte[] byteText = text.getBytes();

        for (String tech : tag.getTechList()) {
            if (tech.equals(NfcV.class.getName())) {
                techFound = true;
                NfcV nfcvTag = NfcV.get(tag);

                byte[] tagUid = tag.getId();    // store tag UID for use in addressed commands
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
                            byteText[0], byteText[1], byteText[2], byteText[3], byteText[4], byteText[5], byteText[6], byteText[7]
                    };

                    System.arraycopy(tagUid, 0, cmd, 2, 8);

                    byte[] response = nfcvTag.transceive(cmd);

                } catch (IOException e) {
//                    Toast.makeText(getApplicationContext(), "ERROR WHILE WRITING THE TAG", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("ERROR", e.getMessage());
                }

                try {
                    blockAddress=1;
                    // Write single block
                    byte[] cmd = new byte[] {
                            (byte)0x60,  // FLAGS
                            (byte)0x21,  // WRITE_SINGLE_BLOCK
                            0, 0, 0, 0, 0, 0, 0, 0,
                            (byte)(blockAddress & 0x0ff),
                            byteText[8], byteText[9], byteText[10], byteText[11], byteText[12], byteText[13], byteText[14], byteText[15]
                    };

                    System.arraycopy(tagUid, 0, cmd, 2, 8);

                    byte[] response = nfcvTag.transceive(cmd);

                } catch (IOException e) {
//                    Toast.makeText(getApplicationContext(), "ERROR WHILE WRITING THE TAG", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    Log.d("ERROR", e.getMessage());
                }

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