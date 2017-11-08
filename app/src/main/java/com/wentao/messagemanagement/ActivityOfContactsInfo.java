package com.wentao.messagemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.Adapter.PhoneInfoAdapter;
import com.wentao.messagemanagement.db.CallInfo;
import com.wentao.messagemanagement.db.MessageInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ActivityOfContactsInfo extends AppCompatActivity {
//    private TextView tv_phone_info;
    private Toolbar toolbar;
    private ImageView iv_background;
    private ListView lv_phone_call,lv_message;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static ActivityOfContactsInfo instance;
    public static ActivityOfContactsInfo getInstance() {
        return instance;
    }
    private int[] imageId = new int[]{R.drawable.background_1,
            R.drawable.background_2,
            R.drawable.background_3,
            R.drawable.background_4};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_infopage);
        instance = ActivityOfContactsInfo.this;
        initView();
        setView();

    }
    private void initView() {
        lv_phone_call = (ListView) findViewById(R.id.lv_phone_call);
//        elv_message = (ExpandableListView) findViewById(R.id.elv_message);
//        tv_phone_info = (TextView) findViewById(R.id.tv_phone_info);
        iv_background = (ImageView) findViewById(R.id.background_image_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }
    private void setListView(String phoneNumber, String id) {
        ArrayList<CallInfo> callInfos = GetContactsInfo.getCallInfo(phoneNumber,id);
//        ArrayList<MessageInfo> messageInfos = new ArrayList<>();
        PhoneInfoAdapter phoneInfoAdapter = new PhoneInfoAdapter(ActivityOfContactsInfo.this, R.layout.callinfo_item, callInfos);
        lv_phone_call.setAdapter(phoneInfoAdapter);
        //MessageInfoAdapter messageInfoAdapter = new MessageInfoAdapter(ActivityOfContactsInfo.this, R.layout.messageinfo_item, messageInfos);
        //elv_message.setAdapter(messageInfoAdapter);
    }
    private void setView() {
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra("phone");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iv_background.setImageResource(imageId[Integer.parseInt(intent.getStringExtra("count")) % 4]);
//        tv_phone_info.setText("Phone\n    " + phoneNumber);
        collapsingToolbarLayout.setTitle(intent.getStringExtra("name"));
        setListView(phoneNumber,intent.getStringExtra("id"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
