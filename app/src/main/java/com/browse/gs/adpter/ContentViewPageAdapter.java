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

import java.util.ArrayList;
import java.util.Arrays;

public class ContentViewPageAdapter extends FragmentPagerAdapter {
    ArrayList<String> tabs =new ArrayList<>(Arrays.asList("首页","资讯","直播","我"));

    public ContentViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

    @Override
    public Fragment getItem(int i) {
        return ContentFragment.newInstance(tabs.get(i));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }
}
