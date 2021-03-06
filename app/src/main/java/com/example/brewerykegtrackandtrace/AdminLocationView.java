package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AdminLocationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_location_view);

        LocationRecyclerListData[] myListData = new LocationRecyclerListData[] {
                new LocationRecyclerListData("user name 1", "3457823894", "3457823894"),
                new LocationRecyclerListData("user name 2", "8337823828", "3457823894"),
                new LocationRecyclerListData("user name 3", "3457826485", "3457823894"),
                new LocationRecyclerListData("user name 4", "1434732842", "3457823894"),
                new LocationRecyclerListData("user name 5", "2462823863", "3457823894"),
                new LocationRecyclerListData("user name 6", "3456925865", "3457823894"),
                new LocationRecyclerListData("user name 1", "3457823894", "3457823894"),
                new LocationRecyclerListData("user name 2", "8337823828", "3457823894"),
                new LocationRecyclerListData("user name 3", "3457826485", "3457823894"),
                new LocationRecyclerListData("user name 4", "1434732842", "3457823894"),
                new LocationRecyclerListData("user name 5", "2462823863", "3457823894"),
                new LocationRecyclerListData("user name 6", "3456925865", "3457823894"),
                new LocationRecyclerListData("user name 1", "3457823894", "3457823894"),
                new LocationRecyclerListData("user name 2", "8337823828", "3457823894"),
                new LocationRecyclerListData("user name 3", "3457826485", "3457823894"),
                new LocationRecyclerListData("user name 4", "1434732842", "3457823894"),
                new LocationRecyclerListData("user name 5", "2462823863", "3457823894"),
                new LocationRecyclerListData("user name 6", "3456925865", "3457823894"),
                new LocationRecyclerListData("user name 1", "3457823894", "3457823894"),
                new LocationRecyclerListData("user name 2", "8337823828", "3457823894"),
                new LocationRecyclerListData("user name 3", "3457826485", "3457823894"),
                new LocationRecyclerListData("user name 4", "1434732842", "3457823894"),
                new LocationRecyclerListData("user name 5", "2462823863", "3457823894"),
                new LocationRecyclerListData("user name 6", "3456925865", "3457823894"),
                new LocationRecyclerListData("user name 1", "3457823894", "3457823894"),
                new LocationRecyclerListData("user name 2", "8337823828", "3457823894"),
                new LocationRecyclerListData("user name 3", "3457826485", "3457823894"),
                new LocationRecyclerListData("user name 4", "1434732842", "3457823894"),
                new LocationRecyclerListData("user name 5", "2462823863", "3457823894"),
                new LocationRecyclerListData("user name 6", "3456925865", "3457823894")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.location_recycler_view);
        LocationRecyclerAdapter adapter = new LocationRecyclerAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}