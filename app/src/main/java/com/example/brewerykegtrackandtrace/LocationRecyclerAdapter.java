package com.example.brewerykegtrackandtrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.ViewHolder>{
    private ArrayList<LocationRecyclerListData> listdata;

    // RecyclerView recyclerView;
    public LocationRecyclerAdapter(ArrayList<LocationRecyclerListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public LocationRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.location_recycler_item, parent, false);
        LocationRecyclerAdapter.ViewHolder viewHolder = new LocationRecyclerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LocationRecyclerAdapter.ViewHolder holder, int position) {
        final LocationRecyclerListData myListData = listdata.get(position);
        holder.location.setText(listdata.get(position).getLocation_name());
        holder.latitute.setText(listdata.get(position).getLatitude());
        holder.longitude.setText(listdata.get(position).getLongitude());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"clicked on edit",Toast.LENGTH_LONG).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"clicked on delete: ",Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView edit;
        public ImageView delete;
        public TextView location;
        public TextView latitute;
        public TextView longitude;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageView) itemView.findViewById(R.id.edit_recycler_location);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_recycler_location);
            this.location = (TextView) itemView.findViewById(R.id.location_recycler);
            this.latitute = (TextView) itemView.findViewById(R.id.latitude_recycler);
            this.longitude = (TextView) itemView.findViewById(R.id.longitute_recycler);
        }
    }
}
