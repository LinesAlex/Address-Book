package com.wentao.messagemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ActivityOfContactsInfo extends AppCompatActivity {
    private TextView tv_phone;
    private Toolbar toolbar;
    private int[] imageId = new int[]{R.drawable.background_1,
            R.drawable.background_2,
            R.drawable.background_3,
            R.drawable.background_4};
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView iv_background;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_infopage);
        initView();
        setView();
    }
    private void initView(){
        tv_phone = (TextView) findViewById(R.id.tv_phone_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        iv_background = (ImageView) findViewById(R.id.background_image_view);

    }
    private void setView() {
        Intent intent = getIntent();
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        iv_background.setImageResource(imageId[Integer.parseInt(intent.getStringExtra("count")) % 4]);
        tv_phone.setText("Phone\n    " + intent.getStringExtra("phone"));
        collapsingToolbarLayout.setTitle(intent.getStringExtra("name"));
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
