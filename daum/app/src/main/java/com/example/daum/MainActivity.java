package com.example.daum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String query="설현";
    String url="https://dapi.kakao.com/v2/search/image";
    int size =10;
    MyAdapter ad;
    ListView list;
    //ProgressDialog progress;
    ProgressBar progressBar;

    ArrayList<HashMap<String,String>> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar =findViewById(R.id.progress);

        getSupportActionBar().setTitle("이미지검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.list);

        new DaumThread().execute();

        FloatingActionButton more =findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size +=10;
                new DaumThread().execute();
            }
        });
    }

    //다음스레드
    class DaumThread extends AsyncTask<String,String,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progress=new ProgressDialog(MainActivity.this);
            //progress.setMessage("불러오는 중입니다.");
            //progress.show();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = Daum.connect(url+ "?query="+query+"&size="+size);
            System.out.println(result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parser(s);
            System.out.println("데이터갯수 : "+array.size());
            ad=new MyAdapter();
            list.setAdapter(ad);
            //progress.dismiss();
            progressBar.setVisibility(View.INVISIBLE);

            super.onPostExecute(s);
        }
    }


    //parsorJSON
    public void parser(String result){
        array = new ArrayList<>();
        try{
            JSONArray jArray = new JSONObject(result).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();
                map.put("name",obj.getString("display_sitename"));
                map.put("thumb",obj.getString("thumbnail_url"));
                map.put("link",obj.getString("doc_url"));
                map.put("image",obj.getString("image_url"));

                array.add(map);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //어댑터
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
            view=getLayoutInflater().inflate(R.layout.item_image,null);
            TextView name = view.findViewById(R.id.name);
            ImageView thumb = view.findViewById(R.id.thumb);

            final HashMap<String,String> map = array.get(i);
            name.setText(map.get("name"));

            Picasso.with(MainActivity.this).load(map.get("thumb")).into(thumb);

            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_bigimage,null);

                    ImageView bigImage = layout.findViewById(R.id.bigimage);
                    Picasso.with(MainActivity.this).load(map.get("image")).into(bigImage);

                    AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                    box.setTitle("이미지");
                    box.setPositiveButton("확인",null);
                    box.setView(layout);
                    box.show();
                }
            });


            return view;


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                query=s;
                size=10;
                new DaumThread().execute();
                //progress.dismiss();
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
        Intent intent;
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.image:
                break;
            case R.id.blog:
                break;
            case R.id.local:
                intent= new Intent(MainActivity.this,LocalActivity.class);
                startActivity(intent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }
}