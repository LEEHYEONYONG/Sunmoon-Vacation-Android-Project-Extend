package com.example.daum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalActivity extends AppCompatActivity {
    String url="https://dapi.kakao.com/v2/local/search/keyword.json";
    String query="선문대학교";
    int size =10;
    ArrayList<HashMap<String,String>> array;
    MyAdapter ad;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("지역검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list=findViewById(R.id.list);
        new DaumThread().execute();

        /**/
        FloatingActionButton more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size= size +5;
                new DaumThread().execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query=s;
                size=10;
                new DaumThread().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //카카오 스레드
    class DaumThread extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {
            String result=Daum.connect(url+"?query="+query+"&size="+size);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parser(s);
            System.out.println("데이터갯수:"+ array.size());
            ad=new MyAdapter();
            list.setAdapter(ad);
            super.onPostExecute(s);
        }
    }

    //파서
    public void parser(String result){
        array=new ArrayList<>();
        try{
            JSONArray jArray = new JSONObject(result).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();
                map.put("name",obj.getString("place_name"));
                map.put("address",obj.getString("address_name"));
                map.put("tel",obj.getString("phone"));
                map.put("x",obj.getString("x"));
                map.put("y",obj.getString("y"));

                array.add(map);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return array.size();
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
            view=getLayoutInflater().inflate(R.layout.item_local,null);
            TextView name = view.findViewById(R.id.name);
            TextView tel = view.findViewById(R.id.tel);
            TextView address = view.findViewById(R.id.address);

            final HashMap<String,String> map = array.get(i);
            name.setText(map.get("name"));
            tel.setText(map.get("tel"));
            address.setText(map.get("address"));

            tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri number = Uri.parse("tel:"+ map.get("tel"));
                    Intent intent = new Intent(Intent.ACTION_DIAL,number);
                    startActivity(intent);
                }
            });

            ImageView maps=view.findViewById(R.id.map);
            maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LocalActivity.this,MapsActivity.class);
                    intent.putExtra("x",map.get("x"));//경도
                    intent.putExtra("y",map.get("y"));//위도
                    intent.putExtra("name",map.get("name"));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}