package com.example.memo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MemoActivity extends AppCompatActivity {
    FloatingActionButton btnAdd;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView listMemo;
    MemoAdapter memoAdapter;
    ArrayList<Memo> arrayMemo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        Intent intent = getIntent();
        final String strEmail=intent.getStringExtra("email");
        getSupportActionBar().setTitle("메모장: "+strEmail);

        memoAdapter=new MemoAdapter(arrayMemo,this);
        listMemo=findViewById(R.id.listMemo);
        listMemo.setLayoutManager(new LinearLayoutManager(this));
        listMemo.setAdapter(memoAdapter);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("memos/"+user.getUid());
        readData();

        btnAdd=findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MemoActivity.this,AddActivity.class);
                intent.putExtra("email",strEmail);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show();
        }else if(requestCode==2 && resultCode==RESULT_OK){
            Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show();
        }
        arrayMemo.clear();
        readData();
    }

    public void readData(){
        arrayMemo.clear();
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Memo memo = (Memo)dataSnapshot.getValue(Memo.class);
                memo.setKey(dataSnapshot.getKey());
                arrayMemo.add(memo);
                memoAdapter.notifyDataSetChanged();
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
    }
}