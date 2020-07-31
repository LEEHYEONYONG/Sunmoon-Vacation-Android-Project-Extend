package com.example.ex13;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;
import java.util.HashMap;

public class BlogFragment extends Fragment {
    EditText edtSearch;
    ImageView btnSearch;
    FloatingActionButton btnMore;

    String url="https://dapi.kakao.com/v2/search/blog";
    String query="선문대";
    int page=1;
    int total=0;//검색건수
    boolean is_end;//페이지의 마지막

    BlogAdapter blogAdapter = new BlogAdapter();
    RecyclerView listBlog;
    TextView txtTotal;

    ArrayList<HashMap<String,String>> arrayBlog = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        edtSearch=view.findViewById(R.id.edtSearch);
        edtSearch.setHint("블로그검색어");
        txtTotal=view.findViewById(R.id.txtTotal);
        btnSearch=view.findViewById(R.id.btnSearch);
        btnMore=view.findViewById(R.id.btnMore);

        listBlog=view.findViewById(R.id.listBlog);
        listBlog.setLayoutManager(new LinearLayoutManager(getActivity()));
        listBlog.setAdapter(blogAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayBlog.clear();
                query=edtSearch.getText().toString();
                page=1;
                new KakaoThread().execute();
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(is_end){
                    Toast.makeText(getActivity(),"마지막페이지입니다.",Toast.LENGTH_SHORT).show();
                }else{
                    page++;
                    new KakaoThread().execute();

                }
            }
        });
        new KakaoThread().execute();
        return view;
    }

    //카카오접속 스레드
    class KakaoThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s=Kakao.connect(url+"?query="+query+"&page="+page);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            blogParsing(s);
            blogAdapter.notifyDataSetChanged();
            listBlog.scrollToPosition(arrayBlog.size()-1);
            txtTotal.setText("검색수:"+total + "/마지막페이지:"+ is_end);
            System.out.println("블로그검색수:"+arrayBlog.size());
        }
    }

    public void blogParsing(String s){
        try{
            JSONObject jObject = new JSONObject(s).getJSONObject("meta");
            total= jObject.getInt("total_count");
            is_end=jObject.getBoolean("is_end");

            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);
                HashMap<String,String> map = new HashMap<>();
                map.put("title",obj.getString("title"));
                map.put("url",obj.getString("url"));
                arrayBlog.add(map);
            }
        }catch (Exception e){

        }
    }

    class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{

        @NonNull
        @Override
        public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_blog,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
            final HashMap<String,String> map =arrayBlog.get(position);
            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.link.setText(map.get("url"));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",map.get("url"));
                    intent.putExtra("title",map.get("title"));
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayBlog.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,link;
            LinearLayout layout;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                link=itemView.findViewById(R.id.link);
                layout=itemView.findViewById(R.id.layout);
            }
        }
    }
}