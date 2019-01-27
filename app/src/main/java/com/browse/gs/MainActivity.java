package com.browse.gs;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browse.gs.adpter.MySpinnerAdapter;
import com.browse.gs.util.ConfigConstant;
import com.browse.gs.util.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.jar.Attributes;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;
import static com.browse.gs.util.Util.getSharedPreference;

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
    boolean isFinish = false;
    @BindView(R.id.btFetchData)
    Button btFetchData;

    //上面tab
    @BindView(R.id.tlTop)
    TabLayout topTabs;
    //当前被选中的tab
    int pointer = -1;
    //页面数据链表
    ArrayList<CheckRecorderEntity> datas = new ArrayList<CheckRecorderEntity>();

    //气瓶编号
    @BindView(R.id.etCylinderNumber)
    public EditText cylinderNumber;
    //有效期
    @BindView(R.id.validDate)
    public EditText etValidityDate;
    //加气枪编号
    @BindView(R.id.spGunCode)
    public Spinner spGunCode;
    //加气机编号
    @BindView(R.id.gasMachineCode)
    public TextView gasMachineCode;
    //加气站名称
    @BindView(R.id.gasName)
    public TextView gasName;


    //加气机编号

    //加气站名称
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

    //充装id
    String fileDataId;
    //充装数据显示
    @BindView(R.id.tvFillDataInfo)
    public TextView tvFillDataInfo;


    //检查员
    @BindView(R.id.spCheckOperator)
    public Spinner spCheckOperator;
    //充装员
    @BindView(R.id.spFillOperator)
    public Spinner spFillOperator;
    //签字图片
    @BindView(R.id.signImage)
    public ImageView signImage;

    //配置对话框
    SettingDialog settingDialog;
    private ArrayList<String> plateNumbers = new ArrayList<String>();

    //读取ServerPool的线程，每1秒刷新一次plateNumbers
    Thread fetchPoolThread;

    //读取t_fill_data的线程，每1秒刷新一次plateNumbers
    Thread fetchFillDataThread;
    //日期数据格式
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    // 车牌httpRequest返回处理
    private Handler mPlateNumbersHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                super.handleMessage(msg);
                try {
                    JSONArray jsonArray = JSONObject.parseArray((String) msg.obj);
                    List list = JSONObject.parseArray((String) msg.obj, String.class);
                    plateNumbers.clear();
                    plateNumbers.addAll(list);
                    //左列表
                    leftListViewAdapter = new ArrayAdapter<String>(
                            MainActivity.this,
                            R.layout.plate_number_item,
                            R.id.plate_item,
                            plateNumbers
                    );
                    leftListView.setAdapter(leftListViewAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            }
        }
    };

    // 实时充装数据httpRequest返回处理
    private Handler mFillDataHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                super.handleMessage(msg);
                try {
                    JSONObject jsonObject = JSONObject.parseObject((String) msg.obj);
                    showFillData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

            }
        }
    };

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
        final String[] data = new String[]{"陕123121", "京77889S", "沪000000", "吉090899", "浙242342",
                "川342346", "湘9837u4", "赣983475", "黑576573", "宁834323"};
        for (String item : data)
            plateNumbers.add(item);

        //左列表
        leftListViewAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.plate_number_item,
                R.id.plate_item,
                plateNumbers
        );
        leftListView.setAdapter(leftListViewAdapter);
        //3个spinner，设置适配器（控制器）
        spGunCode.setAdapter(new MySpinnerAdapter(this, getResources().getStringArray(R.array.gunCode)));
        spCheckOperator.setAdapter(new MySpinnerAdapter(this, getResources().getStringArray(R.array.checkOperator)));
        spFillOperator.setAdapter(new MySpinnerAdapter(this, getResources().getStringArray(R.array.fillOperator)));

        //设置加气站的名称和加气机的名称
        //加气站名称
        String temp = getSharedPreference(this, "gasName");
        if (temp != null && !temp.equals(""))
            gasName.setText(temp);
        //加气机编号
        temp = getSharedPreference(this, "gasMachineCode");
        if (temp != null && !temp.equals(""))
            gasMachineCode.setText(temp);

        // topTabs初始化
        this.setPath();

        //启动线程,动态读取车牌
        fetchPoolThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        //fecthPool 从服务器上读取车牌
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet(ConfigConstant.serverPort + "/rs/getPool");
                        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                        int code = httpResponse.getStatusLine().getStatusCode();
                        if (code == 200) {
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = EntityUtils.toString(
                                    httpResponse.getEntity(), "UTF8");
                            mPlateNumbersHandler.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 0;
                            mPlateNumbersHandler.sendMessage(msg);
                        }
                        Thread.sleep(5000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fetchPoolThread.start();
    }

    private void initEvent() {
        //设置listview点击事件，增加tab，删除列表项
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //增加实体数据
                String selectedPN = plateNumbers.get(i);
                //tab和datas同增同删，数据列表中加入检查的实体数据,用车牌号构造，
                CheckRecorderEntity entity = new CheckRecorderEntity(selectedPN);
                //设定4个radio选项默认值
                entity.setSurfaceBefore(R.id.radioSurfaceBeforeGood);
                entity.setLeakBefore(R.id.radioLeakBeforeNo);
                entity.setSurfaceAfter(R.id.radioSufaceAfterGood);
                entity.setLeakAfter(R.id.radioLeakAfterNo);
                //tab和datas同增同删
                datas.add(entity);
                topTabs.addTab(topTabs.newTab().setText(selectedPN), true);
                //校正指针
                pointer = datas.size() - 1;
                //左列表中删除
                plateNumbers.remove(i);
                leftListViewAdapter.notifyDataSetChanged();
            }
        });
        //tab切换时刷新页面数据，确保显示当前被选中的index的数据
        topTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int oldPointer = pointer;
                pointer = tab.getPosition();
                try {
                    //页面完成关闭时，产生的selected事件，直接显示
                    if (isFinish) {
                        isFinish = false;
                        readData(pointer);
                    } else {
                        //1保存数据
                        saveData(oldPointer);
                        //3重新读取数据
                        readData(pointer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //设置按钮
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettingDialog();
            }
        });
        // 退出按钮
        btExit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);//正常退出App
            }
        });
        //完成按钮
        btFinish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int oldPointer = pointer;
                    pointer--;
                    isFinish = true;
                    //提交数据
                    saveData(oldPointer);
                    //同增同删，删除被提交的数据,指针减一
                    datas.remove(oldPointer);
                    topTabs.removeTabAt(oldPointer);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        //读取加气机的实时数据t_fill_data
        btFetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchFillDataThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //fecthPool 从服务器上读取车牌
                            CloseableHttpClient httpClient = HttpClients.createDefault();
                            HttpGet httpGet = new HttpGet(ConfigConstant.serverPort + "/rs1/getFD/1号枪/6号机/第一加气站");
                            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
                            int code = httpResponse.getStatusLine().getStatusCode();
                            if (code == 200) {
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = EntityUtils.toString(
                                        httpResponse.getEntity(), "UTF8");
                                mFillDataHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 0;
                                mFillDataHandler.sendMessage(msg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                fetchFillDataThread.start();
            }
        });

        //设置充装前的radioGroup
        rgSurfaceBefore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioSurfaceBeforeGood) {
                    //充装前外观
                    datas.get(pointer).setSurfaceBefore(1);
                } else {
                    datas.get(pointer).setSurfaceBefore(0);
                }
            }
        });
        //设置充装前泄漏
        rgLeakBefore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioLeakBeforeYes) {
                    //充装前外观
                    datas.get(pointer).setLeakBefore(1);
                } else {
                    datas.get(pointer).setLeakBefore(0);
                }
            }
        });
        //设置充装后外观
        rgSufaceAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioSufaceAfterGood) {
                    //充装前外观
                    datas.get(pointer).setSurfaceAfter(1);
                } else {
                    datas.get(pointer).setSurfaceAfter(0);
                }
            }
        });
        //设置充装后泄漏
        rgLeakAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioLeakAfterYes) {
                    //充装前外观
                    datas.get(pointer).setLeakAfter(1);
                } else {
                    datas.get(pointer).setLeakAfter(0);
                }
            }
        });
    }

    //设置对话框
    private void showSettingDialog() {
        settingDialog = new SettingDialog(this);
        settingDialog.show();
    }


    //保存当前的数据
    private void saveData(int _pointer) throws ParseException {
        if (_pointer == -1)
            return;
        CheckRecorderEntity entity = datas.get(_pointer);
        //气瓶号
        entity.setCylinderNumber(cylinderNumber.getText().toString());
        //有效期
        String validDate = etValidityDate.getText().toString();
        if (validDate != null && validDate.length() == 8)
            entity.setValidityPeriod(simpleDateFormat.parse(validDate));
        //枪编号
        entity.setGunNumber(spGunCode.getSelectedItem().toString());
        //充装前外观
        entity.setSurfaceBefore(rgSurfaceBefore.getCheckedRadioButtonId());
        //充装前泄漏
        entity.setLeakBefore(rgLeakBefore.getCheckedRadioButtonId());
        //充装后外观
        entity.setSurfaceAfter(rgSufaceAfter.getCheckedRadioButtonId());
        //充装后泄漏
        entity.setLeakAfter(rgLeakAfter.getCheckedRadioButtonId());
        //图片文件名,从签字返回的result中赋值
        //entity.setSignFile(imageFileName);
        //检查员
        entity.setCheckOperator(spCheckOperator.getSelectedItem().toString());
        //充装员
        entity.setFillOperator(spFillOperator.getSelectedItem().toString());
        //充装前余压
        //充装开始时间
        //充装压力
        //充装后压力
        //充装结束时间
        //充装气量
    }

    //从内存实体中读取当前数据
    private void readData(int _pointer) throws ParseException {
        if (_pointer == -1)
            return;
        CheckRecorderEntity entity = datas.get(_pointer);
        //气瓶号
        cylinderNumber.setText(entity.getCylinderNumber());
        //有效期
        Date tempDate = entity.getValidityPeriod();
        //如果日期为空
        if (tempDate == null)
            etValidityDate.setText("");
            //否则
        else {
            etValidityDate.setText(simpleDateFormat.format(tempDate));
        }
        //枪编号
        Util.setSpinnerSelectItem(spGunCode, entity.getGunNumber());
        //充装前外观
        rgSurfaceBefore.check(entity.getSurfaceBefore());
        //充装前泄漏
        rgLeakBefore.check(entity.getLeakBefore());
        //充装后外观
        rgSufaceAfter.check(entity.getSurfaceAfter());
        //充装后泄漏
        rgLeakAfter.check(entity.getLeakAfter());
        //设置检查员
        Util.setSpinnerSelectItem(spCheckOperator, entity.getCheckOperator());
        //设置充装员
        Util.setSpinnerSelectItem(spFillOperator, entity.getFillOperator());
        //设置签名，图片文件名,从签字返回的result中赋值
        String imageFileName = entity.getSignFile();
        if (imageFileName != null && Util.fileExists(imageFileName)) {
            //显示签名图片
            Bitmap bmp = Util.getBitmapThumbnail(imageFileName, 150, 105);
            Util.releaseBitmap(signImage);
            signImage.setImageBitmap(bmp);
        } else
            signImage.setImageDrawable(null);
    }

    //弹出签字界面
    public void popSignPane(View view) {
        Intent intent = new Intent();
        intent.setClass(this, SignActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ID", "sign-" + UUID.randomUUID().toString());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1);
    }

    //从弹出的签字界面返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        Bitmap bmp;
        String imageFileName;
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;
        try {
            String result = intent.getStringExtra("result");
            if (intent.hasExtra("signature")) {
                imageFileName = Util.getSharedPreference(
                        MainActivity.this, "FileDir")
                        + result
                        + ".png";
                if (!Util.fileExists(imageFileName))
                    return;
                Util.releaseBitmap(signImage);
                bmp = Util.getBitmapThumbnail(imageFileName, 700, 300);
                signImage.setImageBitmap(bmp);
            } else {
                imageFileName = Util.getSharedPreference(
                        MainActivity.this, "FileDir")
                        + result
                        + ".jpg";
                if (!Util.fileExists(imageFileName))
                    return;
                bmp = Util.getBitmapThumbnail(imageFileName, 150, 105);
                String idx = result.substring(result.length() - 1);
                if (idx.equals("1")) {
                    Util.releaseBitmap(signImage);
                    signImage.setImageBitmap(bmp);
                }
            }
            //设置签名文件的名字
            datas.get(pointer).setSignFile(imageFileName);
        } catch (Exception e) {
            Toast.makeText(this, "----------" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    //sharePreference路径
    private void setPath() {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getFilesDir();
        Util.setSharedPreference(this, "FileDir", directory.getAbsolutePath() + "/");
    }

    //显示充装数据
    private void showFillData(JSONObject jo){
        String info="开始时间:"+Util.converDateTimeFormat(jo.get("startTime").toString())+"  余压:"+jo.get("pressBefore").toString()+
                    "\r\n气源压力:"+jo.get("gasPress").toString()+"  充装量:"+jo.get("gasCubage").toString()+
                    "\r\n结束时间:"+Util.converDateTimeFormat(jo.get("endTime").toString())+ "  充装后压力:"+jo.get("pressAfter").toString();
        tvFillDataInfo.setText(info);
    }
}


