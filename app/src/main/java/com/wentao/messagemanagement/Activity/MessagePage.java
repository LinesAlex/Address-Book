package com.wentao.messagemanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.Receiver.MessageReceiver;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.MessageInfo;
import com.wentao.messagemanagement.tool.DataHandler;
import com.wentao.messagemanagement.tool.ContactsHandler;
import com.wentao.messagemanagement.tool.TimeTool;

import java.util.Objects;

/**
 * Created by Administrator on 2017/11/10.
 */

public class MessagePage extends AppCompatActivity{
    private EditText et_enter_message;
    private MessageInfoAdapter messageInfoAdapter;
    private static MessagePage instance;
    public static MessagePage getInstance() {return instance;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_message_info);
        instance = MessagePage.this;
        //------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        final String phone;
        final String name;
        final String id = intent.getStringExtra("id");
        if (Objects.equals(id, "") || id == null){
            phone = intent.getStringExtra("phone");
            name = phone;
        } else {
            phone = DataHandler.getPhone(id);
            name = DataHandler.getName(id);
        }

        ListView lv_message_page = (ListView) findViewById(R.id.lv_message_page);
        lv_message_page.setDivider(null);
        lv_message_page.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        et_enter_message = (EditText) findViewById(R.id.et_enter_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_title_message);
        Button btn_send_message = (Button) findViewById(R.id.btn_send_message);
        //------------------------------------------------------------------------------------------
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}
        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_enter_message.getText().toString();
                if (!message.isEmpty() && message.length() > 0) {
                    et_enter_message.setText("");
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(phone, null, message, null, null);
                    MessageInfo messageInfo = new MessageInfo(phone,message, TimeTool.getTime(),"送达");
                    ContactsHandler.MessageInfos.add(messageInfo);
                    messageInfoAdapter.notifyDataSetChanged();
                }
            }
        });
        //------------------------------------------------------------------------------------------
        ContactsHandler.getMessageInfo(phone, id, MessagePage.this);
        messageInfoAdapter = new MessageInfoAdapter(MessagePage.this, R.layout.item_message_info, ContactsHandler.MessageInfos);
        lv_message_page.setAdapter(messageInfoAdapter);
        MessageReceiver.setAdapter(messageInfoAdapter);
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
