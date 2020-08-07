package com.example.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.food.RemoteService.BASE_URL;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit;
    RemoteService remoteService;
    List<FoodVO> arrayFood = new ArrayList<>();
    FoodAdapter foodAdapter = new FoodAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("맛집관리");

        ListView listFood = findViewById(R.id.listFood);
        listFood.setAdapter(foodAdapter);

        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        remoteService = retrofit.create(RemoteService.class);

        Call<List<FoodVO>> call = remoteService.listFood();
        call.enqueue(new Callback<List<FoodVO>>() {
            @Override
            public void onResponse(Call<List<FoodVO>> call, Response<List<FoodVO>> response) {
                arrayFood = response.body();
                foodAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<FoodVO>> call, Throwable t) {

            }
        });

        Button btnLocation =findViewById(R.id.btnLocation);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CurrentActivity.class);
                intent.putExtra("latitude",36.7989522);
                intent.putExtra("longitude",127.072742);
                startActivity(intent);
            }
        });
    }

    class FoodAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayFood.size();
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
            view = getLayoutInflater().inflate(R.layout.item_food, viewGroup, false);

            TextView txtName = view.findViewById(R.id.txtName);
            TextView txtAddress = view.findViewById(R.id.txtAddress);
            TextView txtTel = view.findViewById(R.id.txtTel);

            final FoodVO vo = arrayFood.get(i);
            txtName.setText(vo.getName());
            txtAddress.setText(vo.getAddress());
            txtTel.setText(vo.getTel());

            ImageView btnLocation = view.findViewById(R.id.btnLocation);
            btnLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("latitude",vo.getLatitude());
                    intent.putExtra("longitude",vo.getLongitude());
                    intent.putExtra("name",vo.getName());
                    intent.putExtra("tel",vo.getTel());
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}