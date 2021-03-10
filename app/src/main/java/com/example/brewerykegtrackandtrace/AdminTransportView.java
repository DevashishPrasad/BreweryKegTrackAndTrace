package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminTransportView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_transport_view);

        User.goHome(AdminTransportView.this);

        TransportRecyclerListData[] myListData = new TransportRecyclerListData[] {
                new TransportRecyclerListData("user name 1", "3457823894"),
                new TransportRecyclerListData("user name 2", "8337823828"),
                new TransportRecyclerListData("user name 3", "3457826485"),
                new TransportRecyclerListData("user name 4", "1434732842"),
                new TransportRecyclerListData("user name 5", "2462823863"),
                new TransportRecyclerListData("user name 6", "3456925865"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888"),
                new TransportRecyclerListData("user name 7", "9119423888")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.transport_recycler_view);
        TransportRecyclerAdapter adapter = new TransportRecyclerAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void goToAddTransport(View view) {
        Intent i = new Intent(this, AdminTransportAdd.class);
        startActivity(i);
    }
}