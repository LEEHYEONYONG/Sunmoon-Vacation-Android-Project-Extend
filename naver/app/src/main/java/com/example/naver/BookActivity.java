package com.example.naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class BookActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> array;
    int display=5;
    String query="안드로이드";
    String url="https://openapi.naver.com/v1/search/book.json";
    MyAdapter ad;
    ListView list;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setTitle("도서검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list = findViewById(R.id.list);

        new NaverThread().execute();

        FloatingActionButton more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                ProgressDialog dialog = new ProgressDialog();
                dialog.show();
                */
                display +=5;
                new NaverThread().execute();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        Intent intent = new Intent(BookActivity.this,MainActivity.class);
        switch (item.getItemId()){
            case android.R.id.home:
                break;
            case R.id.news:
                getSupportActionBar().setTitle("뉴스검색");
                url="https://openapi.naver.com/v1/search/news.json";
                intent.putExtra("url",url);
                intent.putExtra("title","뉴스검색");
                break;
            case R.id.cafe:
                getSupportActionBar().setTitle("카페검색");
                url="https://openapi.naver.com/v1/search/cafearticle.json";
                intent.putExtra("url",url);
                intent.putExtra("title","카페검색");
                break;
            case R.id.blog:
                getSupportActionBar().setTitle("블로그검색");
                url="https://openapi.naver.com/v1/search/blog.json";
                intent.putExtra("url",url);
                intent.putExtra("title","블로그검색");
                break;

        }
        startActivityForResult(intent,1);
        /*
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        */
        return super.onOptionsItemSelected(item);
    }

    //네이버접속스레드
    class NaverThread extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progress=new ProgressDialog(BookActivity.this);
            progress.setMessage("검색중입니다.");
            progress.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result=Naver.connect(display,query,url);
            System.out.println("result: "+ result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            parserJSON(s);
            System.out.println("array 갯수:"+ array.size());
            ad=new MyAdapter();
            list.setAdapter(ad);
            super.onPostExecute(s);
        }
    }

    //parserJSON
    public void parserJSON(String result){
        array=new ArrayList<HashMap<String, String>>();
        try{
            JSONArray jArray = new JSONObject(result).getJSONArray("items");
            for(int i=0;i<jArray.length();i++){
                HashMap<String ,String> map = new HashMap<>();
                JSONObject obj= jArray.getJSONObject(i);
                map.put("title",obj.getString("title"));
                map.put("author",obj.getString("author"));
                map.put("price",obj.getString("price"));
                map.put("image",obj.getString("image"));
                map.put("description",obj.getString("description"));
                map.put("publisher",obj.getString("publisher"));
                map.put("pubdate",obj.getString("pubdate"));

                array.add(map);

            }
        }catch (Exception e){

        }
    }

    //어댑터생성
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
            view= getLayoutInflater().inflate(R.layout.item_book,null);
            TextView title=view.findViewById(R.id.title);
            TextView price=view.findViewById(R.id.price);
            TextView author=view.findViewById(R.id.author);

            final HashMap<String,String> map = array.get(i);
            title.setText(Html.fromHtml(map.get("title")));
            price.setText(map.get("price")+ "원");
            author.setText(Html.fromHtml(map.get("author")));

            ImageView image = view.findViewById(R.id.image);
            Picasso.with(BookActivity.this).load(map.get("image")).into(image);

            ImageView view1 = view.findViewById(R.id.view);
            view1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BookActivity.this,BookDetailActivity.class);
                    intent.putExtra("title",map.get("title"));
                    intent.putExtra("price",map.get("price"));
                    intent.putExtra("author",map.get("author"));
                    intent.putExtra("description",map.get("description"));
                    intent.putExtra("image",map.get("image"));
                    intent.putExtra("publisher",map.get("publisher"));
                    intent.putExtra("pubdate",map.get("pubdate"));
                    startActivity(intent);
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
                display=5;
                query=s;
                new NaverThread().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}