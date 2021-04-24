package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class ReportTextView extends AppCompatActivity {
    LinearLayout listForRecords;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_text_view);

        User.setActionbar(ReportTextView.this);
        User.goHome(this);

        listForRecords = findViewById(R.id.listForRecordsLL);

        try {
            updateUiWithReport();
        } catch (JSONException e) {
            // Todo Remove this
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }


    void updateUiWithReport() throws JSONException {
        int len = User.reportJson.length();
        for (int i = 0; i < len; i++) {
            JSONObject objects =  User.reportJson.getJSONObject(i);

            String row = objects.getString("t_asset_tag")+"  "+
                    objects.getString("t_asset_name")+"  "+
                    objects.getString("no_days");

            TextView newTextView = new TextView(this);
            newTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            newTextView.setText(row);
            listForRecords.addView(newTextView);

        }

    }
}