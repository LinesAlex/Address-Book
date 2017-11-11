package com.wentao.messagemanagement.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.MessageReceiver;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.MessageInfo;
import com.wentao.messagemanagement.tool.GetContactsInfo;
import com.wentao.messagemanagement.tool.MTime;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ActivityOfMessageInfo extends AppCompatActivity{
    private ListView lv_message_page;
    private EditText et_enter_message;
    private Toolbar toolbar;
    private ImageButton btn_send_message;
    private MessageInfoAdapter messageInfoAdapter;
    private String phone, id, name;
    private static ActivityOfMessageInfo instance;
    public static ActivityOfMessageInfo getInstance() {return instance;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_infopage);
        instance = ActivityOfMessageInfo.this;
        initView();
        setView();
        setListView();
    }
    private void initView() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        lv_message_page = (ListView) findViewById(R.id.lv_message_page);
        lv_message_page.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        et_enter_message = (EditText) findViewById(R.id.et_enter_message);
        toolbar = (Toolbar) findViewById(R.id.tb_title_message);
        btn_send_message = (ImageButton) findViewById(R.id.btn_send_message);

    }
    private void setView() {
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}
        btn_send_message.setOnClickListener(new OnClickViewListener());
    }
    private void setListView() {
        GetContactsInfo.getMessageInfo(phone, id, ActivityOfMessageInfo.this);
        messageInfoAdapter = new MessageInfoAdapter(ActivityOfMessageInfo.this, R.layout.messageinfo_item, GetContactsInfo.MessageInfos);
        lv_message_page.setAdapter(messageInfoAdapter);
        MessageReceiver.setAdapter(messageInfoAdapter);
    }
    private class OnClickViewListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send_message: {
                    String message = et_enter_message.getText().toString();
                    if (!message.isEmpty() && message.length() > 0) {
                        et_enter_message.setText("");
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phone, null, message, null, null);
                        MessageInfo messageInfo = new MessageInfo(phone,message,MTime.getTime(),"送达");
                        GetContactsInfo.MessageInfos.add(messageInfo);
                        messageInfoAdapter.notifyDataSetChanged();
                    }
                }break;
            }
        }
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
