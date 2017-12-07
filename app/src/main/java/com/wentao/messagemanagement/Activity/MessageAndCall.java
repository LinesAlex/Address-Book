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
import android.view.animation.AnimationUtils;

import com.wentao.messagemanagement.Animation.AnimationUtil;
import com.wentao.messagemanagement.Fragment.CallPageFragment;
import com.wentao.messagemanagement.Fragment.MessagePageFragment;
import com.wentao.messagemanagement.FragmentAdapter.MyFragmentPagerAdapter;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.tool.DataHandler;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/11/14.
 */

public class MessageAndCall extends AppCompatActivity {
    private static MessageAndCall instance;
    private boolean FlagOfGone = false;
    public static MessageAndCall getInstance() {
        return instance;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_message_and_call);
        instance = MessageAndCall.this;
        final FloatingActionButton fbtn_contact =(FloatingActionButton) findViewById(R.id.btn_contact);
        final FloatingActionButton fbtn_dial = (FloatingActionButton) findViewById(R.id.btn_dial);
        final FloatingActionButton fbtn_write = (FloatingActionButton) findViewById(R.id.btn_send);
        final FloatingActionButton[] buttons = new FloatingActionButton[]{fbtn_contact, fbtn_dial, fbtn_write};
        FloatingActionButton fbtn_more = (FloatingActionButton) findViewById(R.id.btn_more);

        fbtn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.this, ContactsList.class);
                startActivity(intent);
                setButtonGone(buttons, true);
            }
        });

        fbtn_dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.this, DialPage.class);
                startActivity(intent);
                setButtonGone(buttons, true);
            }
        });

        fbtn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.this, SendMessagePage.class);
                startActivity(intent);
                setButtonGone(buttons, true);
            }
        });
        fbtn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FlagOfGone) {
                    setButtonGone(buttons, true);
                } else {
                    setButtonGone(buttons, false);
                }
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
        DataHandler.init(instance);
    }

    public void setButtonGone(FloatingActionButton[] button, boolean flag){
        if (flag) {
            for (FloatingActionButton aButton : button) {
                AnimationUtil.setAnimationLeftToRightGone(aButton, instance);
            }
            FlagOfGone = false;
        } else {
            for (FloatingActionButton aButton : button) {
                AnimationUtil.setAnimationLeftToRightGone(aButton, instance);
            }
            FlagOfGone = true;
        }
    }
}
