package com.browse.gs.adpter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.browse.gs.ContentFragment;
import com.browse.gs.MainActivity;

import java.util.ArrayList;

public class ContentViewPageAdapter extends FragmentPagerAdapter {
    ArrayList<String> tabs =new ArrayList<String>();
    ArrayList<ContentFragment> contents =new ArrayList<ContentFragment>();

    public ContentViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Fragment getItem(int i) {
        ContentFragment ret=null;
        ret = ContentFragment.newInstance(tabs.get(i));
        contents.add(ret);
        return ret;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
    public void addTab(String tabName){
        this.tabs.add(tabName);

    }
    public void removeTab(int position){
        this.tabs.remove(position);
        this.contents.remove(position);
        this.notifyDataSetChanged();

    }
}
