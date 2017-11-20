package com.wentao.messagemanagement.FragmentAdapter;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] titles;
    private Context context;
    public MyFragmentPagerAdapter(FragmentManager fm, Context context, List<Fragment>  fragments, String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        this.context = context;
    }
    @Override
    public  Fragment getItem(int position) {
        Log.i("FragmentAdapter : ", "do this");
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
