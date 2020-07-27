package com.example.ex001;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "diary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {
        sql.execSQL("create table diary(_id integer primary key autoincrement, wdate text, subject text , content text)");
        sql.execSQL("insert into diary(wdate,subject,content) values('2017/10/10','안드로이드','안드로이드 어렵지 않아요!')");
        sql.execSQL("insert into diary(wdate,subject,content) values('2017/10/10','장마시작','장마가 시작되었다.')");
        sql.execSQL("insert into diary(wdate,subject,content) values('2017/10/12','생일파티','정원이 생일 축하 파티를 했다.')");
        sql.execSQL("insert into diary(wdate,subject,content) values('2017/10/13','중간고사','중간고사 시험을 아주 잘 봤다.')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
