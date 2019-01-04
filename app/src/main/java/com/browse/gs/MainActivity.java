package com.browse.gs;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.browse.gs.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    //左边ListView
    ListView leftListView;
    private ArrayAdapter<String> leftListViewAdapter;

    //设置按钮
    Button btSetting;
    Button btExit;
    Button btFinish;
    //上面tab
    TabLayout topTabs;
    //当前被选中的tab
    int currentSelectedTab =0;
    //页面数据链表
    ArrayList<CheckRecorderEntity> datas=new ArrayList<CheckRecorderEntity>();

    //气瓶编号
    @BindView(R.id.etCylinderNumber)
    EditText cylinderNumber;
    //有效期
    @BindView(R.id.etValidityPeriod)
    DatePicker validityPeriod;
    //加气枪编号
    @BindView(R.id.spGunNumber)
    Spinner gunNumber;
    //签字图片
    ImageView signImage;

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

        //左列表数据初始化
        final String[] data=new String[]{"陕123121","京77889S","沪000000","吉090899","浙242342",
                "川342346","湘9837u4","赣983475","黑576573","宁834323"};
        for( String item:data)
            plateNumbers.add(item);

        //左列表
        leftListViewAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.plate_number_item,
                R.id.plate_item,
                plateNumbers
        );
        leftListView = findViewById(R.id.lvLeft);
        leftListView.setAdapter(leftListViewAdapter);
        //设置listview点击事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //topTab加入选中的车牌
                String selectedPN =plateNumbers.get(i);
                topTabs.addTab(topTabs.newTab().setText(selectedPN ));
                //数据列表中加入检查的实体数据
                datas.add(new CheckRecorderEntity(selectedPN));
                //设置当前选中的tab
                currentSelectedTab = topTabs.getTabCount()-1;
                topTabs.getTabAt(currentSelectedTab).select();
                //自身列表中删除
                plateNumbers.remove(i);
                leftListViewAdapter.notifyDataSetChanged();
            }
        });

        // topTabs初始化
        topTabs=findViewById(R.id.tlTop);
        signImage=findViewById(R.id.signImage);
        this.setPath();
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
        btFinish=findViewById(R.id.buttonFinish);
        btFinish.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //删除当前选中的tab
                topTabs.removeTabAt(topTabs.getSelectedTabPosition());
            }
        });
    }

    //设置对话框
    private void showSettingDialog(){
        settingDialog= new SettingDialog(this);
        settingDialog.show();
    }
    //读取显示选中的数据
    private void  readSelectedData(){
        datas.get(currentSelectedTab);

    }
    //保存修改的数据
    private void saveSelectedData(){
        datas.get(currentSelectedTab).setCylinderNumber(cylinderNumber.getText().toString());
        //datas.get(currentSelectedTab).setValidityPeriod(validityPeriod.getAutofillValue());
    }
    //弹出签字界面
    public void popSignPane(View view){
        Intent intent = new Intent();
        intent.setClass(this, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ID","sign"+UUID.randomUUID().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    //从弹出的签字界面返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        Bitmap bmp;
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;
        try {
            String result = intent.getStringExtra("result");
            if (intent.hasExtra("signature")) {
                if (!Util.fileExists(Util.getSharedPreference(
                        MainActivity.this, "FileDir")
                        + result
                        + ".png"))
                    return;
                Util.releaseBitmap(signImage);
                bmp = Util.getBitmapThumbnail(
                        Util.getSharedPreference(MainActivity.this,
                                "FileDir") + result + ".png", 700, 300);
                signImage.setImageBitmap(bmp);
            }else {
                if(!Util.fileExists(Util.getSharedPreference(MainActivity.this, "FileDir") + result + ".jpg"))
                    return;
                bmp = Util.getBitmapThumbnail(Util.getSharedPreference(MainActivity.this, "FileDir") + result + ".jpg", 150,105);
                String idx = result.substring(result.length() - 1);
                if (idx.equals("1"))
                {
                    Util.releaseBitmap(signImage);
                    signImage.setImageBitmap(bmp);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "----------" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //sharePreference路径
    private void setPath() {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getFilesDir();
        Util.setSharedPreference(this, "FileDir", directory.getAbsolutePath() +"/");
    }
}


