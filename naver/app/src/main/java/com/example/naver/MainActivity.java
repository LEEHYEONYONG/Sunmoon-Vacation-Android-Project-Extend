package com.example.naver;

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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ArrayList<HashMap<String,String>> array;
    MyAdapter ad;
    ListView list;
    ProgressDialog progressDialog;
    int display = 5;
    String query ="안드로이드";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map = array.get(i);
                String title = map.get("title");
                String link = map.get("link");
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("link",link);
                startActivity(intent);
            }
        });

        new NaverThread().execute();

        FloatingActionButton more = findViewById(R.id.more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display = display +5;
                new NaverThread().execute();
            }
        });
    }

    //네이버 접속 스레드
    class NaverThread extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("검색중입니다.");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = Naver.connect(display,query);
            System.out.println("result:"+ result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            parserJSON(s);
            //System.out.println("array 사이즈: " + array.size());
            ad=new MyAdapter();
            list.setAdapter(ad);
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    //결과파싱
    public void parserJSON(String result){
        array=new ArrayList<HashMap<String, String>>();
        try {
            JSONArray jArray = new JSONObject(result).getJSONArray("items");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);
                String title = obj.getString("title");
                String link = obj.getString("link");
                String description = obj.getString("description");

                HashMap<String,String> map = new HashMap<String, String>();
                map.put("title",title);
                map.put("link",link);
                map.put("description",description);
                array.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyAdapter extends BaseAdapter {

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
            view=getLayoutInflater().inflate(R.layout.item_news,null);
            TextView title=view.findViewById(R.id.title);
            TextView description = view.findViewById(R.id.description);

            HashMap<String,String> map = array.get(i);
            title.setText(Html.fromHtml(map.get("title")));
            description.setText(Html.fromHtml(map.get("description")));
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
                display=5;
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