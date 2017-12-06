package com.wentao.messagemanagement.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.wentao.messagemanagement.Adapter.ChoiceContactsAdapter;
import com.wentao.messagemanagement.Adapter.DialContactsAdapter;
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
    private SendMessagePage instance;
    public SendMessagePage getInstance() {
        return instance;
    }

    private List<DialInfo> contactsList = new ArrayList<>();
    private List<DialInfo> contactsChoiceList = new ArrayList<>();
    private List<DialInfo> contactsSearchList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_send_message);
        instance = SendMessagePage.this;



        final RecyclerView rv_choice_contacts, rv_contacts, rv_search_contacts;

        final DialContactsAdapter adapter = new DialContactsAdapter(contactsList);
        adapter.setChioceFlag();
        DataHandler.getDialList(contactsList,DataHandler.NO_PHONE);
        LinearLayoutManager manager = new LinearLayoutManager(instance);
        rv_contacts = (RecyclerView) findViewById(R.id.rv_contacts);
        rv_contacts.setLayoutManager(manager);
        rv_contacts.setAdapter(adapter);

        ChoiceContactsAdapter adapterC = new ChoiceContactsAdapter(contactsChoiceList);
        LinearLayoutManager managerC = new LinearLayoutManager(instance);
        managerC.setOrientation(LinearLayoutManager.HORIZONTAL);
//        GridLayoutManager managerC = new GridLayoutManager(this,5);
        rv_choice_contacts = (RecyclerView) findViewById(R.id.rv_choice_contacts);
        rv_choice_contacts.setLayoutManager(managerC);
        rv_choice_contacts.setAdapter(adapterC);

        final DialContactsAdapter adapterS = new DialContactsAdapter(contactsSearchList);
        LinearLayoutManager managerS = new LinearLayoutManager(instance);
        rv_search_contacts = (RecyclerView) findViewById(R.id.rv_search_contacts);
        rv_search_contacts.setAdapter(adapterS);
        rv_search_contacts.setLayoutManager(managerS);

        NotifyList.initNotify(contactsList, contactsChoiceList, contactsSearchList, adapter, adapterC, adapterS);

        final EditText et_message = (EditText) findViewById(R.id.autoCompleteTextView);
        final FloatingActionButton show_edit = (FloatingActionButton) findViewById(R.id.show_edit);
        final FloatingActionButton send_message = (FloatingActionButton) findViewById(R.id.fbtn_send_message);
        final LinearLayout edit_page = (LinearLayout) findViewById(R.id.edit_page);
        show_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_page.setVisibility(View.VISIBLE);
                edit_page.setAnimation(AnimationUtil.moveToViewLocation());
                send_message.setVisibility(View.VISIBLE);
                send_message.setAnimation(AnimationUtil.moveToViewLocation());
                show_edit.setVisibility(View.GONE);
                show_edit.setAnimation(AnimationUtil.moveToViewBottom());
                rv_contacts.setVisibility(View.GONE);
                rv_contacts.setAnimation(AnimationUtil.moveToViewBottom());

            }
        });

        Button hide_edit = (Button) findViewById(R.id.hide_edit);
        hide_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_page.setVisibility(View.GONE);
                edit_page.setAnimation(AnimationUtil.moveToViewBottom());
                send_message.setVisibility(View.GONE);
                send_message.setAnimation(AnimationUtil.moveToViewBottom());
                show_edit.setVisibility(View.VISIBLE);
                show_edit.setAnimation(AnimationUtil.moveToViewLocation());
                rv_contacts.setVisibility(View.VISIBLE);
                rv_contacts.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = et_message.getText().toString();
                if(contactsChoiceList.isEmpty()) {
                    Snackbar.make(edit_page, "未选择联系人，无法发送", Snackbar.LENGTH_SHORT).show();
                }else if (message.isEmpty() || message.length() == 0) {
                    Snackbar.make(edit_page, "未填写短信，无法发送", Snackbar.LENGTH_SHORT).show();
                }else {
                    if (contactsChoiceList.size() > 1) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(instance);
                        dialog.setTitle("提示");
                        dialog.setMessage("是否要将信息发出?");
                        dialog.setCancelable(false);
                        dialog.setPositiveButton("发出信息", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String names = null;
                                for (DialInfo i : contactsChoiceList) {
                                    et_message.setText("");
                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(i.getPhone(), null, message, null, null);
                                    names += i.getName() + " ";
                                }
                                Snackbar.make(edit_page, "已将信息发送给联系人 : " + names, Snackbar.LENGTH_SHORT).show();
                                while (!contactsChoiceList.isEmpty()) {
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
                        sms.sendTextMessage(contactsChoiceList.get(0).getPhone(), null, message, null, null);
                        Snackbar.make(edit_page, "已将信息发送给联系人 : " + contactsChoiceList.get(0).getName(), Snackbar.LENGTH_SHORT).show();
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
                if (newText.length() > 0 && rv_contacts.getVisibility() == View.VISIBLE) {
                    rv_contacts.setVisibility(View.GONE);
                    rv_contacts.setAnimation(AnimationUtils.makeOutAnimation(instance, true));
                    rv_search_contacts.setVisibility(View.VISIBLE);
                    rv_search_contacts.setAnimation(AnimationUtils.makeInAnimation(instance, true));
                } else if (newText.length() == 0 && rv_contacts.getVisibility() == View.GONE){
                    rv_contacts.setVisibility(View.VISIBLE);
                    rv_contacts.setAnimation(AnimationUtils.makeInAnimation(instance, false));
                    rv_search_contacts.setVisibility(View.GONE);
                    rv_search_contacts.setAnimation(AnimationUtils.makeOutAnimation(instance, false));
                }
                NotifyList.Notify.searchList(newText);
                FrameLayout fl_search = (FrameLayout) findViewById(R.id.fl_search);
                if (contactsSearchList.size() == 0 && rv_contacts.getVisibility() == View.GONE) {
                    for (DialInfo infos :contactsChoiceList) {
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
}
