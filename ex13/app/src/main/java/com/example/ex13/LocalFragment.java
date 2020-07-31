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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalFragment extends Fragment {
    String url="https://dapi.kakao.com/v2/local/search/keyword.json";
    String query="선문대학교";
    int page=1;
    int total=0;
    boolean is_end;

    TextView txtTotal;
    EditText edtSearch;
    ImageView btnSearch;
    FloatingActionButton btnMore;
    RecyclerView listLocal;

    LocalAdapter localAdapter = new LocalAdapter();

    ArrayList<HashMap<String,String>> arrayLocal = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        edtSearch=view.findViewById(R.id.edtSearch);
        edtSearch.setHint("지역검색어");

        txtTotal = view.findViewById(R.id.txtTotal);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnMore = view.findViewById(R.id.btnMore);

        listLocal = view.findViewById(R.id.listLocal);
        listLocal.setLayoutManager(new LinearLayoutManager(getActivity()));
        listLocal.setAdapter(localAdapter);

        new KakaoThread().execute();
        return view;
    }

    class KakaoThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s = Kakao.connect(url + "?query=" + query + "&page=" + page);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LocalParsing(s);
            localAdapter.notifyDataSetChanged();
            txtTotal.setText("검색수:"+total + "/마지막페이지:"+ is_end);
            System.out.println("지역검색수:"+arrayLocal.size());

        }
    }

    public void LocalParsing(String s){
        try{
            JSONObject jObject = new JSONObject(s).getJSONObject("meta");
            total= jObject.getInt("total_count");
            is_end=jObject.getBoolean("is_end");

            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);
                HashMap<String,String> map = new HashMap<>();
                map.put("place_name",obj.getString("place_name"));
                map.put("road_address_name",obj.getString("road_address_name"));
                map.put("phone",obj.getString("phone"));
                map.put("x",obj.getString("x"));
                map.put("y",obj.getString("y"));
                arrayLocal.add(map);
            }
        }catch(Exception e){

        }

    }

    class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder>{

        @NonNull
        @Override
        public LocalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_local,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocalAdapter.ViewHolder holder, int position) {
            final HashMap<String,String> map =arrayLocal.get(position);
            holder.txtName.setText(Html.fromHtml(map.get("place_name")));
            holder.txtAddress.setText(Html.fromHtml(map.get("road_address_name")));
            holder.txtTel.setText(map.get("phone"));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("place_name",map.get("place_name"));
                    intent.putExtra("road_address_name",map.get("road_address_name"));
                    intent.putExtra("phone",map.get("phone"));

                    intent.putExtra("x",map.get("x"));
                    intent.putExtra("y",map.get("y"));


                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayLocal.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtName;
            TextView txtAddress;
            TextView txtTel;
            ImageView image;
            RelativeLayout layout;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtName=itemView.findViewById(R.id.name);
                txtAddress=itemView.findViewById(R.id.address);
                txtTel=itemView.findViewById(R.id.tel);
                layout=itemView.findViewById(R.id.layout);

            }
        }
    }
}