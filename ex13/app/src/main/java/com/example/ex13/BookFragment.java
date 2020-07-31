package com.example.ex13;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class BookFragment extends Fragment {
    EditText edtSearch;
    TextView txtTotal;
    ImageView btnSearch;
    FloatingActionButton btnMore;
    RecyclerView listBook;

    String url="https://dapi.kakao.com/v3/search/book?target=title";
    String query="안드로이드";
    int page=1;
    int total=0;
    boolean is_end;
    ArrayList<HashMap<String,String>> arrayBook = new ArrayList<>();
    BookAdapter bookAdapter = new BookAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        edtSearch=view.findViewById(R.id.edtSearch);
        edtSearch.setHint("책검색어");
        txtTotal=view.findViewById(R.id.txtTotal);
        btnSearch=view.findViewById(R.id.btnSearch);
        btnMore=view.findViewById(R.id.btnMore);

        listBook=view.findViewById(R.id.listBook);
        listBook.setLayoutManager(new LinearLayoutManager(getActivity()));
        listBook.setAdapter(bookAdapter);

        new KakaoThread().execute();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayBook.clear();
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

        return view;
    }

    //카카오 스레드
    class KakaoThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s=Kakao.connect(url + "&query="+query +"&page="+page);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bookParsing(s);
            bookAdapter.notifyDataSetChanged();
            txtTotal.setText("검색수:"+total+"/ 마지막페이지:"+is_end);
            System.out.println("도서데이터갯수: "+arrayBook.size());
        }
    }

    //도서 파싱
    public void bookParsing(String s){
        try{
            JSONObject jObject = new JSONObject(s).getJSONObject("meta");
            total= jObject.getInt("total_count");
            is_end=jObject.getBoolean("is_end");

            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);
                HashMap<String,String> map = new HashMap<>();
                map.put("title",obj.getString("title"));
                map.put("contents",obj.getString("contents"));
                map.put("price",obj.getString("price"));
                map.put("publisher",obj.getString("publisher"));
                map.put("thumbnail",obj.getString("thumbnail"));
                arrayBook.add(map);
            }
        }catch (Exception e){

        }
    }

    class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

        @NonNull
        @Override
        public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_book,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
            HashMap<String,String> map = arrayBook.get(position);
            holder.title.setText(map.get("title"));
            holder.price.setText(map.get("price")+"원");
            holder.publisher.setText(map.get("publisher"));

            String thumbnail= map.get("thumbnail");
            if(!thumbnail.equals("")){
                Picasso.with(getActivity()).load(thumbnail).into(holder.thumbnail);
            }
        }

        @Override
        public int getItemCount() {
            return arrayBook.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title,publisher,price;
            ImageView thumbnail;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.title);
                publisher=itemView.findViewById(R.id.publisher);
                price=itemView.findViewById(R.id.price);
                thumbnail=itemView.findViewById(R.id.thumbnail);

            }
        }
    }
}