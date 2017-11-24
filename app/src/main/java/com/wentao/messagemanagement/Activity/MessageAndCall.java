package com.wentao.messagemanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wentao.messagemanagement.Fragment.CallPageFragment;
import com.wentao.messagemanagement.Fragment.MessagePageFragment;
import com.wentao.messagemanagement.FragmentAdapter.MyFragmentPagerAdapter;
import com.wentao.messagemanagement.R;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/11/14.
 */

public class MessageAndCall extends AppCompatActivity {
    private static MessageAndCall instance;
    public static MessageAndCall getInstance() {
        return instance;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_message_and_call);
        instance = MessageAndCall.this;
        FloatingActionButton floatingActionButton =(FloatingActionButton) findViewById(R.id.btn_contact);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.this, ContactsList.class);
                startActivity(intent);
            }
        });
//--------------------------------------------------------------------------------------------------
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new CallPageFragment());
        fragmentList.add(new MessagePageFragment());
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(
                getSupportFragmentManager()
                , MessageAndCall.this
                , fragmentList
                , new String[] {"电话", "短信"});
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
