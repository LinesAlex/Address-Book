package com.wentao.messagemanagement.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.wentao.messagemanagement.Adapter.ChoiceContactsAdapter;
import com.wentao.messagemanagement.Adapter.DialAdapter;
import com.wentao.messagemanagement.Animation.AnimationUtil;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.tool.DataHandler;
import com.wentao.messagemanagement.tool.NotifyList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class SendMessagePage extends AppCompatActivity {
    private static SendMessagePage instance;
    public static SendMessagePage getInstance() {
        return instance;
    }

    private List<DialInfo> list = new ArrayList<>();
    private List<DialInfo> choiceList = new ArrayList<>();
    private List<DialInfo> searchList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_send_message);
        instance = SendMessagePage.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_title_send_message);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {actionBar.setDisplayHomeAsUpEnabled(true);}

        final RecyclerView rv_choice_contacts, rv_contacts, rv_search_contacts;
        DataHandler.getDialList(list,DataHandler.NO_PHONE);
        DialAdapter adapter = new DialAdapter(list, DialAdapter.CHOICE_ITEM);
        LinearLayoutManager manager = new LinearLayoutManager(instance);
        rv_contacts = (RecyclerView) findViewById(R.id.rv_contacts);
        rv_contacts.setLayoutManager(manager);
        rv_contacts.setAdapter(adapter);

        DialAdapter adapterS = new DialAdapter(searchList, DialAdapter.SEARCH_ITEM);
        LinearLayoutManager managerS = new LinearLayoutManager(instance);
        rv_search_contacts = (RecyclerView) findViewById(R.id.rv_search_contacts);
        rv_search_contacts.setLayoutManager(managerS);
        rv_search_contacts.setAdapter(adapterS);

        ChoiceContactsAdapter adapterC = new ChoiceContactsAdapter(choiceList);
        LinearLayoutManager managerC = new LinearLayoutManager(instance);
        managerC.setOrientation(LinearLayoutManager.HORIZONTAL);
//        GridLayoutManager managerC = new GridLayoutManager(this,5);
        rv_choice_contacts = (RecyclerView) findViewById(R.id.rv_choice_contacts);
        rv_choice_contacts.setLayoutManager(managerC);
        rv_choice_contacts.setAdapter(adapterC);



        NotifyList.initNotify(list, choiceList, searchList,adapter, adapterC, adapterS);

        final EditText et_message = (EditText) findViewById(R.id.autoCompleteTextView);
        final FloatingActionButton show_edit = (FloatingActionButton) findViewById(R.id.show_edit);
        final FloatingActionButton send_message = (FloatingActionButton) findViewById(R.id.fbtn_send_message);
        final LinearLayout edit_page = (LinearLayout) findViewById(R.id.edit_page);
        show_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.setAnimationTopToButton(new View[]{edit_page, send_message, show_edit, rv_contacts});
            }
        });

        Button hide_edit = (Button) findViewById(R.id.hide_edit);
        hide_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.setAnimationTopToButton(new View[]{edit_page, send_message, show_edit, rv_contacts});
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = et_message.getText().toString();
                if(choiceList.isEmpty()) {
                    Snackbar.make(edit_page, "未选择联系人，无法发送", Snackbar.LENGTH_SHORT).show();
                }else if (message.isEmpty() || message.length() == 0) {
                    Snackbar.make(edit_page, "未填写短信，无法发送", Snackbar.LENGTH_SHORT).show();
                }else {
                    if (choiceList.size() > 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(instance);
                        dialog.setTitle("提示");
                        dialog.setMessage("是否要将信息发出?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("发出信息", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String names = null;
                                for (DialInfo i : choiceList) {
                                    et_message.setText("");
                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(i.getPhone(), null, message, null, null);
                                    names += i.getName() + " ";
                                }
                                Snackbar.make(edit_page, "已将信息发送给联系人 : " + names, Snackbar.LENGTH_SHORT).show();
                                while (!choiceList.isEmpty()) {
                                    NotifyList.Notify.notChoiceItem(0);
                                }
                            }
                        });
                        dialog.setNegativeButton("重新编辑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                    } else {
                        et_message.setText("");
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(choiceList.get(0).getPhone(), null, message, null, null);
                        Snackbar.make(edit_page, "已将信息发送给联系人 : " + choiceList.get(0).getName(), Snackbar.LENGTH_SHORT).show();
                        NotifyList.Notify.notChoiceItem(0);
                    }
                }
            }
        });


        SearchView search = (SearchView) findViewById(R.id.sv_search_contacts);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (send_message.getVisibility() == View.VISIBLE) {
                    AnimationUtil.setAnimationTopToButton(new View[]{edit_page, send_message, show_edit, rv_search_contacts});
                }
                if (newText.length() > 0 && rv_contacts.getVisibility() == View.VISIBLE) {
                    AnimationUtil.setAnimationLeftToRightGone(rv_contacts, instance);
                    AnimationUtil.setAnimationRightToLeftGone(rv_search_contacts, instance);
                } else if (newText.length() == 0 && rv_contacts.getVisibility() == View.GONE){
                    AnimationUtil.setAnimationLeftToRightGone(rv_contacts, instance);
                    AnimationUtil.setAnimationRightToLeftGone(rv_search_contacts, instance);
                }
                NotifyList.Notify.searchList(newText);
                FrameLayout fl_search = (FrameLayout) findViewById(R.id.fl_search);
                if (searchList.size() == 0 && rv_contacts.getVisibility() == View.GONE) {
                    for (DialInfo infos : searchList) {
                        if(infos.getPhone().startsWith(newText)) {
                            fl_search.setVisibility(View.GONE);
                            return true;
                        }
                    }
                    fl_search.setVisibility(View.VISIBLE);
                    TextView tv_search = (TextView) findViewById(R.id.tv_search_choice);
                    Button btn_search = (Button) findViewById(R.id.btn_search_choice);
                    tv_search.setText(newText);
                    btn_search.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NotifyList.Notify.addSearchItem(newText);
                        }
                    });
                } else {
                    fl_search.setVisibility(View.GONE);
                }
                return true;
            }
        });
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
