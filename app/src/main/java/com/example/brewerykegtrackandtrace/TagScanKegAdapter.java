package com.example.brewerykegtrackandtrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagScanKegAdapter extends RecyclerView.Adapter<TagScanKegAdapter.ViewHolder>{
    private ArrayList<TagScanKegListData> listdata;

    // RecyclerView recyclerView;
    public TagScanKegAdapter(ArrayList<TagScanKegListData> listdata) {
            this.listdata = listdata;
            }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.tag_scan_tabitem, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TagScanKegListData myListData = listdata.get(position);
        holder.datetime.setText(listdata.get(position).getDatetime());
        holder.keg_id.setText(listdata.get(position).getKeg_id());
        holder.status.setText(listdata.get(position).getStatus());
    }



    @Override
    public int getItemCount() {
            return listdata.size();
            }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView datetime;
        public TextView keg_id;
        public TextView status;
        public ViewHolder(View itemView) {
            super(itemView);
            this.datetime =  itemView.findViewById(R.id.datetime);
            this.keg_id =  itemView.findViewById(R.id.keg_id);
            this.status =  itemView.findViewById(R.id.status);
        }
    }
}

