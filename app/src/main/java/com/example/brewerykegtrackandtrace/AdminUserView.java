package com.example.brewerykegtrackandtrace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class AdminUserView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        UserRecyclerListData[] myListData = new UserRecyclerListData[] {
                new UserRecyclerListData("user name 1", "3457823894"),
                new UserRecyclerListData("user name 2", "8337823828"),
                new UserRecyclerListData("user name 3", "3457826485"),
                new UserRecyclerListData("user name 4", "1434732842"),
                new UserRecyclerListData("user name 5", "2462823863"),
                new UserRecyclerListData("user name 6", "3456925865"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888"),
                new UserRecyclerListData("user name 7", "9119423888")
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        UserRecyclerAdapter adapter = new UserRecyclerAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}