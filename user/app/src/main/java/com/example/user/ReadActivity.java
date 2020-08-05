package com.example.user;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.user.RemoteService.BASE_URL;

public class ReadActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService remoteService;
    EditText edtId, edtName, edtPassword;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("사용자수정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService=retrofit.create(RemoteService.class);
        intent = getIntent();
        Call<UserVO> call = remoteService.readUser(intent.getStringExtra("id"));
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                UserVO userVO = response.body();
                edtId.setText(userVO.getId());
                edtId.setEnabled(false);
                edtName.setText(userVO.getName());
                edtPassword.setText(userVO.getPassword());
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {

            }
        });

        FloatingActionButton btnSave=findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(ReadActivity.this);
                box.setTitle("질의");
                box.setMessage("수정하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        UserVO vo= new UserVO();
                        vo.setId(edtId.getText().toString());
                        vo.setName(edtName.getText().toString());
                        vo.setPassword(edtPassword.getText().toString());

                        Call<Void> call = remoteService.updateUser(vo.getId(),vo.getName(),vo.getPassword());
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                setResult(RESULT_OK);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
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
                break;
            case R.id.itemDelete:
                AlertDialog.Builder box = new AlertDialog.Builder(this);
                box.setTitle("질의");
                box.setMessage("삭제하실래요?");

                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call call = remoteService.deleteUser(intent.getStringExtra("id"));
                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                setResult(3);
                                finish();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {

                            }
                        });
                    }
                });
                box.setNegativeButton("아니오",null);
                box.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return super.onCreateOptionsMenu(menu);
    }
}