package com.example.chat;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ArrayList<Chat> arrayChat;
    Context context;
    String strEmail;

    public ChatAdapter(Context context, ArrayList<Chat> arrayChat, String strEmail) {
        this.strEmail = strEmail;
        this.context = context;
        this.arrayChat = arrayChat;
    }

    //Context context;
    //ArrayList<Chat> arrayChat;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String email = arrayChat.get(position).getEmail();

        LinearLayout.LayoutParams prmContent = (LinearLayout.LayoutParams)holder.txtContent.getLayoutParams();
        LinearLayout.LayoutParams prmWdate = (LinearLayout.LayoutParams)holder.txtDate.getLayoutParams();
        LinearLayout.LayoutParams prmEmail = (LinearLayout.LayoutParams)holder.txtEmail.getLayoutParams();

        //Log.i("Email",context.toString() + ":" + arrayChat.get(position).getEmail());

        if(strEmail.equals(email)){
            holder.txtContent.setTextColor(Color.RED);
            prmContent.gravity = Gravity.RIGHT;
            prmWdate.gravity = Gravity.RIGHT;
            prmEmail.gravity = Gravity.RIGHT;
            holder.txtEmail.setVisibility(View.GONE);

        }else{
            holder.txtContent.setTextColor(Color.BLUE);
            prmContent.gravity = Gravity.LEFT;
            prmWdate.gravity = Gravity.LEFT;
            prmEmail.gravity = Gravity.LEFT;
        }
        /*
        if(context.equals(arrayChat.get(position).getEmail())){
            prmContent.gravity = Gravity.RIGHT;
            prmWdate.gravity = Gravity.RIGHT;
        }else{
            prmContent.gravity = Gravity.LEFT;
            prmWdate.gravity = Gravity.LEFT;
        }

        holder.txtDate.setLayoutParams(prmWdate);
        holder.txtContent.setLayoutParams(prmContent);

        */

        holder.txtDate.setText(arrayChat.get(position).getWdate());
        holder.txtContent.setText(arrayChat.get(position).getContent());
        holder.txtEmail.setText(arrayChat.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return arrayChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent, txtDate, txtEmail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate=itemView.findViewById(R.id.txtDate);
            txtContent=itemView.findViewById(R.id.txtContent);
            txtEmail=itemView.findViewById(R.id.txtEmail);
        }
    }
}
