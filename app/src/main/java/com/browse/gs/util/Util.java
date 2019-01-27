package com.browse.gs.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.io.File;

public class Util {
    //取得本地存储
    public static String getSharedPreference(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(Constant.AppID, Context.MODE_PRIVATE);
        String temp = sp.getString(key, "");
        return temp;
    }

    //进行本地存储
    public static void setSharedPreference(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(Constant.AppID,  Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean fileExists(String path)
    {
        File file = new File(path);
        if(file.exists())
            return true;
        else
            return false;
    }

    public static Bitmap getBitmapThumbnail(String path, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = Math.min((int) (opts.outHeight / (float) height),
                (int) (opts.outWidth / (float) width));
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, opts);
        return bitmap;
    }

    public static void releaseBitmap(ImageView iv)
    {
        BitmapDrawable bd = (BitmapDrawable)iv.getDrawable();
        if(bd != null)
        {
            Bitmap bt = bd.getBitmap();
            if(bt != null && !bt.isRecycled())
                bt.recycle();
            bt = null;
        }
    }

    //根据spinner的值设定显示
    public static void setSpinnerSelectItem(Spinner spinner ,String value){
        int length = spinner.getAdapter().getCount();
        for(int i=0;i<length;i++) {
            if (spinner.getItemAtPosition(i).toString().equals(value)) {
                spinner.setSelection(i);
            }
        }
    }
    //根据radioGroup的值设定显示
    public static  void setRadoiGroupSelectItem(RadioGroup radioGroup,int value)
    {
        radioGroup.check(value);
    }
    //2018-09-09T04:19:12.000+0000 转为 2018-09-09 04:19:12
    public static String converDateTimeFormat(String d1){
        d1=d1.replace('T',' ');
        return d1.substring(0,d1.indexOf('.'));

    }
}
