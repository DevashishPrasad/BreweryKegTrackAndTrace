package com.example.brewerykegtrackandtrace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardMonthlyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardMonthlyListFragment extends Fragment {

    LinearLayout transaction_daily_location,transaction_daily_truck,kegLL,loadCountLL,unloadCountLL;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardMonthlyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardMonthlyListFragment newInstance(String param1, String param2) {
        DashboardMonthlyListFragment fragment = new DashboardMonthlyListFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monthly_list, container, false);
        transaction_daily_location = view.findViewById(R.id.transaction_monthly_location);
        transaction_daily_truck = view.findViewById(R.id.transaction_monthly_truck);
        loadCountLL = view.findViewById(R.id.transaction_monthly_load);
        unloadCountLL = view.findViewById(R.id.transaction_monthly_unload);
        kegLL = view.findViewById(R.id.transaction_monthly_keg);
        updateUI();
        return view;
    }

    private void updateUI() {

        Iterator it = User.dataHolder_Monthly.entrySet().iterator();

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


            for (int i = 0;i<4;i++)
            {
                transaction_daily_location.addView(setTextView((String) pair.getKey()));
                transaction_daily_truck.addView(setTextView(k.t_trans_rn));
            }

            kegLL.addView(setTextView("k30"));
            loadCountLL.addView(k30_l);
            unloadCountLL.addView(k30_un);

            kegLL.addView(setTextView("k50"));
            loadCountLL.addView(k50_l);
            unloadCountLL.addView(k50_un);


            kegLL.addView(setTextView("CO2"));
            loadCountLL.addView(co2_l);
            unloadCountLL.addView(co2_un);

            kegLL.addView(setTextView("Disp"));
            loadCountLL.addView(disp_l);
            unloadCountLL.addView(disp_un);

            it.remove();
        }
    }

    public TextView setTextView(String text)
    {
        TextView tv = new TextView(getContext().getApplicationContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        tv.setText(text);

        return tv;
    }
}