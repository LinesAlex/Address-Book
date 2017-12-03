package com.wentao.messagemanagement.tool;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.wentao.messagemanagement.db.output.DialInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/12/1.
 */

public class NotifyList {
    private List<DialInfo> choiceList, list;
    private RecyclerView.Adapter  choiceAdapter, adapter;
    public static NotifyList Notify;
    private NotifyList(List<DialInfo> l0, List<DialInfo> l1, RecyclerView.Adapter a0, RecyclerView.Adapter a1){
        list = l0;
        choiceList = l1;
        adapter = a0;
        choiceAdapter = a1;
    }
    public static void initNotify(List<DialInfo> l0, List<DialInfo> l1, RecyclerView.Adapter a0, RecyclerView.Adapter a1){
        Notify = new NotifyList(l0,l1,a0,a1);
    }
    public void choiceItem(int index) {
        choiceList.add(0,list.get(index));
        list.remove(index);
        notifyData();
    }

    public void addItem(String newText) {
        for (DialInfo info : choiceList) {
            if (Objects.equals(newText, info.getPhone())) {
                return;
            }
        }
        DialInfo dialInfo = new DialInfo();
        dialInfo.setName(newText);
        dialInfo.setPhone(newText);
        dialInfo.setContacts(false);
        choiceList.add(0,dialInfo);
        notifyData();
    }

    public void notChoiceItem(int index) {
        if (choiceList.get(index).isContacts())
            list.add(choiceList.get(index));
        choiceList.remove(index);
        notifyData();
    }

    public void searchList(String phone) {
        List<DialInfo> l = new ArrayList<>();
        if (!phone.isEmpty() && phone.length() > 0) {
            DataHandler.getDialList(l, phone);
        } else {
            DataHandler.getDialList(l, "n");
        }
        list.clear();
        if (!choiceList.isEmpty())
            for (int i = l.size() - 1; i >= 0; i--) {
                for (int j = 0; j < choiceList.size(); j++) {
                    if(l.get(i).getPhone().contains(choiceList.get(j).getPhone())) {
                        break;
                    } else if (j == choiceList.size() - 1) {
                        list.add(l.get(i));
                    }
                }
            }
        else list.addAll(l);
        adapter.notifyDataSetChanged();
    }

    private void notifyData(){
        choiceAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }
}
