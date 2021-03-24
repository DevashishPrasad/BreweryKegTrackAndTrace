package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TransportRecyclerAdapter extends RecyclerView.Adapter<TransportRecyclerAdapter.ViewHolder>{
    private ArrayList<TransportRecyclerListData> listdata;

    // RecyclerView recyclerView;
    public TransportRecyclerAdapter(ArrayList<TransportRecyclerListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public TransportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.transport_recycler_item, parent, false);
        TransportRecyclerAdapter.ViewHolder viewHolder = new TransportRecyclerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransportRecyclerAdapter.ViewHolder holder, int position) {
        final TransportRecyclerListData myListData =listdata.get(position);
        holder.vehiclename.setText(listdata.get(position).getVehiclename());
        holder.vehicleno.setText(listdata.get(position).getVehicleno());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;

                intent = new Intent(view.getContext(), AdminTransportAdd.class);
                User.isEdit = true;
                User.editData = listdata.get(position).tranData;
                view.getContext().startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListData.deleteTransport(listdata.get(position).getVehicleno());

                String trans_rn = listdata.get(position).getVehicleno();
                Map<String,String> param = new HashMap<>();
                param.put("trans_rn",trans_rn);

                StringRequester.getData((Activity) view.getContext(),Constants.TRANSPORTS_DELETE_URL, param, new VolleyCallback() {
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

                Toast.makeText(view.getContext(),trans_rn + " deleted",Toast.LENGTH_LONG).show();
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
        public ImageView edit;
        public ImageView delete;
        public TextView vehicleno;
        public TextView vehiclename;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageView) itemView.findViewById(R.id.edit_recycler_transport);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_recycler_transport);
            this.vehicleno = (TextView) itemView.findViewById(R.id.vehicleno_recycler);
            this.vehiclename = (TextView) itemView.findViewById(R.id.vehiclename_recycler);
        }
    }
}
