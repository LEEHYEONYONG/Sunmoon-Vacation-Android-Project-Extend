package com.example.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    Button btnRegister, btnLogin, btnCancel;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("로그인");

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edtEmail.getText().toString();
                String strPassword = edtPassword.getText().toString();
                registerUser(strEmail,strPassword);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = edtEmail.getText().toString();
                String strPassword = edtPassword.getText().toString();
                if(strEmail.indexOf('@')<0){
                    Toast.makeText(MainActivity.this,"이메일형식이 아닙니다.",Toast.LENGTH_SHORT).show();
                }else if(strPassword.length()<8){
                    Toast.makeText(MainActivity.this,"비밀번호를 8자리 이상 입력하세요.",Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(strEmail,strPassword);
                }

            }
        });
    }
/*
    public void registerUser(String strEmail, String strPassword){
        mAuth.createUserWithEmailAndPassword(this,new MediaPlayer.OnCompletionListener<AuthResult>(){
            @Override
            public void onComplete
        });
    }

 */

    public void registerUser(final String strEmail, String strPassword){
        mAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this, "등록성공", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "등록실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginUser(final String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"로그인성공",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"로그인실패",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}