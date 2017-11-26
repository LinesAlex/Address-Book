package com.wentao.messagemanagement.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.Adapter.CallInfoAdapter;
import com.wentao.messagemanagement.db.input.Intro;
import com.wentao.messagemanagement.db.input.MContacts;
import com.wentao.messagemanagement.tool.DataHandler;
import com.wentao.messagemanagement.tool.ContactsHandler;
import com.wentao.messagemanagement.R;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsPage extends AppCompatActivity {


    private ListView lv_phone_call, lv_message;
    private Button btn_show_call;
    private Button btn_show_message;
    private TextView none_call_info, none_message_info, intro_name, intro_phone, intro_email , intro_address, intro_job, intro_age;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private static ContactsPage instance;
    public static ContactsPage getInstance() {
        return instance;
    }
    private int[] imageId = new int[]{R.drawable.background_1,
            R.drawable.background_2,
            R.drawable.background_3,
            R.drawable.background_4};
    private String id, phoneNumber;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_contacts_info);
        instance = ContactsPage.this;
        //------------------------------------------------------------------------------------------
        none_call_info = (TextView) findViewById(R.id.none_call_info);
        none_message_info = (TextView) findViewById(R.id.none_message_info);

        Button btn_call_page = (Button) findViewById(R.id.btn_call_page);
        lv_phone_call = (ListView) findViewById(R.id.lv_phone_call);
        btn_show_call = (Button) findViewById(R.id.btn_show_call);

        Button btn_message_page = (Button) findViewById(R.id.btn_message_page);
        lv_message = (ListView) findViewById(R.id.lv_message);
        btn_show_message = (Button) findViewById(R.id.btn_show_message);

        intro_name = (TextView) findViewById(R.id.intro_name);
        intro_phone = (TextView) findViewById(R.id.intro_phone);
        intro_email = (TextView) findViewById(R.id.intro_email);
        intro_address = (TextView) findViewById(R.id.intro_address);
        intro_job = (TextView) findViewById(R.id.intro_job);
        intro_age = (TextView) findViewById(R.id.intro_age);

        ImageView iv_background = (ImageView) findViewById(R.id.background_image_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        FloatingActionButton btn_to_add = (FloatingActionButton) findViewById(R.id.btn_to_add);
        FloatingActionButton btn_to_delete = (FloatingActionButton) findViewById(R.id.btn_to_delete);
        //-------------------------------------初始化------------------------------------------------
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        phoneNumber = DataHandler.getPhone(id);
        String name = DataHandler.getName(id);
        String email = DataHandler.getEmail(id);
        Intro intro = DataHandler.getIntro(id);
        if (intro != null) {
            intro_name.setText(check(intro.getName()));
            intro_phone.setText(check(intro.getPhone()));
            intro_email.setText(check(intro.getEmail()));
            intro_address.setText(check(intro.getAddress()));
            intro_job.setText(check(intro.getJob()));
            intro_age.setText(check(intro.getAge()));
        } else {
            intro_phone.setText(phoneNumber);
            intro_name.setText(name);
            intro_email.setText(email);
            DataHandler.saveIntro(id, new String[] {phoneNumber, name, email, "", "", ""});
        }

        OnClickButtonListener listener = new OnClickButtonListener();
        btn_show_call.setOnClickListener(listener);
        btn_show_message.setOnClickListener(listener);
        none_message_info.setOnClickListener(listener);
        btn_message_page.setOnClickListener(listener);
        btn_call_page.setOnClickListener(listener);
        btn_to_add.setOnClickListener(listener);
        btn_to_delete.setOnClickListener(listener);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iv_background.setImageResource(imageId[Integer.parseInt(id) % 4]);
        collapsingToolbarLayout.setTitle(name);
        //-----------------------------------------设置----------------------------------------------
        //通话信息ListView设置
        if (!(phoneNumber.isEmpty() && phoneNumber.equals(""))){
            ContactsHandler.getCallInfo(phoneNumber,id);
            CallInfoAdapter phoneInfoAdapter = new CallInfoAdapter(ContactsPage.this, R.layout.item_call_info, ContactsHandler.CallInfos);
            lv_phone_call.setAdapter(phoneInfoAdapter);
            int height = lv_phone_call.getLayoutParams().height;
            int size = ContactsHandler.CallInfos.size();
            ViewGroup.LayoutParams params = lv_phone_call.getLayoutParams();
            params.height = height * size;
            lv_phone_call.setLayoutParams(params);
            //finish

            //短信消息ListView设置
            ContactsHandler.getMessageInfo(phoneNumber, id, ContactsPage.this);
            Collections.reverse(ContactsHandler.MessageInfos);
            MessageInfoAdapter messageInfoAdapter = new MessageInfoAdapter(ContactsPage.this, R.layout.item_message_info, ContactsHandler.MessageInfos);
            lv_message.setAdapter(messageInfoAdapter);
            height = lv_message.getLayoutParams().height;
            size = ContactsHandler.MessageInfos.size();
            params = lv_message.getLayoutParams();
            if (size > 5) {
                params.height = height * 5;
            } else {
                params.height = height * size;
            }
            lv_message.setLayoutParams(params);
            //------------------------------------------设置ListView-------------------------------------
        }
    }


    private String check(String str) {
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
                    if(ActivityCompat.checkSelfPermission(ContactsList.getInstance(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        ContactsList.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_show_call : {
                    if (lv_phone_call.getVisibility() == View.GONE) {
                        btn_show_call.setBackgroundResource(R.drawable.button_show_menu_2);
                        lv_phone_call.setVisibility(View.VISIBLE);
                        none_call_info.setVisibility(View.VISIBLE);
                    } else {
                        btn_show_call.setBackgroundResource(R.drawable.button_show_menu_1);
                        lv_phone_call.setVisibility(View.GONE);
                        none_call_info.setVisibility(View.GONE);
                    }
                } break;
                case R.id.btn_message_page :
                case R.id.none_message_info : {
                    Intent intent = new Intent(ContactsPage.this, MessagePage.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }break;
                case R.id.btn_show_message : {
                    if (lv_message.getVisibility() == View.GONE) {
                        btn_show_message.setBackgroundResource(R.drawable.button_show_menu_2);
                        lv_message.setVisibility(View.VISIBLE);
                        none_message_info.setVisibility(View.VISIBLE);
                    } else {
                        btn_show_message.setBackgroundResource(R.drawable.button_show_menu_1);
                        lv_message.setVisibility(View.GONE);
                        none_message_info.setVisibility(View.GONE);
                    }
                }break;
                case R.id.btn_to_add :{
                    Intent intent = new Intent(ContactsPage.this, AddContact.class);
                    intent.putExtra("Flag", true);
                    intent.putExtra("id", id);
                    startActivityForResult(intent, 2);
                }break;
                case R.id.btn_to_delete :{
                    AlertDialog.Builder dialog = new AlertDialog.Builder (ContactsPage.this);
                    dialog.setTitle("删除联系人");
                    dialog.setMessage("是否要删除此联系人 ?");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("name", DataHandler.getName(id));
                            setResult(RESULT_OK, intent);
                            DataHandler.deleteData(id);
                            finish();
                        }
                    });
                    dialog.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                }
            }
        }
    }

    //菜单返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //获取返回菜单
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
                        intro_name.setText(check(intro.getName()));
                        intro_phone.setText(check(intro.getPhone()));
                        intro_email.setText(check(intro.getEmail()));
                        intro_address.setText(check(intro.getAddress()));
                        intro_job.setText(check(intro.getJob()));
                        intro_age.setText(check(intro.getAge()));
                        collapsingToolbarLayout.setTitle(check(intro.getName()));
                        super.onRestart();
                    }
                }break;
            default : break;
        }
    }
}