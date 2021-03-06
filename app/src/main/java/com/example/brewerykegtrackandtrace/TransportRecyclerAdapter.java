package com.example.brewerykegtrackandtrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class TransportRecyclerAdapter extends RecyclerView.Adapter<TransportRecyclerAdapter.ViewHolder>{
    private TransportRecyclerListData[] listdata;

    // RecyclerView recyclerView;
    public TransportRecyclerAdapter(TransportRecyclerListData[] listdata) {
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
        final TransportRecyclerListData myListData = listdata[position];
        holder.vehiclename.setText(listdata[position].getVehiclename());
        holder.vehicleno.setText(listdata[position].getVehicleno());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListData.updateTransport(listdata[position].getVehicleno());
                Toast.makeText(view.getContext(),"clicked on edit",Toast.LENGTH_LONG).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myListData.deleteTransport(listdata[position].getVehicleno());
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
