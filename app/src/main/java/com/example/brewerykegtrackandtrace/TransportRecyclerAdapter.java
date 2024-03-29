package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                // Confirmation Dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteTransport(view, position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure?");
                alert.setMessage("Do you want to delete this Transport?");
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
        public TextView vehicleno;
        public TextView vehiclename;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (TextView) itemView.findViewById(R.id.edit_recycler_transport);
            this.delete = (TextView) itemView.findViewById(R.id.delete_recycler_transport);
            this.vehicleno = (TextView) itemView.findViewById(R.id.vehicleno_recycler);
            this.vehiclename = (TextView) itemView.findViewById(R.id.vehiclename_recycler);
        }
    }

    private void deleteTransport(View view, int position){

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
}
