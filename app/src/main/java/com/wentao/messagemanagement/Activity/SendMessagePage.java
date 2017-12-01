package com.wentao.messagemanagement.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_send_message);
        instance = SendMessagePage.this;

        final FloatingActionButton show_edit = (FloatingActionButton) findViewById(R.id.show_edit);
        final LinearLayout edit_page = (LinearLayout) findViewById(R.id.edit_page);
        show_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_page.setVisibility(View.VISIBLE);
                edit_page.setAnimation(AnimationUtil.moveToViewLocation());
                show_edit.setVisibility(View.GONE);
                show_edit.setAnimation(AnimationUtil.moveToViewBottom());
            }
        });
        Button hide_edit = (Button) findViewById(R.id.hide_edit);
        hide_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_page.setVisibility(View.GONE);
                edit_page.setAnimation(AnimationUtil.moveToViewBottom());
                show_edit.setVisibility(View.VISIBLE);
                show_edit.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });

        RecyclerView rv_choice_contacts, rv_contacts;
        rv_choice_contacts = (RecyclerView) findViewById(R.id.rv_choice_contacts);
        ChoiceContactsAdapter adapterC = new ChoiceContactsAdapter(contactsChoiceList);
        LinearLayoutManager managerC = new LinearLayoutManager(instance);
        managerC.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_choice_contacts.setLayoutManager(managerC);
        rv_choice_contacts.setAdapter(adapterC);


        rv_contacts = (RecyclerView) findViewById(R.id.rv_contacts);
        DataHandler.getDialList(contactsList,"n");
        DialContactsAdapter adapter = new DialContactsAdapter(contactsList);
        LinearLayoutManager manager = new LinearLayoutManager(instance);
        adapter.setChioceFlag();
        rv_contacts.setLayoutManager(manager);
        rv_contacts.setAdapter(adapter);

        NotifyList.initNotify(contactsList, contactsChoiceList, adapter, adapterC);
    }
}
