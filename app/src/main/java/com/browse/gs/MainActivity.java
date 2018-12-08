package com.browse.gs;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    private ViewPager viewPager;

    private MyFPAdapter fpAdapter;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] fragment = new String[]{"最新","热门","我的","最新","热门","我的","最新","热门","我的","ok"};
    private String[] tabHeader ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    private void init() {
        Resources res =getResources();
        tabHeader=res.getStringArray(R.array.plateNumber);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager =  findViewById(R.id.viewPager);


        tabLayout.setupWithViewPager(viewPager,false);

        fpAdapter = new MyFPAdapter(fragments,tabLayout,getSupportFragmentManager());

        viewPager.setAdapter(fpAdapter);

        for(int i=0;i<tabHeader.length;i++){
            fragments.add(new MyFragment(this.getApplicationContext(), fragment[i]));
            tabLayout.addTab(tabLayout.newTab().setText(tabHeader[i]));
        }
        fpAdapter.notifyDataSetChanged();
    }

}
