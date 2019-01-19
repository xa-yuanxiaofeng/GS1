package com.browse.gs.adpter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.browse.gs.R;

//Spinnerde的适配器，控制器
public class MySpinnerAdapter extends ArrayAdapter {

    private Context mContext;
    private String [] mStringArray;
    public MySpinnerAdapter(Context context, String[] stringArray) {
        super(context, R.layout.spinner_item, stringArray);
        mContext = context;
        mStringArray=stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent,false);
        }
        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.spinner_dropdown_item_text);
        tv.setText(mStringArray[position]);
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(R.id.spinner_item_text);
        tv.setText(mStringArray[position]);
        return convertView;
    }

}
