package com.browse.gs;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public  class MyFragment extends Fragment {
    private Context context;
    private String content;
    private View rootView;
    public MyFragment() {
    }

    @SuppressLint("ValidFragment")
    public MyFragment(Context contexts, String content) {
        this.context = contexts;
        this.content = content;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.activity_content, null);
        }
        return rootView;
    }

    public String getContent() {
        return content;
    }
}