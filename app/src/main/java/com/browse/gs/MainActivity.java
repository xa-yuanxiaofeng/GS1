package com.browse.gs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    //左边ListView
    ListView leftListView;
    private ArrayAdapter<String> leftListViewAdapter;

    //设置按钮
    Button btSetting;
    Button btExit;

    //上面tab
    TabLayout topTabs;

    //气瓶编号
    @BindView(R.id.etCylinderNumber)
    EditText cylinderNumber;
    //有效期
    @BindView(R.id.etValidityPeriod)
    EditText validityPeriod;
    //加气枪编号
    @BindView(R.id.spGunNumber)
    Spinner gunNumber;

    //配置对话框
    SettingDialog settingDialog;
    private ArrayList<String> plateNumbers= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initEvent();

    }

    @SuppressLint("WrongViewCast")
    private void init() {

        //左数据初始化
        String[] data=new String[]{"陕123121","京77889S","沪000000","吉090899","浙242342",
                "川342346","湘9837u4","赣983475","黑576573","宁834323"};
        for( String item:data)
            plateNumbers.add(item);
        leftListViewAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.plate_number_item,
                R.id.plate_item,
                plateNumbers
        );
        leftListView = findViewById(R.id.lvLeft);
        leftListView.setAdapter(leftListViewAdapter);

        // top数据初始化
        topTabs=findViewById(R.id.tlTop);
        for(int i=0;i<8;i++) {
            TabLayout.Tab tab=topTabs.newTab();
            topTabs.addTab(topTabs.newTab().setText(data[i]));

        }




    }

    private void initEvent() {
        btSetting=findViewById(R.id.btSetting);
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingDialog();
            }
        });
        btExit=findViewById(R.id.btExit);
        btExit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.exit(0);//正常退出App
            }
        });
    }

    private void showSettingDialog(){
        settingDialog= new SettingDialog(this);
        settingDialog.show();
    }

}


