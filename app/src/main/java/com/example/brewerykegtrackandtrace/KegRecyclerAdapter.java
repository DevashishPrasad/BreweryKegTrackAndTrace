package com.example.brewerykegtrackandtrace;

import android.app.Activity;
import android.content.DialogInterface;
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
        holder.kegid.setText(listdata.get(position).getAss_name());
        holder.kegtype.setText(listdata.get(position).getKegtype());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Confirmation Dialog box
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteKeg(view, position);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle("Are you sure?");
                alert.setMessage("Do you want to delete this Keg?");
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
        public TextView kegid;
        public TextView kegtype;
        public ViewHolder(View itemView) {
            super(itemView);
            this.delete = (TextView) itemView.findViewById(R.id.delete_recycler_keg);
            this.kegid = (TextView) itemView.findViewById(R.id.kegid_recycler);
            this.kegtype = (TextView) itemView.findViewById(R.id.kegtype_recycler);
        }
    }

    private void deleteKeg(View view, int position){
        String ass_tag = listdata.get(position).getAss_tag();
        String ass_name = listdata.get(position).getAss_name();
        Map<String,String> param = new HashMap<>();

        param.put("ass_name",ass_name);
        param.put("ass_tag",ass_tag);

        StringRequester.getData((Activity) view.getContext(),Constants.ASSETS_DELETE_URL, param, new VolleyCallback() {
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

        Toast.makeText(view.getContext(),ass_name+" Deleted: ",Toast.LENGTH_LONG).show();
    }
}

