package com.wentao.messagemanagement.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.input.Intro;
import com.wentao.messagemanagement.tool.GetContactsInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */

public class AddContact extends AppCompatActivity {
    private static AddContact instance;
    public static AddContact getInstance() {
        return instance;
    }
    private boolean Flag;
    private EditText et_name, et_phone, et_email, et_address, et_job, et_age;
    private String id;
    private boolean[] FlagOfInfo = new boolean[3];
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_add_contact);
        instance = AddContact.this;
        //------------------------------------------------------------------------------------------
        et_name = (EditText) findViewById(R.id.et_name_add);
        et_phone = (EditText) findViewById(R.id.et_phone_add);
        et_email = (EditText) findViewById(R.id.et_email_add);
        et_address = (EditText) findViewById(R.id.et_address_add);
        et_job = (EditText) findViewById(R.id.et_job_add);
        et_age = (EditText) findViewById(R.id.et_age_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton btn_right = (FloatingActionButton) findViewById(R.id.btn_right);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("简讯");
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView tv_contact_info = (TextView) findViewById(R.id.tv_contact_info);
        //------------------------------------------------------------------------------------------
        Intent intent = getIntent();
        Flag = intent.getBooleanExtra("Flag", Flag);
        id = intent.getStringExtra("id");
        if (Flag && DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            Intro intro = intros.get(0);
            FlagOfInfo[0] = intro.getName().equals("");
            FlagOfInfo[1] = intro.getPhone().equals("");
            FlagOfInfo[2] = intro.getEmail().equals("");
            et_name.setText(intro.getName());
            et_phone.setText(intro.getPhone());
            et_email.setText(intro.getEmail());
            et_address.setText(intro.getAddress());
            et_job.setText(intro.getJob());
            et_age.setText(intro.getAge());
            tv_contact_info.setText("修改联系人");
            toolbar.setTitle(intent.getStringExtra("name"));
        }
        btn_right.setOnClickListener(new OnClickButtonListener());
    }
    private class OnClickButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_right : {
                    Intro intro = new Intro();
                    String phone = check(et_phone.getText().toString());
                    String name = check(et_name.getText().toString());
                    String email = check(et_email.getText().toString());
                    if (phone.equals("") || phone.equals("")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder (AddContact.this);
                        dialog.setTitle("无法执行此操作");
                        dialog.setMessage("未填写姓名或电话号码!\n是否放弃编辑?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("重新编辑", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        dialog.show();
                    } else {
                        if (Flag) {//update
                            GetContactsInfo.update(id, phone, name, email, FlagOfInfo);
                            intro.setPhone(phone);
                            intro.setName(name);
                            intro.setEmail(email);
                            intro.setAddress(check(et_address.getText().toString()));
                            intro.setJob(check(et_job.getText().toString()));
                            intro.setAge(check(et_age.getText().toString()));
                            intro.updateAll("mid = ?", id);
                        } else {//insert
                            String id = GetContactsInfo.insert(phone, name, email);
                            intro.setMid(id);
                            intro.setPhone(phone);
                            intro.setName(name);
                            intro.setEmail(email);
                            intro.setAddress(check(et_address.getText().toString()));
                            intro.setJob(check(et_job.getText().toString()));
                            intro.setAge(check(et_age.getText().toString()));
                            intro.save();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("name", name);
                        setResult(RESULT_OK, intent);
                        finish();
                    }break;
                }
            }
        }
    }
    private String check(String str) {
        if (str == null || str.contains("NULL")) {
            return "";
        } else {
            return str;
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
