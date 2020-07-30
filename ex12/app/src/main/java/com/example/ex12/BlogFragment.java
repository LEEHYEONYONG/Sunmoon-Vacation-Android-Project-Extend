package com.example.ex12;

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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BlogFragment extends Fragment {

    EditText editSearch;
    String url="https://dapi.kakao.com/v2/search/blog";
    String query="선문대학교";
    int size = 10;
    ArrayList<HashMap<String,String>> array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        editSearch = view.findViewById(R.id.editSearch);
        editSearch.setHint("블로그검색어");

        new KakaoThread().execute();

        ImageView btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = size+10;
                new KakaoThread().execute();
            }
        });

        ImageView btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query=editSearch.getText().toString();
                size=10;
                new KakaoThread().execute();
            }
        });
        return view;
    }

    //카카오 스레드
    class KakaoThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s = Kakao.connect(url + "?query="+query+"&size="+size);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            blogParser(s);

            //System.out.println("데이터갯수: " + array.size());


            RecyclerView list = getActivity().findViewById(R.id.listBlog);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
            BlogAdapter ad = new BlogAdapter();
            list.setAdapter(ad);

        }
    }

    //데이터 parse
    public void blogParser(String s){
        array = new ArrayList<>();

        try {
            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();
                map.put("title",obj.getString("title"));
                map.put("link",obj.getString("url"));

                array.add(map);
            }
        } catch (Exception e){

        }

    }
    //블로그 어댑터
    class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder>{

        @NonNull
        @Override
        public BlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BlogAdapter.ViewHolder holder, int position) {
            HashMap<String,String> map =array.get(position);

            holder.title.setText(Html.fromHtml(map.get("title")));
            holder.link.setText(map.get("link"));
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,link;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                link=itemView.findViewById(R.id.link);
            }
        }
    }
}