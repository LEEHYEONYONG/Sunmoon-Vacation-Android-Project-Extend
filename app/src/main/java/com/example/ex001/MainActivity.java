package com.example.ex001;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id=cur.getInt(0);
                //Toast.makeText(MainActivity.this,"id:" + id,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                intent.putExtra("id",id);
                startActivityForResult(intent,2);
            }
        });


        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivityForResult(intent,1);
            }
        });

        /*
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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

        final int id = cursor.getInt(0);
        ImageView delete = view.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                box.setTitle("질의:" + id);
                box.setMessage("삭제하실래요?");
                box.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String str = "delete from diary where _id=" + id;
                        db.execSQL(str);

                        cur=db.rawQuery("select * from diary order by wdate desc",null);
                        ad.changeCursor(cur);
                    }
                });
                box.setNegativeButton("아니오",null);
                box.show();



            }
        });
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
                break;
            case 2:
                str="select * from diary order by wdate";
                break;
            case 3:
                str="select * from diary order by subject";
                break;
        }
        cur=db.rawQuery(str,null);
        ad.changeCursor(cur);

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String str="select * from diary where subject like '%" +s+"%'";
                str += " order by wdate desc";
                cur=db.rawQuery(str,null);
                ad.changeCursor(cur);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode ==1 &&resultCode==RESULT_OK){
            cur=db.rawQuery("select * from diary order by wdate desc",null);
            ad.changeCursor(cur);
            Toast.makeText(this,"저장완료",Toast.LENGTH_SHORT).show();
        }else if (requestCode ==1 &&resultCode==RESULT_CANCELED){
            Toast.makeText(this,"저장취소",Toast.LENGTH_SHORT).show();
        }else if(requestCode ==2 &&resultCode==RESULT_OK){
            cur=db.rawQuery("select * from diary order by wdate desc",null);
            ad.changeCursor(cur);
            Toast.makeText(this,"수정완료",Toast.LENGTH_SHORT).show();
        }else if(requestCode ==2 &&resultCode==RESULT_CANCELED){
            Toast.makeText(this,"수정취소",Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}