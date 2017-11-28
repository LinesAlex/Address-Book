package com.wentao.messagemanagement.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.wentao.messagemanagement.Adapter.DialContactsAdapter;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.CallInfo;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.tool.DataHandler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class DialPage extends AppCompatActivity {
    private static DialPage instance;
    public static DialPage getInstance() {
        return instance;
    }
    private DialContactsAdapter adapter;
    private List<DialInfo> mDialList = new LinkedList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_dial);
        instance = DialPage.this;

        Button one = (Button) findViewById(R.id.one);
        Button two = (Button) findViewById(R.id.two);
        Button three = (Button) findViewById(R.id.three);
        Button four = (Button) findViewById(R.id.four);
        Button five = (Button) findViewById(R.id.five);
        Button six = (Button) findViewById(R.id.six);
        Button seven = (Button) findViewById(R.id.seven);
        Button eight = (Button) findViewById(R.id.eight);
        Button nine = (Button) findViewById(R.id.nine);
        Button zero = (Button) findViewById(R.id.zero);
        Button jin = (Button) findViewById(R.id.jin);
        Button xin = (Button) findViewById(R.id.xin);
        Button callin = (Button) findViewById(R.id.call_in);
        Button packup = (Button) findViewById(R.id.pack_up);
        Button delete = (Button) findViewById(R.id.delete);

        OnClickItemListener listener = new OnClickItemListener();
        one.setOnClickListener(listener);
        two.setOnClickListener(listener);
        three.setOnClickListener(listener);
        four.setOnClickListener(listener);
        five.setOnClickListener(listener);
        six.setOnClickListener(listener);
        seven.setOnClickListener(listener);
        eight.setOnClickListener(listener);
        nine.setOnClickListener(listener);
        zero.setOnClickListener(listener);
        jin.setOnClickListener(listener);
        xin.setOnClickListener(listener);
        callin.setOnClickListener(listener);
        packup.setOnClickListener(listener);
        delete.setOnClickListener(listener);

        DataHandler.getDialList(mDialList, "n");
        RecyclerView rv_contasts = (RecyclerView) findViewById(R.id.rv_contact);
        LinearLayoutManager manager = new LinearLayoutManager(instance);
        adapter = new DialContactsAdapter(mDialList);
        rv_contasts.setLayoutManager(manager);
        rv_contasts.setAdapter(adapter);
    }
    class OnClickItemListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText phone = (EditText) findViewById(R.id.et_phone);
            int index = phone.getSelectionStart();
            Editable editable = phone.getText();
            switch (v.getId()) {
                case R.id.one:{
                    editable.insert(index, "1");
                } break;
                case R.id.two:{
                    editable.insert(index, "2");
                } break;
                case R.id.three:{
                    editable.insert(index, "3");
                } break;
                case R.id.four:{
                    editable.insert(index, "4");
                } break;
                case R.id.five:{
                    editable.insert(index, "5");
                } break;
                case R.id.six:{
                    editable.insert(index, "6");
                } break;
                case R.id.seven:{
                    editable.insert(index, "7");
                } break;
                case R.id.eight:{
                    editable.insert(index, "8");
                } break;
                case R.id.nine:{
                    editable.insert(index, "9");
                } break;
                case R.id.zero:{
                    editable.insert(index, "0");
                } break;
                case R.id.jin:{
                    editable.insert(index, "#");
                } break;
                case R.id.xin:{
                    editable.insert(index, "*");
                } break;
                case R.id.call_in:{
                    if(ActivityCompat.checkSelfPermission(instance,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phone.getText()));
                        startActivity(intent);
                    }
                }break;
                case R.id.pack_up:{
                    finish();
                } break;
                case R.id.delete:{
                    if (phone.getText().length() > 0 && index > 0){

                        editable.delete(index-1, index);
                    }
                }break;
            }
            CharSequence text = phone.getText();
            DataHandler.getDialList(mDialList, text.toString());
            adapter.notifyDataSetChanged();
//            adapter.viewNotify(mDialList);
            //Debug.asserts(text instanceof Spannable);
            if (text instanceof Spannable && index == 0) {
                Spannable spanText = (Spannable)text;
                Selection.setSelection(spanText, text.length());
            }
        }
    }
}
