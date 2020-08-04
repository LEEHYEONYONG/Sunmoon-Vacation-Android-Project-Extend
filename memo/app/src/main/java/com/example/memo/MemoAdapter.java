package com.example.memo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    ArrayList<Memo> arrayMemo;
    Context context;


    public MemoAdapter(ArrayList<Memo> arrayMemo, Context context) {
        this.arrayMemo=arrayMemo;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Memo memo=arrayMemo.get(position);
        holder.txtContent.setText(memo.getContent());
        holder.txtDate.setText(memo.getCreateDate());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ReadActivity.class);
                intent.putExtra("key",memo.getKey());
                intent.putExtra("createDate",memo.getCreateDate());
                intent.putExtra("updateDate",memo.getUpdateDate());
                intent.putExtra("content",memo.getContent());
                ((Activity)context).startActivityForResult(intent,2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayMemo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent,txtDate;
        LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.txtContent);
            txtDate = itemView.findViewById(R.id.txtDate);
            item = itemView.findViewById(R.id.item);


        }
    }
}
