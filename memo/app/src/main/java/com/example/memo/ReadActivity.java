package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadActivity extends AppCompatActivity {

    Memo memo = new Memo();
    EditText edtContent;
    String strKey,strContent;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        getSupportActionBar().setTitle("메모읽기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        strKey=intent.getStringExtra("key");
        strContent=intent.getStringExtra("content");
        memo.setKey(strKey);
        memo.setContent(strContent);
        memo.setCreateDate(intent.getStringExtra("createDate"));
        memo.setUpdateDate(intent.getStringExtra("updateDate"));

        edtContent=findViewById(R.id.edtContent);
        edtContent.setText(strContent);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("memos/"+user.getUid()+"/"+strKey);

        FloatingActionButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("질의");
                box.setMessage("수정하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        strContent=edtContent.getText().toString();
                        memo.setContent(strContent);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        memo.setUpdateDate(sdf.format(new Date()));
                        myRef.setValue(memo);
                        setResult(RESULT_OK);
                        finish();
                    }
                });
                box.setNegativeButton("아니오",null);
                box.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
            case R.id.itemDelete:
                AlertDialog.Builder box = new AlertDialog.Builder(this);
                box.setTitle("질의");
                box.setMessage("삭제하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myRef.removeValue();
                        finish();
                    }
                });
                box.setNegativeButton("아니오",null);
                box.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read,menu);
        return super.onCreateOptionsMenu(menu);
    }
}