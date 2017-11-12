package com.wentao.messagemanagement.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.Intro;

import org.litepal.crud.DataSupport;

import java.util.List;

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
    private FloatingActionButton btn_right;
    private EditText et_name, et_phone, et_email, et_address, et_job, et_age;
    private Toolbar toolbar;
    private String id;
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
        btn_right = (FloatingActionButton) findViewById(R.id.btn_right);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("简讯");
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}
        tv_contact_info = (TextView) findViewById(R.id.tv_contact_info);
    }
    private void setView() {
        Intent intent = getIntent();
        Flag = intent.getBooleanExtra("Flag", Flag);
        id = intent.getStringExtra("id");
        if (Flag && DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            Intro intro = intros.get(0);
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
    class OnClickButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_right : {
                    if (Flag) {
                        Intro intro = new Intro();
                        intro.setPhone(checkOutIntroInfo(et_phone.getText().toString()));
                        intro.setName(checkOutIntroInfo(et_name.getText().toString()));
                        intro.setEmail(checkOutIntroInfo(et_email.getText().toString()));
                        intro.setAddress(checkOutIntroInfo(et_address.getText().toString()));
                        intro.setJob(checkOutIntroInfo(et_job.getText().toString()));
                        intro.setAge(checkOutIntroInfo(et_age.getText().toString()));
                        intro.updateAll("mid = ?", id);
                    } else {
                        Intro intro = new Intro();
                        intro.setPhone(checkOutIntroInfo(et_phone.getText().toString()));
                        intro.setName(checkOutIntroInfo(et_name.getText().toString()));
                        intro.setEmail(checkOutIntroInfo(et_email.getText().toString()));
                        intro.setAddress(checkOutIntroInfo(et_address.getText().toString()));
                        intro.setJob(checkOutIntroInfo(et_job.getText().toString()));
                        intro.setAge(checkOutIntroInfo(et_age.getText().toString()));
                        intro.save();
                    }
                    finish();break;
                }
            }
        }
    }
    private String checkOutIntroInfo(String str) {
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
