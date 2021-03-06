package com.example.brewerykegtrackandtrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{
    private UserRecyclerListData[] listdata;

    // RecyclerView recyclerView;
    public UserRecyclerAdapter(UserRecyclerListData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserRecyclerListData myListData = listdata[position];
        holder.username.setText(listdata[position].getUsername());
        holder.mobileno.setText(listdata[position].getMobileno());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListData.updateUser(listdata[position].getMobileno());
                Toast.makeText(view.getContext(),"clicked on edit",Toast.LENGTH_LONG).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListData.deleteUser(listdata[position].getMobileno());
                Toast.makeText(view.getContext(),"clicked on delete: ",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView edit;
        public ImageView delete;
        public TextView mobileno;
        public TextView username;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageView) itemView.findViewById(R.id.edit_recycler);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_recycler);
            this.mobileno = (TextView) itemView.findViewById(R.id.mobileno_recycler);
            this.username = (TextView) itemView.findViewById(R.id.username_recycler);
        }
    }
}
