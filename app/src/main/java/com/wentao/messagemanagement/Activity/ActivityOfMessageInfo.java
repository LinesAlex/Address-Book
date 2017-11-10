package com.wentao.messagemanagement.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.MessageInfo;
import com.wentao.messagemanagement.tool.GetContactsInfo;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ActivityOfMessageInfo extends AppCompatActivity{
    private ListView lv_message_page;
    private EditText et_enter_message;
    MessageInfoAdapter messageInfoAdapter;
//    private ArrayList<MessageInfo> originalMessageInfos;//// TODO: 2017/11/10
    private String phone, id;
    private static ActivityOfMessageInfo instance;
    public static ActivityOfMessageInfo getInstance() {return instance;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_infopage);
        instance = ActivityOfMessageInfo.this;
        initView();
        setListView();
    }
    private void initView() {
        lv_message_page = (ListView) findViewById(R.id.lv_message_page);
        lv_message_page.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        et_enter_message = (EditText) findViewById(R.id.et_enter_message);
        ImageButton btn_send_message = (ImageButton) findViewById(R.id.btn_send_message);
        btn_send_message.setOnClickListener(new OnClickViewListener() );
    }
    private void setListView() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        id = intent.getStringExtra("id");

        GetContactsInfo.getMessageInfo(phone, id, ActivityOfMessageInfo.this);
        Collections.reverse(GetContactsInfo.MessageInfos);
        messageInfoAdapter = new MessageInfoAdapter(ActivityOfMessageInfo.this, R.layout.messageinfo_item, GetContactsInfo.MessageInfos);
        lv_message_page.setAdapter(messageInfoAdapter);
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

                        refresh();
                    }
                }break;
            }
        }
    }
    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetContactsInfo.getMessageInfo(phone, id, ActivityOfMessageInfo.this);
                        Collections.reverse(GetContactsInfo.MessageInfos);
                        messageInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
