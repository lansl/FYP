package com.example.lzyang.fyptest;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lzyang.fyptest.Entity.EmergencyCard;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    ArrayList<EmergencyCard> arrayList = new ArrayList<>();

    public RecyclerAdapter() {
    }

    public void setArrayList(ArrayList<EmergencyCard> arrayList) {
        this.arrayList = arrayList;
    }

    public void refreshRecyclerAdapter(){
        RecyclerAdapter.super.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_child,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.img.setImageBitmap(arrayList.get(position).getImg());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.location_address.setText(arrayList.get(position).getLocation_address());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView title, location_address;
        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.ess_img);
            title = (TextView)itemView.findViewById(R.id.ess_title);
            location_address = (TextView)itemView.findViewById(R.id.ess_location_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent_detail = new Intent(v.getContext(), DetailActivity.class);
            intent_detail.putExtra("Position",getAdapterPosition());
            v.getContext().startActivity(intent_detail);
        }
    }
}
