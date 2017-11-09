package com.wentao.messagemanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


    private Toolbar toolbar;
    private ImageView iv_background;
    private ListView lv_phone_call, lv_message;
    private Button btn_call_page, btn_show_call, btn_message_page,btn_show_message;
    private TextView none_call_info, none_message_info, intro_name, intro_phone, intro_email , intro_address, intro_job, intro_age;
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
        none_call_info = (TextView) findViewById(R.id.none_call_info);
        none_message_info = (TextView) findViewById(R.id.none_message_info);

        lv_phone_call = (ListView) findViewById(R.id.lv_phone_call);
        btn_call_page = (Button) findViewById(R.id.btn_call_page);
        btn_show_call = (Button) findViewById(R.id.btn_show_call);

        lv_message = (ListView) findViewById(R.id.lv_message);
        btn_message_page = (Button) findViewById(R.id.btn_message_page);
        btn_show_message = (Button) findViewById(R.id.btn_show_message);

        intro_name = (TextView) findViewById(R.id.intro_name);
        intro_phone = (TextView) findViewById(R.id.intro_phone);
        intro_email = (TextView) findViewById(R.id.intro_email);
        intro_address = (TextView) findViewById(R.id.intro_address);
        intro_job = (TextView) findViewById(R.id.intro_job);
        intro_age = (TextView) findViewById(R.id.intro_age);

        iv_background = (ImageView) findViewById(R.id.background_image_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
    }
    private void setListView(String phoneNumber, String id) {
        //通话信息ListView设置
        ArrayList<CallInfo> callInfos = GetContactsInfo.getCallInfo(phoneNumber,id);
        PhoneInfoAdapter phoneInfoAdapter = new PhoneInfoAdapter(ActivityOfContactsInfo.this, R.layout.callinfo_item, callInfos);
        lv_phone_call.setAdapter(phoneInfoAdapter);
        int height = lv_phone_call.getLayoutParams().height;
        int size = callInfos.size();
        ViewGroup.LayoutParams params = lv_phone_call.getLayoutParams();
        params.height = height * size;
        lv_phone_call.setLayoutParams(params);
        //finish

        //短信消息ListView设置

        ArrayList<MessageInfo> messageInfos = GetContactsInfo.getMessageInfo(phoneNumber, id);
        MessageInfoAdapter messageInfoAdapter = new MessageInfoAdapter(ActivityOfContactsInfo.this, R.layout.messageinfo_item, messageInfos);
        lv_message.setAdapter(messageInfoAdapter);
        height = lv_message.getLayoutParams().height;
        size = messageInfos.size();
        params = lv_message.getLayoutParams();
        params.height = height * size;
        lv_message.setLayoutParams(params);
    }
    private void setView() {
        Intent intent = getIntent();
        int id = 0;
        String phoneNumber = intent.getStringExtra("phone");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String count = intent.getStringExtra("count");
        intent.getIntExtra("id",id);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}
        iv_background.setImageResource(imageId[Integer.parseInt(count) % 4]);
        collapsingToolbarLayout.setTitle(name);
        setListView(phoneNumber,id + "");

        intro_name.setText(checkOutIntroInfo(name));
        intro_phone.setText(checkOutIntroInfo(phoneNumber));
        intro_email.setText(checkOutIntroInfo(email));
        intro_address.setText(checkOutIntroInfo(null));
        intro_job.setText(checkOutIntroInfo(null));
        intro_age.setText(checkOutIntroInfo(null));

        btn_show_call.setOnClickListener(new OnClickButtonListener());
        btn_show_message.setOnClickListener(new OnClickButtonListener());
        btn_message_page.setOnClickListener(new OnClickButtonListener(phoneNumber));
        btn_call_page.setOnClickListener(new OnClickButtonListener(phoneNumber));

    }

    private String checkOutIntroInfo(String str) {
        final String info = str;
        if (info == null || info.startsWith("NULL")) {
            return "...";
        } else {
            return info;
        }
    }

    class OnClickButtonListener implements View.OnClickListener {
        private String phoneNumber;
        public OnClickButtonListener(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
        public OnClickButtonListener() {super();}
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_call_page : {
                    if(ActivityCompat.checkSelfPermission(MainActivity.getInstance(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        MainActivity.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_show_call : {
                    if (lv_phone_call.getVisibility() == View.GONE) {
                        btn_show_call.setBackgroundResource(R.drawable.show_menu_bottom);
                        lv_phone_call.setVisibility(View.VISIBLE);
                        none_call_info.setVisibility(View.VISIBLE);
                    } else {
                        btn_show_call.setBackgroundResource(R.drawable.show_menu);
                        lv_phone_call.setVisibility(View.GONE);
                        none_call_info.setVisibility(View.GONE);
                    }
                } break;
                case R.id.btn_message_page : {
                    if(ActivityCompat.checkSelfPermission(MainActivity.getInstance(),
                            Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Uri uri = Uri.parse("smsto:" + phoneNumber);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "");
                        MainActivity.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_show_message : {
                    if (lv_message.getVisibility() == View.GONE) {
                        btn_show_message.setBackgroundResource(R.drawable.show_menu_bottom);
                        lv_message.setVisibility(View.VISIBLE);
                        none_message_info.setVisibility(View.VISIBLE);
                    } else {
                        btn_show_message.setBackgroundResource(R.drawable.show_menu);
                        lv_message.setVisibility(View.GONE);
                        none_message_info.setVisibility(View.GONE);
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
