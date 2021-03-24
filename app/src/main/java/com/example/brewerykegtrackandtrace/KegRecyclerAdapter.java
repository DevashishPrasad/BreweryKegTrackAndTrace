package com.example.brewerykegtrackandtrace;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class KegRecyclerAdapter extends RecyclerView.Adapter<KegRecyclerAdapter.ViewHolder>{
    private ArrayList<KegRecyclerListData> listdata;

    // RecyclerView recyclerView;
    public KegRecyclerAdapter(ArrayList<KegRecyclerListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public KegRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.keg_recycler_item, parent, false);
        KegRecyclerAdapter.ViewHolder viewHolder = new KegRecyclerAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(KegRecyclerAdapter.ViewHolder holder, int position) {
        final KegRecyclerListData myListData = listdata.get(position);
        holder.kegid.setText(listdata.get(position).getKegid());
        holder.kegtype.setText(listdata.get(position).getKegtype());
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
        public TextView kegid;
        public TextView kegtype;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageView) itemView.findViewById(R.id.edit_recycler_keg);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_recycler_keg);
            this.kegid = (TextView) itemView.findViewById(R.id.kegid_recycler);
            this.kegtype = (TextView) itemView.findViewById(R.id.kegtype_recycler);
        }
    }
}

