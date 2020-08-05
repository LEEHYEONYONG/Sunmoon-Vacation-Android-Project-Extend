package com.example.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.user.RemoteService.BASE_URL;

public class MainActivity extends AppCompatActivity {
    ListView list;
    UserAdapter userAdapter = new UserAdapter();
    List<UserVO>  arrayUser=new ArrayList<>();
    Retrofit retrofit;
    RemoteService remoteService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("사용자관리");

        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,1);
            }
        });

        list=findViewById(R.id.listUser);
        list.setAdapter(userAdapter);
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService=retrofit.create(RemoteService.class);
        Call<List<UserVO>> call=remoteService.listUser();
        call.enqueue(new Callback<List<UserVO>>() {
            @Override
            public void onResponse(Call<List<UserVO>> call, Response<List<UserVO>> response) {
                arrayUser= response.body();
                userAdapter.notifyDataSetChanged();
                list.setAdapter(userAdapter);
            }

            @Override
            public void onFailure(Call<List<UserVO>> call, Throwable t) {

            }
        });
        userAdapter.notifyDataSetChanged();
    }

    class UserAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayUser.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.item_user, viewGroup,false);
            TextView txtId=view.findViewById(R.id.txtId);
            TextView txtName=view.findViewById(R.id.txtName);
            txtId.setText(arrayUser.get(i).getId());
            txtName.setText(arrayUser.get(i).getName());

            final String strId = arrayUser.get(i).getId();
            ImageView btnRead = view.findViewById(R.id.btnRead);
            btnRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,ReadActivity.class);
                    intent.putExtra("id",strId);
                    startActivityForResult(intent,2);
                }
            });

            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show();

        }else if(requestCode==2 && resultCode==RESULT_OK){
            Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show();
        }else if(requestCode==2 && resultCode==3){
            Toast.makeText(this,"삭제완료",Toast.LENGTH_SHORT).show();
        }
        Call<List<UserVO>> call=remoteService.listUser();
        call.enqueue(new Callback<List<UserVO>>() {
            @Override
            public void onResponse(Call<List<UserVO>> call, Response<List<UserVO>> response) {
                arrayUser= response.body();
                userAdapter.notifyDataSetChanged();
                list.setAdapter(userAdapter);
            }

            @Override
            public void onFailure(Call<List<UserVO>> call, Throwable t) {

            }
        });
        super.onActivityResult(requestCode, resultCode, data);

    }
}