package com.wentao.messagemanagement.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.Adapter.PhoneInfoAdapter;
import com.wentao.messagemanagement.db.Intro;
import com.wentao.messagemanagement.tool.GetContactsInfo;
import com.wentao.messagemanagement.R;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ActivityOfContactsInfo extends AppCompatActivity {


    private Toolbar toolbar;
    private ImageView iv_background;
    private ListView lv_phone_call, lv_message;
    private Button btn_call_page, btn_show_call, btn_message_page,btn_show_message;
    private TextView none_call_info, none_message_info, intro_name, intro_phone, intro_email , intro_address, intro_job, intro_age;
    private FloatingActionButton btn_to_add;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static ActivityOfContactsInfo instance;
    public static ActivityOfContactsInfo getInstance() {
        return instance;
    }
    private int[] imageId = new int[]{R.drawable.background_1,
            R.drawable.background_2,
            R.drawable.background_3,
            R.drawable.background_4};
    private String phoneNumber, id, name;
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

        btn_to_add = (FloatingActionButton) findViewById(R.id.btn_to_add);
    }

    private void setView() {
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phone");
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String count = intent.getStringExtra("count");

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iv_background.setImageResource(imageId[Integer.parseInt(count) % 4]);
        collapsingToolbarLayout.setTitle(name);
        setListView();
        if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            Intro intro = intros.get(0);
            intro_name.setText(checkOutIntroInfo(intro.getName()));
            intro_phone.setText(checkOutIntroInfo(intro.getPhone()));
            intro_email.setText(checkOutIntroInfo(intro.getEmail()));
            intro_address.setText(checkOutIntroInfo(intro.getAddress()));
            intro_job.setText(checkOutIntroInfo(intro.getJob()));
            intro_age.setText(checkOutIntroInfo(intro.getAge()));
        } else {
            Intro intro = new Intro();
            String n = checkOutIntroInfo(name);
            String p = checkOutIntroInfo(phoneNumber);
            String e = checkOutIntroInfo(email);
            intro_name.setText(n);
            intro_phone.setText(p);
            intro_email.setText(e);
            intro_address.setText("");
            intro_job.setText("");
            intro_age.setText("");
            intro.setName(n);
            intro.setPhone(p);
            intro.setEmail(e);
            intro.setMid(id);
            intro.save();
        }
        btn_show_call.setOnClickListener(new OnClickButtonListener());
        btn_show_message.setOnClickListener(new OnClickButtonListener());
        none_message_info.setOnClickListener(new OnClickButtonListener());
        btn_message_page.setOnClickListener(new OnClickButtonListener());
        btn_call_page.setOnClickListener(new OnClickButtonListener());
        btn_to_add.setOnClickListener(new OnClickButtonListener());
    }

    private void setListView() {
        //通话信息ListView设置
        GetContactsInfo.getCallInfo(phoneNumber,id);
        PhoneInfoAdapter phoneInfoAdapter = new PhoneInfoAdapter(ActivityOfContactsInfo.this, R.layout.callinfo_item, GetContactsInfo.CallInfos);
        lv_phone_call.setAdapter(phoneInfoAdapter);
        int height = lv_phone_call.getLayoutParams().height;
        int size = GetContactsInfo.CallInfos.size();
        ViewGroup.LayoutParams params = lv_phone_call.getLayoutParams();
        params.height = height * size;
        lv_phone_call.setLayoutParams(params);
        //finish

        //短信消息ListView设置
        GetContactsInfo.getMessageInfo(phoneNumber, id, ActivityOfContactsInfo.this);
        Collections.reverse(GetContactsInfo.MessageInfos);
        MessageInfoAdapter messageInfoAdapter = new MessageInfoAdapter(ActivityOfContactsInfo.this, R.layout.messageinfo_item, GetContactsInfo.MessageInfos);
        lv_message.setAdapter(messageInfoAdapter);
        height = lv_message.getLayoutParams().height;
        size = GetContactsInfo.MessageInfos.size();
        params = lv_message.getLayoutParams();
        if (size > 5) {
            params.height = height * 5;
        } else {
            params.height = height * size;
        }
        lv_message.setLayoutParams(params);
    }

    private String checkOutIntroInfo(String str) {
        if (str == null || str.contains("NULL")) {
            return "";
        } else {
            return str;
        }
    }

    private class OnClickButtonListener implements View.OnClickListener {
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
                case R.id.btn_message_page :
                case R.id.none_message_info : {
                    Intent intent = new Intent(ActivityOfContactsInfo.this, ActivityOfMessageInfo.class);
                    intent.putExtra("phone", phoneNumber);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    startActivity(intent);
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
                case R.id.btn_to_add :{
                    Intent intent = new Intent(ActivityOfContactsInfo.this, ActivityOfAddContact.class);
                    intent.putExtra("Flag", true);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    startActivityForResult(intent, 2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 2 :
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    Snackbar.make(collapsingToolbarLayout, "联系人 " + name + " 修改成功!", Snackbar.LENGTH_SHORT).show();
                    if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
                        List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
                        Intro intro = intros.get(0);
                        intro_name.setText(checkOutIntroInfo(intro.getName()));
                        intro_phone.setText(checkOutIntroInfo(intro.getPhone()));
                        intro_email.setText(checkOutIntroInfo(intro.getEmail()));
                        intro_address.setText(checkOutIntroInfo(intro.getAddress()));
                        intro_job.setText(checkOutIntroInfo(intro.getJob()));
                        intro_age.setText(checkOutIntroInfo(intro.getAge()));
                        collapsingToolbarLayout.setTitle(checkOutIntroInfo(intro.getName()));
                        super.onRestart();
                    }
                }break;
            default : break;
        }
    }
}