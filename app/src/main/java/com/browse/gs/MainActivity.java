package com.browse.gs;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.browse.entity.CheckRecorder;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    Boolean finishAction=false;
    @BindView(R.id.btFetchData)
    Button btFetchData;

    //上面tab
    @BindView(R.id.tlTop)
    TabLayout topTabs;
    //当前被选中的tab
    int pointer = -1;

    //检查记录
    ArrayList<CheckRecorder> checkRecorders = new ArrayList<CheckRecorder>();
    //充装记录
    ArrayList<JSONObject> fillRecorders=new ArrayList<JSONObject>();

    //气瓶编号
    @BindView(R.id.etCylinderNumber)
    public EditText etCylinderNumber;
    //有效期
    @BindView(R.id.etValidDate)
    public EditText etValidDate;

    //加气枪编号
    @BindView(R.id.spGunCode)
    public Spinner spGunCode;
    //加气机编号
    @BindView(R.id.gasMachineCode)
    public TextView tvGasMachineCode;
    //加气站名称
    @BindView(R.id.gasName)
    public TextView tvGasName;


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

    //充装数据id
    String fillDataId;
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

    //上传检查数据
    Thread mUploadCheckRecordThread;
    // 上传签名图片
    Thread mUploadSignThread;

    //读取t_fill_data的线程，每1秒刷新一次plateNumbers
    Thread fetchFillDataThread;
    //日期数据格式
    SimpleDateFormat formDateFormat = new SimpleDateFormat("yyyymmdd");
    // 从pool中取车牌，httpRequest返回处理
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
    private Handler mFillRecorderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (1 == msg.what) {
                super.handleMessage(msg);
                try {
                    if (pointer == -1) {
                        Toast.makeText(MainActivity.this, "请先选中车辆", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    JSONObject fillRecorder = JSONObject.parseObject((String) msg.obj);
                    CheckRecorder checkRecorder = checkRecorders.get(pointer);
                    checkRecorder.setFillRecorderId((int)fillRecorder.get("id"));
                    fillRecorders.add(fillRecorder);
                    //显示到页面区域
                    readFillRecorderFromMemory(pointer);
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
            tvGasName.setText(temp);
        //加气机编号
        temp = getSharedPreference(this, "gasMachineCode");
        if (temp != null && !temp.equals(""))
            tvGasMachineCode.setText(temp);

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
                //tab和checkRecorders同增同删，数据列表中加入检查的实体数据,用车牌号构造，
                CheckRecorder entity = new CheckRecorder(selectedPN);
                //设定4个radio选项默认值
                entity.setSurfaceBefore(1);
                entity.setLeakBefore(0);
                entity.setSurfaceAfter(1);
                entity.setLeakAfter(0);
                //tab和checkRecorders同增同删
                checkRecorders.add(entity);
                topTabs.addTab(topTabs.newTab().setText(selectedPN), true);
                //校正指针
                pointer = checkRecorders.size() - 1;
                //左列表中删除
                plateNumbers.remove(i);
                leftListViewAdapter.notifyDataSetChanged();
            }
        });
        //tab切换时刷新页面数据，确保显示当前被选中的index的数据
        topTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    int oldPointer = pointer;
                    pointer = tab.getPosition();
                    //因提交动作产生的OnTabSelectedListener，不用保存
                    if(finishAction) {
                        finishAction = false;
                    }else{
                        saveCheckRecorderToMemory(oldPointer);
                    }
                    //3重新读取数据
                    readCheckRecorderFromMemory(pointer);
                    readFillRecorderFromMemory(pointer);
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
                finishAction=true;
                int oldPointer = pointer;
                pointer = topTabs.getSelectedTabPosition();
                //完成时，先保存，再提交
                saveCheckRecorderToMemory(oldPointer);
                //检查的数据上传，调用add方法
                uploadCheckRecorder(oldPointer);
                //同增同删，删除被提交的数据,指针减一
                fillRecorders.remove(getFillRecorder(checkRecorders.get(oldPointer).getFillRecorderId()));
                checkRecorders.remove(oldPointer);
                topTabs.removeTabAt(oldPointer);
                //读取内存中的检查记录
                readCheckRecorderFromMemory(pointer);
                //读取内存中的充装记录
                readFillRecorderFromMemory(pointer);
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
                                mFillRecorderHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 0;
                                mFillRecorderHandler.sendMessage(msg);
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
                    checkRecorders.get(pointer).setSurfaceBefore(1);
                } else {
                    checkRecorders.get(pointer).setSurfaceBefore(0);
                }
            }
        });
        //设置充装前泄漏
        rgLeakBefore.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioLeakBeforeYes) {
                    //充装前外观
                    checkRecorders.get(pointer).setLeakBefore(1);
                } else {
                    checkRecorders.get(pointer).setLeakBefore(0);
                }
            }
        });
        //设置充装后外观
        rgSufaceAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioSufaceAfterGood) {
                    //充装前外观
                    checkRecorders.get(pointer).setSurfaceAfter(1);
                } else {
                    checkRecorders.get(pointer).setSurfaceAfter(0);
                }
            }
        });
        //设置充装后泄漏
        rgLeakAfter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioLeakAfterYes) {
                    //充装前外观
                    checkRecorders.get(pointer).setLeakAfter(1);
                } else {
                    checkRecorders.get(pointer).setLeakAfter(0);
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
    private void saveCheckRecorderToMemory(int _pointer)  {
        try {
        if (_pointer == -1)
            return;
        CheckRecorder checkRecorder = checkRecorders.get(_pointer);
        //气瓶号
        checkRecorder.setCylinderNumber(etCylinderNumber.getText().toString());
        //有效期
        String validDate = etValidDate.getText().toString();
        Log.i("validDate---:",validDate);
        if (validDate != null && validDate.length() == 8) {
                checkRecorder.setValidDate(formDateFormat.parse(validDate));
        }
        //枪编号
        checkRecorder.setGunCode(spGunCode.getSelectedItem().toString());
        //加气机编号
        checkRecorder.setGasMachineCode(tvGasMachineCode.getText().toString());
        //加气站编号
        checkRecorder.setGasName(tvGasName.getText().toString());
        //充装前外观
        checkRecorder.setSurfaceBefore(rgSurfaceBefore.getCheckedRadioButtonId()==R.id.radioSurfaceBeforeGood?1:0);
        //充装前泄漏
        checkRecorder.setLeakBefore(rgLeakBefore.getCheckedRadioButtonId()==R.id.radioLeakBeforeYes?1:0);
        //充装后外观
        checkRecorder.setSurfaceAfter(rgSufaceAfter.getCheckedRadioButtonId()==R.id.radioSufaceAfterGood?1:0);
        //充装后泄漏
        checkRecorder.setLeakAfter(rgLeakAfter.getCheckedRadioButtonId()==R.id.radioLeakAfterYes?1:0);
        //图片文件名,从签字返回的result中赋值
        //entity.setSignImage(imageFileName);
        //检查员
        checkRecorder.setCheckOperator(spCheckOperator.getSelectedItem().toString());
        //充装员
        checkRecorder.setFillOperator(spFillOperator.getSelectedItem().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    //从内存中读取当前检查数据
    private void readCheckRecorderFromMemory(int _pointer)  {
        if (checkRecorders.size()<1){
            //情况页面数据
            etCylinderNumber.setText("");
            etValidDate.setText("");
            return;
        }

        CheckRecorder checkRecorder = checkRecorders.get(_pointer);
        //气瓶号
        etCylinderNumber.setText(checkRecorder.getCylinderNumber());
        //有效期
        Date tempDate = checkRecorder.getValidDate();
        //如果日期为空
        if (tempDate == null)
            etValidDate.setText("");
            //否则
        else {
            etValidDate.setText(formDateFormat.format(tempDate));
        }
        //枪编号
        Util.setSpinnerSelectItem(spGunCode, checkRecorder.getGunCode());
        //充装前外观
        if(checkRecorder.getSurfaceBefore()==1)
             rgSurfaceBefore.check(R.id.radioSurfaceBeforeGood);
        else
            rgSurfaceBefore.check(R.id.radioSurfaceBeforeBad);
        //充装前泄漏
        if(checkRecorder.getLeakBefore()==1)
            rgLeakBefore.check(R.id.radioLeakBeforeYes);
        else
            rgLeakBefore.check(R.id.radioLeakBeforeNo);
        //充装后外观
        if(checkRecorder.getSurfaceAfter()==1)
            rgSufaceAfter.check(R.id.radioSufaceAfterGood);
        else
            rgSufaceAfter.check(R.id.radioSufaceAfterBad);
        //充装后泄漏
        if(checkRecorder.getLeakAfter()==1)
            rgLeakAfter.check(R.id.radioLeakAfterYes);
        else
            rgLeakAfter.check(R.id.radioLeakAfterNo);
        //设置检查员
        Util.setSpinnerSelectItem(spCheckOperator, checkRecorder.getCheckOperator());
        //设置充装员
        Util.setSpinnerSelectItem(spFillOperator, checkRecorder.getFillOperator());
        //设置签名，图片文件名,从签字返回的result中赋值
        String imageFileName = checkRecorder.getSignImage();
        if (imageFileName != null && Util.fileExists(imageFileName)) {
            //显示签名图片
            Bitmap bmp = Util.getBitmapThumbnail(imageFileName, 150, 105);
            Util.releaseBitmap(signImage);
            signImage.setImageBitmap(bmp);
        } else
            signImage.setImageDrawable(null);
    }

    //读取充装记录
    private void readFillRecorderFromMemory(int _point){
        Log.i("--------",String.valueOf(_point));
        if(checkRecorders==null||checkRecorders.size() == 0||checkRecorders.get(_point)==null||checkRecorders.get(_point).getFillRecorderId()<0){
            tvFillDataInfo.setText("");
        }else{
            JSONObject fillData =this.getFillRecorder(checkRecorders.get(_point).getFillRecorderId());
            String info = "开始时间:" + Util.converDateTimeFormat(fillData.get("startTime").toString()) + "  余压:" + fillData.get("pressBefore").toString() +
                    "\r\n气源压力:" + fillData.get("gasPress").toString() + "  充装量:" + fillData.get("gasCubage").toString() +
                    "\r\n结束时间:" + Util.converDateTimeFormat(fillData.get("endTime").toString()) + "  充装后压力:" + fillData.get("pressAfter").toString();
            tvFillDataInfo.setText(info);
        }
        return;
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
            checkRecorders.get(pointer).setSignImage(imageFileName);
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

    //上传数据
    private void uploadCheckRecorder(int _pointer) {
        //校正上传图片的文件名，去掉路径部分
       final String signImageFileName = checkRecorders.get(_pointer).getSignImage();
        if(signImageFileName==null){
            Toast.makeText(this,"缺少司机签名，不能提交",Toast.LENGTH_SHORT).show();
            return;
        }
        //校正后的名字
        String _signImageFileName =signImageFileName.substring(signImageFileName.indexOf("sign-"),signImageFileName.length());
        checkRecorders.get(_pointer).setSignImage(_signImageFileName);
        //在充装记录中增加上传时间
        checkRecorders.get(_pointer).setCheckDateTime(new Timestamp(System.currentTimeMillis()));
        final JSONObject checkRecorder = JSONObject.parseObject(JSONObject.toJSONString(checkRecorders.get(_pointer)));
        Log.i("-----",checkRecorder.toJSONString().toString());
        //上传CheckRecorder
        mUploadCheckRecordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpPost httpPost = new HttpPost(ConfigConstant.serverPort + "/check/add");
                    httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
                    StringEntity entity = new StringEntity(checkRecorder.toString(), "utf-8");
                    entity.setContentEncoding("UTF-8");
                    httpPost.setEntity(entity);
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResp = httpClient.execute(httpPost);
                    if (httpResp != null) {
                        if (httpResp.getStatusLine().getStatusCode() == 200)
                            Log.i("---", "上传成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mUploadCheckRecordThread.start();

        //上传签名图片
        mUploadSignThread=new Thread(new Runnable() {
            @Override
            public void run() {
                Util.uploadFile(signImageFileName);
            }
        });
        mUploadSignThread.start();
    }

    //在充装记录中查找
    private JSONObject getFillRecorder(int id){
        JSONObject ret =null;
        for (JSONObject recorder : this.fillRecorders) {
            if((int)recorder.get("id")==id){
                ret =recorder;
                break;
            }
        }
        return ret;
    }
}





