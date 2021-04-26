package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportTextView extends AppCompatActivity {
    LinearLayout nodlLL,tagserialLL,tagnameLL;
    TextView startDateTV,endDateTV,kegTypeTV,locationTV,NORTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_text_view);

        User.setActionbar(ReportTextView.this);
        User.goHome(this);

        // Get UI Elements
        nodlLL = findViewById(R.id.nodlLL);
        tagserialLL = findViewById(R.id.tagserialLL);
        tagnameLL = findViewById(R.id.tagnameLL);

        startDateTV = findViewById(R.id.startDateTV);
        endDateTV = findViewById(R.id.endDateTV);
        kegTypeTV = findViewById(R.id.kegTypeTV);
        locationTV = findViewById(R.id.locationTV);
        NORTV = findViewById(R.id.nortv);

        // Update the Report Meta Data on UI
        updateUiWithReportMetaData();

        try {
            updateUiWithReport(); // Update the Table
        } catch (JSONException e) {
            // Todo Remove this
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    void updateUiWithReportMetaData()
    {
        int len = User.reportJson.length();
        startDateTV.setText(User.editData.get("start_date"));
        endDateTV.setText(User.editData.get("end_date"));
        kegTypeTV.setText(User.editData.get("keg_type"));
        locationTV.setText(User.editData.get("location"));
        NORTV.setText(String.valueOf(len));
    }

    void updateUiWithReport() throws JSONException {
        int len = User.reportJson.length();
        for (int i = 0; i < len; i++) {
            JSONObject objects =  User.reportJson.getJSONObject(i);

            TextView tagSerial = new TextView(this);
            TextView tagname = new TextView(this);
            TextView nodl = new TextView(this);

            tagSerial.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tagSerial.setText(objects.getString("t_asset_tag"));

            tagname.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            tagname.setText(objects.getString("t_asset_name"));

            nodl.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            nodl.setText(objects.getString("no_days"));

            tagserialLL.addView(tagSerial);
            tagnameLL.addView(tagname);
            nodlLL.addView(nodl);

        }

    }
}