package com.wentao.messagemanagement.tool;

import android.support.v7.widget.RecyclerView;

import com.wentao.messagemanagement.db.output.DialInfo;

import java.util.List;

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

    public void notChoiceItem() {
        list.add(choiceList.get(0));
        choiceList.remove(0);
        notifyData();
    }

    private void notifyData(){
        choiceAdapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }
}
