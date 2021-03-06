package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AdminKegView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_keg_view);

        KegRecyclerListData[] myListData = new KegRecyclerListData[] {
                new KegRecyclerListData("user name 1", "3457823894"),
                new KegRecyclerListData("user name 2", "8337823828"),
                new KegRecyclerListData("user name 3", "3457826485"),
                new KegRecyclerListData("user name 4", "1434732842"),
                new KegRecyclerListData("user name 5", "2462823863"),
                new KegRecyclerListData("user name 6", "3456925865"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888"),
                new KegRecyclerListData("user name 7", "9119423888")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.keg_recycler_view);
        KegRecyclerAdapter adapter = new KegRecyclerAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}