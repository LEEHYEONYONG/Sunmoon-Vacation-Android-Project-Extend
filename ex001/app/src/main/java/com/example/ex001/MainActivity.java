package com.example.ex001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Database sql;
    SQLiteDatabase db;
    Cursor cur;
    MyAdapter ad;
    ListView list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("일기장");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_vertical_split_24);

        sql = new Database(this);
        db=sql.getWritableDatabase();
        cur = db.rawQuery("select * from diary",null );
        ad=new MyAdapter(this,cur);

        list=(ListView)findViewById(R.id.list);
        list.setAdapter(ad);
        registerForContextMenu(list);
        /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,)
            }
        });
        */




    }



    public class MyAdapter extends CursorAdapter{

        public MyAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            return inflater.inflate(R.layout.item,viewGroup,false);

        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView wdate = (TextView)view.findViewById(R.id.wdate);
            TextView subject = (TextView)view.findViewById(R.id.subject);
            wdate.setText(cursor.getString(1));
            subject.setText(cursor.getString(2));
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0,1,0,"최근순정렬");
        menu.add(0,2,0,"과거순정렬");
        menu.add(0,3,0,"제목순정렬");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String str= "";
        switch (item.getItemId()){
            case 1:
                str="select * from diary order by wdate desc";
            case 2:
                str="select * from diary order by wdate";
            case 3:
                str="select * from diary order by subject";
        }
        cur=db.rawQuery(str,null);
        ad.changeCursor(cur);

        return super.onContextItemSelected(item);
    }
}