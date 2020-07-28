package com.example.naver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        getSupportActionBar().setTitle("도서내용");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent();
        String stitle = getIntent().getStringExtra("title");
        String sauthor = getIntent().getStringExtra("author");
        String sprice = getIntent().getStringExtra("price");
        String sdescription = getIntent().getStringExtra("description");
        String simage = getIntent().getStringExtra("image");
        String spubdate = getIntent().getStringExtra("pubdate");
        String spublisher = getIntent().getStringExtra("publisher");

        TextView title = findViewById(R.id.title);
        TextView author = findViewById(R.id.author);
        TextView price = findViewById(R.id.price);
        TextView description = findViewById(R.id.description);
        ImageView image = findViewById(R.id.image);
        TextView pubdate = findViewById(R.id.pubdate);
        TextView publisher = findViewById(R.id.publisher);

        title.setText(Html.fromHtml(stitle));
        author.setText("저자: " + Html.fromHtml(sauthor));
        price.setText("가격: " + Html.fromHtml(sprice) + "원");
        description.setText(Html.fromHtml(sdescription));
        pubdate.setText("출판일: " + Html.fromHtml(spubdate));
        publisher.setText("출판사: " + Html.fromHtml(spublisher));

        Picasso.with(BookDetailActivity.this).load(simage).into(image);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(==android.R.id.home){

        }
        return super.onCreateOptionsMenu(menu);
    }
    */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}