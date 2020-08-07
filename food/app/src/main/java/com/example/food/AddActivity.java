package com.example.food;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.food.RemoteService.BASE_URL;

public class AddActivity extends AppCompatActivity {
    EditText name,tel,address,edtLatitude,edtLongitude;
    RemoteService remoteService;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("맛집등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.name);
        tel=findViewById(R.id.tel);
        address=findViewById(R.id.address);
        edtLatitude=findViewById(R.id.edtLatitude);
        edtLongitude=findViewById(R.id.edtLongitude);

        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);

        Intent intent = getIntent();
        address.setText(intent.getStringExtra("address"));

        double longitude = intent.getDoubleExtra("longitude",0);
        edtLongitude.setText(longitude + "");
        double latitude = intent.getDoubleExtra("latitude",0);
        edtLatitude.setText(latitude + "");

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(AddActivity.this);
                box.setTitle("질의");
                box.setMessage("저장하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FoodVO vo =new FoodVO();
                        vo.setAddress(address.getText().toString());
                        vo.setName(name.getText().toString());
                        vo.setTel(tel.getText().toString());

                        String strLatitude = edtLatitude.getText().toString();
                        vo.setLatitude(Double.parseDouble(strLatitude));

                        String strLongitude = edtLongitude.getText().toString();
                        vo.setLongitude(Double.parseDouble(strLongitude));

                        if(vo.getName().equals("") || vo.getAddress().equals("")){
                            Toast.makeText(AddActivity.this, "상호명과 전화번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        }else{
                            Call<Void> call = remoteService.insertFood(vo.getName(),vo.getAddress(),vo.getTel(),vo.getLatitude(),vo.getLongitude());
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(AddActivity.this,"저장완료",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(AddActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                box.setNegativeButton("아니오",null);
                box.show();
            }
        });
    }
}