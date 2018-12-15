package com.browse.gs;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.browse.gs.adpter.ContentViewPageAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SettingDialog sd;
    static MainActivity ma;
    public TabLayout tabLayout;
    private ViewPager viewPager;

    private Button exitButton;

    public ContentViewPageAdapter viewPagerAdapter;

    private ListView plateNumberListView;
    private ArrayAdapter<String> plateNumberAdapter;

    private ArrayList<String> plateNumbers= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma=this;
        setContentView(R.layout.activity_main);

        init();
        initEvent();

    }

    private void init() {
        String[] data=new String[]{"陕123121","京77889S","沪000000","吉090899","浙242342",
                "川342346","湘9837u4","赣983475","黑576573","宁834323"};
        for( String item:data)
            plateNumbers.add(item);


        plateNumberListView=findViewById(R.id.plateNumberList);
        plateNumberAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.plate_number_item,
                R.id.plate_item,
                plateNumbers
        );

        tabLayout =  findViewById(R.id.tab_layout);

        viewPager =  findViewById(R.id.viewPager);
        viewPagerAdapter = new ContentViewPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        plateNumberListView.setAdapter(plateNumberAdapter);
        plateNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //list点击事件
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
            {
                String tabHeader=plateNumbers.get(p3);
                viewPagerAdapter.addTab(tabHeader);
                plateNumbers.remove(p3);
                plateNumberAdapter.notifyDataSetChanged();
                viewPagerAdapter.notifyDataSetChanged();
            }
        });
        exitButton= findViewById(R.id.exit1);
        exitButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.exit(0);//正常退出App
            }
        });


    }

    private void initEvent() {
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetting();
            }
        });
    }

    private void showSetting(){
        sd= new SettingDialog(this);
        sd.show();
    }
    public static MainActivity getMainActivity() {
        return ma;
    }


}


