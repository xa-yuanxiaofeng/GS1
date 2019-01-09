package com.browse.gs;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.browse.gs.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    //左边ListView
    @BindView(R.id.lvLeft)
    ListView leftListView;
    private ArrayAdapter<String> leftListViewAdapter;

    //设置按钮
    @BindView(R.id.btSetting)
    Button btSetting;
    @BindView(R.id.btExit)
    Button btExit;
    @BindView(R.id.buttonFinish)
    Button btFinish;
    //上面tab
    @BindView(R.id.tlTop)
    TabLayout topTabs;
    //当前被选中的tab
    int currentTabIndex =0;
    //页面数据链表
    ArrayList<CheckRecorderEntity> datas=new ArrayList<CheckRecorderEntity>();

    //气瓶编号
    @BindView(R.id.etCylinderNumber)
    public EditText cylinderNumber;
    //有效期
    @BindView(R.id.validDate)
    public EditText etValidityDate;
    //加气枪编号
    @BindView(R.id.spGunNumber)
    public Spinner gunNumber;
    //充装前外观
    @BindView(R.id.rgSurfaceBefore)
    public RadioGroup rgSurfaceBefore;
    //充装前泄漏
    @BindView(R.id.rgLeakBefore)
    public RadioGroup rgLeakBefore;
    //充装后外观
    @BindView(R.id.rgSufaceAfter)
    public RadioGroup rgSufaceAfter;
    //充装后泄漏
    @BindView(R.id.rgLeakAfter)
    public RadioGroup rgLeakAfter;
    //充装前余压
    @BindView(R.id.tvPressBefore)
    public TextView tvPressBefore;
    //充装开始时间
    @BindView(R.id.tvStartTime)
    public TextView tvStartTime;
    //充装后压力
    @BindView(R.id.tvPressAfter)
    public TextView tvPressAfter;
    //充装结束时间
    @BindView(R.id.tvEndTime)
    public TextView tvEndTime;
    //检查员
    @BindView(R.id.spCheckOperator)
    public Spinner spCheckOperator;
    //充装员
    @BindView(R.id.spFillOperator)
    public Spinner spFillOperator;
    //签字图片
    @BindView(R.id.signImage)
    public ImageView signImage;
    public String imageFileName;

    //配置对话框
    SettingDialog settingDialog;
    private ArrayList<String> plateNumbers= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
        leftListView.setAdapter(leftListViewAdapter);
        //设置listview点击事件
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //topTab加入选中的车牌
                String selectedPN =plateNumbers.get(i);
                topTabs.addTab(topTabs.newTab().setText(selectedPN ));
                //数据列表中加入检查的实体数据,用车牌号构造
                datas.add(new CheckRecorderEntity(selectedPN));
                //设置当前选中的tab
                currentTabIndex = topTabs.getTabCount()-1;
                topTabs.getTabAt(currentTabIndex).select();
                //自身列表中删除
                plateNumbers.remove(i);
                leftListViewAdapter.notifyDataSetChanged();
            }
        });

        // topTabs初始化
        this.setPath();
    }

    private void initEvent() {
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingDialog();
            }
        });
        btExit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.exit(0);//正常退出App
            }
        });
        btFinish.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                //提交数据
                try {
                    commitData(datas.get(currentTabIndex));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //删除被提交的数据
                datas.remove(datas.get(currentTabIndex));
                //删除当前选中的tab
                topTabs.removeTabAt(topTabs.getSelectedTabPosition());
            }
        });
        //设置充装前的radioGroup
        rgSurfaceBefore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioSurfaceBeforeGood){
                    //充装前外观
                    datas.get(currentTabIndex).setSurfaceBefore(1);
                }else{
                    datas.get(currentTabIndex).setSurfaceBefore(0);
                }
            }
        });
        //设置充装前泄漏
        rgLeakBefore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioLeakBeforeYes){
                    //充装前外观
                    datas.get(currentTabIndex).setLeakBefore(1);
                }else{
                    datas.get(currentTabIndex).setLeakBefore(0);
                }
            }
        });
        //设置充装后外观
        rgSufaceAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioSufaceAfterGood){
                    //充装前外观
                    datas.get(currentTabIndex).setSurfaceAfter(1);
                }else{
                    datas.get(currentTabIndex).setSurfaceAfter(0);
                }
            }
        });
        //设置充装后泄漏
        rgLeakAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioLeakAfterYes){
                    //充装前外观
                    datas.get(currentTabIndex).setLeakAfter(1);
                }else{
                    datas.get(currentTabIndex).setLeakAfter(0);
                }
            }
        });
    }

    //设置对话框
    private void showSettingDialog(){
        settingDialog= new SettingDialog(this);
        settingDialog.show();
    }
    //读取当前的数据
    private void  readCurrentData(CheckRecorderEntity entity){}


    //保存当前的数据
    private void commitData(CheckRecorderEntity entity) throws ParseException {
        //气瓶号
        entity.setCylinderNumber(cylinderNumber.getText().toString());
        //有效期
        String validDate =etValidityDate.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        entity.setValidityPeriod(sdf.parse(validDate));
        //枪编号
        entity.setGunNumber(gunNumber.getSelectedItem().toString());
        //图片文件名,从签字返回的result中赋值
        entity.setSignFile(imageFileName);
        //检查员
        entity.setCheckOperator(spCheckOperator.getSelectedItem().toString());
        //充装员
        entity.setFillOperator(spFillOperator.getSelectedItem().toString());
        Toast.makeText(this,JSON.toJSONString(entity),LENGTH_LONG).show();
        Log.i("commit data---",JSON.toJSONString(entity));
        //充装前余压
        //充装开始时间
        //充装后压力
        //充装结束时间

    }
    //弹出签字界面
    public void popSignPane(View view){
        Intent intent = new Intent();
        intent.setClass(this, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ID","sign-"+UUID.randomUUID().toString());
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
                imageFileName =Util.getSharedPreference(
                        MainActivity.this, "FileDir")
                        + result
                        + ".png";
                if (!Util.fileExists(imageFileName))
                    return;
                Util.releaseBitmap(signImage);
                bmp = Util.getBitmapThumbnail(imageFileName, 700, 300);
                signImage.setImageBitmap(bmp);
            }else {
                imageFileName =Util.getSharedPreference(
                        MainActivity.this, "FileDir")
                        + result
                        + ".jpg";
                if(!Util.fileExists(imageFileName))
                    return;
                bmp = Util.getBitmapThumbnail(imageFileName, 150,105);
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


