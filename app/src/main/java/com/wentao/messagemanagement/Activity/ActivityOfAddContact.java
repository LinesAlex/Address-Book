package com.wentao.messagemanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.wentao.messagemanagement.R;

/**
 * Created by Administrator on 2017/11/11.
 */

public class ActivityOfAddContact extends AppCompatActivity {
    private static ActivityOfAddContact instance;
    public static ActivityOfAddContact getInstance() {
        return instance;
    }
    private boolean Flag;
    private TextView tv_contact_info;
    private EditText et_name, et_phone, et_email, et_address, et_job, et_age;
    private Toolbar toolbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact_page);
        instance = ActivityOfAddContact.this;
        initView();
        setView();
    }
    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name_add);
        et_phone = (EditText) findViewById(R.id.et_phone_add);
        et_email = (EditText) findViewById(R.id.et_email_add);
        et_address = (EditText) findViewById(R.id.et_address_add);
        et_job = (EditText) findViewById(R.id.et_job_add);
        et_age = (EditText) findViewById(R.id.et_age_add);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_contact_info = (TextView) findViewById(R.id.tv_contact_info);
    }
    private void setView() {
        Intent intent = getIntent();
        Flag = intent.getBooleanExtra("Flag",Flag);
        if (Flag) {
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            String email = intent.getStringExtra("email");
            String address = intent.getStringExtra("address");
            String job = intent.getStringExtra("job");
            String age = intent.getStringExtra("age");
            et_name.setText(name);
            et_phone.setText(phone);
            et_email.setText(email);
            et_address.setText(address);
            et_job.setText(job);
            et_age.setText(age);
            tv_contact_info.setText("修改联系人");
            toolbar.setTitle(name);
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}
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
