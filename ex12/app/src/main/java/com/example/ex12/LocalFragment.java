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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class LocalFragment extends Fragment {

    EditText editSearch;
    //String url="https://dapi.kakao.com/v2/search/keyword";
    String url="https://dapi.kakao.com/v2/local/search/keyword";
    String query="선문대학교";
    int size = 5;
    ArrayList<HashMap<String,String>> array;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        editSearch = view.findViewById(R.id.editSearch);
        editSearch.setHint("지역검색어");

        new KakaoThread().execute();

        ImageView btnMore = view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = size+5;
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

    class KakaoThread extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String s = Kakao.connect(url+"?query="+query+"&size="+size);
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            localParser(s);

            //System.out.println("지역데이터갯수: " + array.size());

            RecyclerView list = getActivity().findViewById(R.id.listLocal);
            list.setLayoutManager(new LinearLayoutManager(getActivity()));
            LocalAdapter ad = new LocalAdapter();
            list.setAdapter(ad);

        }
    }


    public void localParser(String s){
        array=new ArrayList<>();

        try{
            JSONArray jArray = new JSONObject(s).getJSONArray("documents");
            for(int i=0;i<jArray.length();i++){
                JSONObject obj = jArray.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();

                map.put("place_name",obj.getString("place_name"));//이름
                map.put("road_address_name",obj.getString("road_address_name"));//주소
                map.put("phone",obj.getString("phone"));//전화번호
                map.put("x",obj.getString("x"));//x
                map.put("y",obj.getString("y"));//y

                array.add(map);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //지역 어댑터
    class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_local,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HashMap<String,String> map =array.get(position);

            holder.name.setText(map.get("place_name"));
            holder.address.setText(map.get("road_address_name"));
            holder.tel.setText(map.get("phone"));

        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name,address,tel;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                address=itemView.findViewById(R.id.address);
                tel=itemView.findViewById(R.id.tel);



            }
        }
    }
}