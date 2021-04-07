package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        holder.location.setText(listdata.get(position).getLocation_name());
        holder.latitute.setText(listdata.get(position).getLatitude());
        holder.longitude.setText(listdata.get(position).getLongitude());

        // FACTORY CAN NOT BE DELETED
        if (listdata.get(position).isFactory) {
            holder.delete.setEnabled(false);
            holder.delete.setVisibility(View.INVISIBLE);
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext(), AdminLocationAdd.class);
                User.isEdit = true;
                User.editData = listdata.get(position).locationData;
                view.getContext().startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirmation Dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteLocation(view, position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure?");
                alert.setMessage("Do you want to delete this Location?");
                alert.show();
            }
        });
    }

    public void removeAt(int position) {
        listdata.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listdata.size());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView edit;
        public TextView delete;
        public TextView location;
        public TextView latitute;
        public TextView longitude;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit =  itemView.findViewById(R.id.edit_recycler_location);
            this.delete =  itemView.findViewById(R.id.delete_recycler_location);
            this.location = (TextView) itemView.findViewById(R.id.location_recycler);
            this.latitute = (TextView) itemView.findViewById(R.id.latitude_recycler);
            this.longitude = (TextView) itemView.findViewById(R.id.longitute_recycler);
        }
    }

    private void deleteLocation(View view, int position){
        String longitude = listdata.get(position).getLongitude();
        String latitude = listdata.get(position).getLatitude();
        Map<String,String> param = new HashMap<>();

        param.put("latitude",latitude);
        param.put("longitude",longitude);

        StringRequester.getData((Activity) view.getContext(),Constants.LOCATIONS_DELETE_URL, param, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                Toast.makeText(view.getContext(),result.getString("message"),Toast.LENGTH_SHORT).show();
                removeAt(position);
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(view.getContext(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
