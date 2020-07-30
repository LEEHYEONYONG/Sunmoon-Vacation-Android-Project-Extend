package com.example.ex12;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class BookFragment extends Fragment {

    EditText editSearch;
    String url = "https://dapi.kakao.com/v3/search/book?target=title";
    String query = "안드로이드";
    int size = 10;
    ArrayList<HashMap<String, String>> array;
    BookAdapter bookAdapter;
    RecyclerView bookList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        editSearch = view.findViewById(R.id.editSearch);
        editSearch.setHint("도서검색어");
        new KakaoThread().execute();

        ImageView btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = size+10;
                new BookFragment.KakaoThread().execute();
            }
        });

        ImageView btnSearch = view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                query=editSearch.getText().toString();
                size=10;
                new BookFragment.KakaoThread().execute();
            }
        });

        return view;
    }

    //카카오 스레드
    class KakaoThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String s = Kakao.connect(url + "&query=" + query + "&size=" + size);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bookParser(s);
            System.out.println("도서데이터: " + array.size());
            bookList = getActivity().findViewById(R.id.listBook);
            bookAdapter = new BookAdapter();
            bookList.setAdapter(bookAdapter);
            bookList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    //bookparser
    public void bookParser(String s) {
        array = new ArrayList<>();

        try {
            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);

                HashMap<String, String> map = new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("thumbnail", obj.getString("thumbnail"));
                //System.out.println("상태2: " + obj.getString("thumbnail") );
                map.put("publisher", obj.getString("publisher"));
                map.put("price", obj.getString("price"));

                array.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }
    }

    //BookAdapter
    class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String,String> map =array.get(position);

            holder.title.setText(map.get("title"));
            holder.publisher.setText(map.get("publisher"));
            holder.price.setText(map.get("price"));


            //System.out.println("상태:" + getActivity());
            Picasso.with(getActivity()).load(map.get("thumbnail")).into(holder.thumbnail);


        }

        @Override
        public int getItemCount() {
            return array.size();
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