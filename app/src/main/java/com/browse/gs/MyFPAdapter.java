package com.browse.gs;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import java.util.ArrayList;

class MyFPAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private TabLayout tabLayout;
    public MyFPAdapter(ArrayList<Fragment> fragments, TabLayout tabLayout, FragmentManager fm) {
        super(fm);
        this.fragments = fragments;
        this.tabLayout = tabLayout;
    }

    @Override
    public MyFragment getItem(int position) {
        return (MyFragment) fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    //显示标签上的文字
    @Override
    public CharSequence getPageTitle(int position) {
        Object o =tabLayout.getTabAt(position);
        return tabLayout.getTabAt(position).getText();
    }
}
