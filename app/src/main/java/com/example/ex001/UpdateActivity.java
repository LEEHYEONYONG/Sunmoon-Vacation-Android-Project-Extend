package com.example.ex001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class UpdateActivity extends AppCompatActivity {
    Database db;
    SQLiteDatabase sql;
    Cursor cur;
    TextView wdate;
    EditText subject, content;
    int mMonth,mDay,mYear;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        getSupportActionBar().setTitle("일기수정"+ id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db=new Database(this);
        sql=db.getWritableDatabase();
        cur=sql.rawQuery("select * from diary where _id=" + id ,null);

        wdate=findViewById(R.id.wdate);
        subject=findViewById(R.id.subject);
        content=findViewById(R.id.content);

        if(cur.moveToNext()){
            wdate.setText(cur.getString(1));
            subject.setText(cur.getString(2));
            content.setText(cur.getString(3));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void mClick(View v){
        switch (v.getId()){
            case R.id.calendar:
                String strWdate=wdate.getText().toString();
                mYear=Integer.parseInt(strWdate.substring(0,4));
                mMonth=Integer.parseInt(strWdate.substring(5,7));
                mDay=Integer.parseInt(strWdate.substring(8,10));
                new DatePickerDialog(this,setDate,mYear,mMonth-1,mDay).show();
                break;
            case R.id.btn1:
                strWdate = wdate.getText().toString();
                String strSubject = subject.getText().toString();
                String strContent = content.getText().toString();
                String str="update diary set ";
                str +="wdate='" +strWdate +"',";
                str +="subject='" +strSubject +"',";
                str +="content='" +strContent +"'";
                str +=" where _id="+id;
                sql.execSQL(str);
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.btn2:
                setResult(RESULT_CANCELED);
                finish();
                break;

        }
    }

    DatePickerDialog.OnDateSetListener setDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            mYear=i;
            mMonth=i1;
            mDay=i2;
            wdate.setText(String.format("%04d/%02d/%02d", mYear,mMonth+1,mDay));
        }
    };
}