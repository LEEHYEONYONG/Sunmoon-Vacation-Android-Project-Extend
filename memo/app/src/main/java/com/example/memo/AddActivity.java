package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    EditText edtContent;
    String strId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        String strEmail = intent.getStringExtra("email");
        getSupportActionBar().setTitle("메모쓰기"+strEmail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtContent = findViewById(R.id.edtContent);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        strId=user.getUid();

        FloatingActionButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strContent=edtContent.getText().toString();
                if(strContent.equals("")){
                    Toast.makeText(AddActivity.this,"내용을 입력하세요!",Toast.LENGTH_SHORT).show();
                }else{
                    Memo memo = new Memo();
                    memo.setContent(strContent);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    memo.setCreateDate(sdf.format(new Date()));
                    database=FirebaseDatabase.getInstance();
                    myRef=database.getReference("memos").child(strId).push();
                    myRef.setValue(memo);

                    setResult(RESULT_OK);
                    finish();

                    //Toast.makeText(AddActivity.this,"저장완료",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}