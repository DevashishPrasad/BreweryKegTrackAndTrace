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

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{
    private ArrayList<UserRecyclerListData> listdata;

    // RecyclerView recyclerView;
    public UserRecyclerAdapter(ArrayList<UserRecyclerListData> listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.user_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }
    public void removeAt(int position) {
        listdata.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listdata.size());
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserRecyclerListData myListData = listdata.get(position);
        holder.username.setText(listdata.get(position).getUsername());
        holder.mobileno.setText(listdata.get(position).getMobileno());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(view.getContext(), AdminUserAdd.class);
                User.isEdit = true;
                User.editData = listdata.get(position).userData;
                view.getContext().startActivity(intent);

//                Toast.makeText(view.getContext(),"clicked on edit",Toast.LENGTH_LONG).show();
            }
        });

        // TO DELETE THE USER
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = listdata.get(position).getMobileno();
                Map<String,String> param = new HashMap<>();
                param.put("mobile",mobile);

                StringRequester.getData((Activity) view.getContext(),Constants.USER_DELETE_URL, param, new VolleyCallback() {
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
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView edit;
        public ImageView delete;
        public TextView mobileno;
        public TextView username;
        public ViewHolder(View itemView) {
            super(itemView);
            this.edit = (ImageView) itemView.findViewById(R.id.edit_recycler_user);
            this.delete = (ImageView) itemView.findViewById(R.id.delete_recycler_user);
            this.mobileno = (TextView) itemView.findViewById(R.id.mobileno_recycler);
            this.username = (TextView) itemView.findViewById(R.id.username_recycler);
        }
    }
}
