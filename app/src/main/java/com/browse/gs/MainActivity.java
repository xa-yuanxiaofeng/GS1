package com.browse.gs;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;

    private ViewPager viewPager;

    private MyFPAdapter fpAdapter;

    private ListView plateNumberListView;
    private ArrayAdapter<String> plateNumberAdapter;

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
        plateNumberListView=findViewById(R.id.plateNumberList);

        plateNumberAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.plate_number_item,
                R.id.plate_item,
                new String[]{"陕123121","京77889S","沪000000","吉090899","浙242342",
                          "川342346","湘9837u4","赣983475","黑576573","宁834323"}
        );

        plateNumberListView.setAdapter(plateNumberAdapter);



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
