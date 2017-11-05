package com.wentao.messagemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ActivityOfContactsInfo extends AppCompatActivity {
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_email;
    private TextView tv_count;
    private Button btn_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_info);
        tv_name = (TextView) findViewById(R.id.tv_name_info);
        tv_phone = (TextView) findViewById(R.id.tv_phone_info);
        tv_email = (TextView) findViewById(R.id.tv_email_info);
        tv_count = (TextView) findViewById(R.id.tv_count_info);
        Intent intent = getIntent();
        tv_name.setText(intent.getStringExtra("name"));
        tv_count.setText(intent.getStringExtra("count"));
        tv_phone.setText("Phone\n    " + intent.getStringExtra("phone").substring(7));
        tv_email.setText("Email\n    " + intent.getStringExtra("email").substring(7));
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
