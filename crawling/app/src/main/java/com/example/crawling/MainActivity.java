package com.example.crawling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ArrayList<HashMap<String,String>> arrayCGV = new ArrayList<>();
    CGVAdapter cgvAdapter;
    ArrayList<HashMap<String,String>> arrayDaum = new ArrayList<>();
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CGVThread().execute();
        new DaumThread().execute();
    }

    class CGVThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try{
                Document doc= Jsoup.connect("http://www.cgv.co.kr/movies").get();
                Elements elements=doc.select(".sect-movie-chart ol");
                for(Element e:elements.select("li")){
                    HashMap<String, String> map=new HashMap<String, String>();
                    map.put("rank", e.select(".rank").text());
                    map.put("title", e.select(".title").text());
                    map.put("image", e.select("img").attr("src"));
                    /*
                    map.put("percent", e.select(".percent span").text());
                    map.put("link", e.select(".box-image a").attr("href"));

                     */
                    if(!e.select(".rank").text().equals("")){
                        arrayCGV.add(map);
                    }
                }
            }catch(Exception e){
                System.out.println(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("데이터갯수: " + arrayCGV.size());
            RecyclerView listCGV = findViewById(R.id.listCGV);
            listCGV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            cgvAdapter = new CGVAdapter(MainActivity.this,arrayCGV);
            listCGV.setAdapter(cgvAdapter);
        }
    }

    //다음 날씨 스레드
    class DaumThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                HashMap<String, Object> object=new HashMap<String, Object>();
                Document doc=Jsoup.connect("http://www.daum.net").get();
                Elements elements=doc.select(".list_weather");
                for(Element e:elements.select("li")) {
                    HashMap<String, String> map=new HashMap<String, String>();
                    map.put("part", e.select(".txt_part").text());
                    map.put("temper", e.select(".txt_temper").text());
                    //map.put("wa", e.select(".ir_wa").text());
                    map.put("ico", e.select(".ico_ws").text());
                    arrayDaum.add(map);
                }
            }catch(Exception e) {
                System.out.println(e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            System.out.println("다음날씨데이터갯수:"+ arrayDaum.size());
            getSupportActionBar().setTitle("다음날씨");

            BackThread backThread = new BackThread();
            backThread.setDaemon(true);
            backThread.start();
        }
    }

    class BackThread extends Thread{
        @Override
        public void run() {
            super.run();
            index=0;
            while(true){
                handler.sendEmptyMessage(0);
                index++;
                if(index==arrayDaum.size()){
                    index=0;
                }
                try{Thread.sleep(2000);}catch(Exception e){}
            }
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String part=arrayDaum.get(index).get("part");
            String temper=arrayDaum.get(index).get("temper");
            //String wa=arrayDaum.get(index).get("wa");
            String ico=arrayDaum.get(index).get("ico");
            //txtWeather.setText(part + temper + wa + ico);
            getSupportActionBar().setTitle(part +": "+ temper +"도/" + ico);
        }
    };
}