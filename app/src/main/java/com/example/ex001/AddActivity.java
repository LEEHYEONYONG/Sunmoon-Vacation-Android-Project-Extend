package com.example.ex001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Year;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddActivity extends AppCompatActivity {

    int mYear,mMonth,mDay;
    TextView wdate;
    Database db;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = new Database(this);
        sql=db.getWritableDatabase();


        getSupportActionBar().setTitle("일기쓰기");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        GregorianCalendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        wdate = findViewById(R.id.wdate);

        wdate.setText(String.format("%04d/%02d/%02d", mYear,mMonth+1,mDay));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void mClick(View v){
        switch (v.getId()){
            case R.id.calendar:
                new DatePickerDialog(this,setDate,mYear,mMonth-1,mDay).show();
                break;
            case R.id.btn1://저장버튼

                TextView wdate= findViewById(R.id.wdate);
                EditText subject =findViewById(R.id.subject);
                EditText content = findViewById(R.id.content);
                String strWdate= wdate.getText().toString();
                String strSubject=subject.getText().toString();
                String strContent=content.getText().toString();

                if (strSubject.equals("") || strContent.equals("")) {
                    Toast.makeText(this,"제목이나 내용을 입력하세요.",Toast.LENGTH_SHORT).show();
                }else{
                    String str = "insert into diary(wdate,subject,content) values(";
                    str += "'" +strWdate+"',";
                    str += "'" +strSubject+"',";
                    str += "'" +strContent+"')";
                    sql.execSQL(str);

                    setResult(RESULT_OK);
                    finish();
                }
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
            mDay=12;
            wdate.setText(String.format("%04d/%02d/%02d", mYear,mMonth,mDay));
        }
    };
}