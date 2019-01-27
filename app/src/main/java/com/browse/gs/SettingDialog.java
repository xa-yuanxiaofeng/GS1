package com.browse.gs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.browse.gs.adpter.MySpinnerAdapter;
import com.browse.gs.util.ConfigConstant;
import com.browse.gs.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.browse.gs.util.Util.getSharedPreference;

public class SettingDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;

    //加气站名称
    @BindView(R.id.gasName)
    public EditText gasName;

    //加气机编号
    @BindView(R.id.gasMachineCode)
    public Spinner gasMachineCode;

    //加气枪编号0
    @BindView(R.id.gunCode0)
    public Spinner gunCode0;
    //加气枪编号1
    @BindView(R.id.gunCode1)
    public Spinner gunCode1;
    //加气枪编号2
    @BindView(R.id.gunCode2)
    public Spinner gunCode2;
    //加气枪编号3
    @BindView(R.id.gunCode3)
    public Spinner gunCode3;


    //取消
    @BindView(R.id.btnCancel)
    public Button btnCancel;

    //保存
    @BindView(R.id.btnSave)
    public Button btnSave;

    private View.OnClickListener mClickListener;

    public SettingDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.setting_dialog);
        ButterKnife.bind(this);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);

        init();
        initEvent();
        this.setCancelable(true);
    }

    private void init(){
        gasMachineCode.setAdapter(new MySpinnerAdapter(context,context.getResources().getStringArray(R.array.gasMachineCode)));
        gunCode0.setAdapter(new MySpinnerAdapter(context,context.getResources().getStringArray(R.array.gunCode)));
        gunCode1.setAdapter(new MySpinnerAdapter(context,context.getResources().getStringArray(R.array.gunCode)));
        gunCode2.setAdapter(new MySpinnerAdapter(context,context.getResources().getStringArray(R.array.gunCode)));
        gunCode3.setAdapter(new MySpinnerAdapter(context,context.getResources().getStringArray(R.array.gunCode)));
        //读取本地设置
        readFromSharedPreference();
    }
    //初始化事件
    private void initEvent(){
        // 取消事件
        btnCancel.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //保存事件
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存到本地缓存
                saveToSharedPreference();
                //通知主页面设置加气站和加气机的名称
                ((MainActivity) context).gasName.setText(gasName.getText().toString());
                ((MainActivity) context).gasMachineCode.setText(gasMachineCode.getSelectedItem().toString());
                dismiss();
            }
        });
    }
    private void saveToSharedPreference(){
        //保存加气站的名称
        Util.setSharedPreference(context,"gasName",gasName.getText().toString());
        //保存加气机的编号
        Util.setSharedPreference(context,"gasMachineCode",gasMachineCode.getSelectedItem().toString());
        //加气枪的编号
        Util.setSharedPreference(context,"gunCode0",gunCode0.getSelectedItem().toString());
        Util.setSharedPreference(context,"gunCode1",gunCode1.getSelectedItem().toString());
        Util.setSharedPreference(context,"gunCode2",gunCode2.getSelectedItem().toString());
        Util.setSharedPreference(context,"gunCode3",gunCode3.getSelectedItem().toString());
    }

    //从本地配置文件中读取数据
    private void readFromSharedPreference(){
        //加气站名称
       String temp= getSharedPreference(context,"gasName");
        if(temp!=null&&!temp.equals(""))
            gasName.setText(temp);
        //加气机编号
        temp= getSharedPreference(context,"gasMachineCode");
        Log.i("---",temp);
        if(temp!=null&&!temp.equals(""))
            Util.setSpinnerSelectItem(gasMachineCode,temp);
        //加气枪0编号
        temp= getSharedPreference(context,"gunCode0");
        Log.i("---0",temp);
        if(temp!=null&&!temp.equals(""))
            Util.setSpinnerSelectItem(gunCode0,temp);
        //加气枪1编号
        temp= getSharedPreference(context,"gunCode1");
        Log.i("---1",temp);
        if(temp!=null&&!temp.equals(""))
            Util.setSpinnerSelectItem(gunCode1,temp);
        //加气枪2编号
        temp= getSharedPreference(context,"gunCode2");
        Log.i("---2",temp);
        if(temp!=null&&!temp.equals(""))
            Util.setSpinnerSelectItem(gunCode2,temp);
        //加气枪3编号
        temp= getSharedPreference(context,"gunCode3");
        Log.i("---3",temp);
        if(temp!=null&&!temp.equals(""))
            Util.setSpinnerSelectItem(gunCode3,temp);
    }

}
