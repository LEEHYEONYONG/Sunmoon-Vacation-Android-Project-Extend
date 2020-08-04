package com.example.crawling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CGVAdapter extends RecyclerView.Adapter<CGVAdapter.ViewHolder> {
    Context context;
    ArrayList<HashMap<String,String>> arrayCGV;



    public CGVAdapter(Context context, ArrayList<HashMap<String, String>> arrayCGV) {
        this.context = context;
        this.arrayCGV = arrayCGV;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cgv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rank.setText(arrayCGV.get(position).get("rank"));
        holder.title.setText(arrayCGV.get(position).get("title"));
        Picasso.with(context).load(arrayCGV.get(position).get("image")).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return arrayCGV.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rank,title;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rank=itemView.findViewById(R.id.rank);
            title=itemView.findViewById(R.id.title);
            image=itemView.findViewById(R.id.image);
        }
    }
}
