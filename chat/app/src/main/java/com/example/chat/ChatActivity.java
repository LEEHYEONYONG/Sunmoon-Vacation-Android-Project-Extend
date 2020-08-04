package com.example.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ArrayList<Chat> arrayChat= new ArrayList<>();
    EditText edtContent;
    ImageView btnSend;
    RecyclerView listChat;

    String strEmail;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        strEmail=user.getEmail();
        getSupportActionBar().setTitle("채팅:"+strEmail);

        edtContent = findViewById(R.id.edtContent);
        listChat = findViewById(R.id.listChat);
        chatAdapter = new ChatAdapter(this,arrayChat,strEmail);
        listChat.setLayoutManager(new LinearLayoutManager(this));
        listChat.setAdapter(chatAdapter);



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chats");//
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String strDate=dataSnapshot.getKey();
                Chat chat=dataSnapshot.getValue(Chat.class);
                chat.setWdate(strDate);
                arrayChat.add(chat);

                listChat.scrollToPosition(arrayChat.size()-1);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strContent = edtContent.getText().toString();
                if(strContent.equals("")){
                    Toast.makeText(ChatActivity.this,"내용을 입력하세요!",Toast.LENGTH_SHORT).show();
                }else{
                    Chat chat = new Chat();
                    chat.setEmail(strEmail);
                    chat.setContent(strContent);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = sdf.format(new Date());
                    chat.setWdate(strDate);

                    myRef=database.getReference("chats").child(strDate);
                    myRef.setValue(chat);
                    edtContent.setText("");
                }
            }
        });

    }
}