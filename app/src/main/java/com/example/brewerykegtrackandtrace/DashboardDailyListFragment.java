package com.example.brewerykegtrackandtrace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DashboardDailyListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;



    public DashboardDailyListFragment() {
        // Required empty public constructor
    }

    public static DashboardDailyListFragment newInstance(String param1, String param2) {
        DashboardDailyListFragment fragment = new DashboardDailyListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Todo get json from server and initialize it here


    }

    LinearLayout transaction_daily_location,transaction_daily_truck,kegLL,loadCountLL,unloadCountLL,transaction_daily_time;
    View vthis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_list, container, false);

        transaction_daily_location = view.findViewById(R.id.transaction_daily_location);
        transaction_daily_truck = view.findViewById(R.id.transaction_daily_truck);
        loadCountLL = view.findViewById(R.id.transaction_daily_load);
        unloadCountLL = view.findViewById(R.id.transaction_daily_unload);
        kegLL = view.findViewById(R.id.transaction_daily_keg);
        transaction_daily_time = view.findViewById(R.id.transaction_daily_time);

        vthis = view;
        updateUI();
        return view;
    }

    private void updateUI() {

        Iterator it = User.dataHolder_daily.entrySet().iterator();

        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry)it.next();

            TransKegType k = (TransKegType) pair.getValue();

            TextView k30_l = setTextView(String.valueOf(k.k30_l));
            TextView k30_un = setTextView(String.valueOf(k.k30_un));

            TextView k50_l = setTextView(String.valueOf(k.k50_l));
            TextView k50_un = setTextView(String.valueOf(k.k50_un));

            TextView co2_l = setTextView(String.valueOf(k.co2_l));
            TextView co2_un = setTextView(String.valueOf(k.co2_un));

            TextView disp_l = setTextView(String.valueOf(k.disp_l));
            TextView disp_un = setTextView(String.valueOf(k.disp_un));


            TextView k30_time = setTextView(String.valueOf(k.k30_time)) ;
            TextView k50_time = setTextView(String.valueOf(k.k50_time));
            TextView co2_time = setTextView(String.valueOf(k.co2_time));
            TextView disp_time = setTextView(String.valueOf(k.disp_time));

            for (int i = 0;i<4;i++)
            {
                transaction_daily_location.addView(setTextView((String) pair.getKey()));
                transaction_daily_truck.addView(setTextView(k.t_trans_rn));
            }

            kegLL.addView(setTextView("k30"));
            loadCountLL.addView(k30_l);
            unloadCountLL.addView(k30_un);
            transaction_daily_time.addView(k30_time);

            kegLL.addView(setTextView("k50"));
            loadCountLL.addView(k50_l);
            unloadCountLL.addView(k50_un);
            transaction_daily_time.addView(k50_time);

            kegLL.addView(setTextView("CO2"));
            loadCountLL.addView(co2_l);
            unloadCountLL.addView(co2_un);
            transaction_daily_time.addView(co2_time);

            kegLL.addView(setTextView("Disp"));
            loadCountLL.addView(disp_l);
            unloadCountLL.addView(disp_un);
            transaction_daily_time.addView(disp_time);

            it.remove();
        }
    }


    public TextView setTextView(String text)
    {
        TextView tv = new TextView(getContext().getApplicationContext());

        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.setText(text);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setMaxLines(1);

        return tv;
    }
}