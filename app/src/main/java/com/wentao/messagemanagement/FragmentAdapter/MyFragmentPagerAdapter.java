package com.wentao.messagemanagement.FragmentAdapter;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private Context context;
    private String[] titles;
    public MyFragmentPagerAdapter(FragmentManager fm, Context context
            , List<Fragment>  fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.context = context;
        this.titles = titles;
    }

    @Override
    public  Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
