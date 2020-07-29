package com.example.ex05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Adapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tab;
    ArrayList<Fragment> fragments;
    PageAdapter ad;
    ViewPager pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab=findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("도서검색"));
        tab.addTab(tab.newTab().setText("영화검색"));
        tab.addTab(tab.newTab().setText("지역검색"));
        tab.getTabAt(0).setIcon(R.drawable.ic_baseline_book_24);
        tab.getTabAt(1).setIcon(R.drawable.ic_baseline_movie_24);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragments=new ArrayList<>();
        fragments.add(new BookFragment());
        fragments.add(new MovieFragment());
        fragments.add(new LocalFragment());

        ad=new PageAdapter(getSupportFragmentManager(),0);
        pager = findViewById(R.id.pager);
        pager.setAdapter(ad);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
    }

    //어댑터
    class PageAdapter extends FragmentStatePagerAdapter{
        public PageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}