package com.example.brewerykegtrackandtrace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link kCO2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class kCO2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public kCO2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment kCO2.
     */
    // TODO: Rename and change types and number of parameters
    public static kCO2 newInstance(String param1, String param2) {
        kCO2 fragment = new kCO2();
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
        View view = inflater.inflate(R.layout.fragment_k_c_o2, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.tag_scan_RV_c_o2);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        TagScanKegListData[] myListData = new TagScanKegListData[] {
                new TagScanKegListData("March 20 21","co2:1234", "DONE"),
                new TagScanKegListData("Feb 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),
                new TagScanKegListData("March 20 21","RFID:1234", "DONE"),


        };


        TagScanKegAdapter adapter = new TagScanKegAdapter(myListData);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
}