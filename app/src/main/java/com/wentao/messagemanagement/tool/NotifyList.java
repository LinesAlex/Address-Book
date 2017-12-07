package com.wentao.messagemanagement.tool;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.wentao.messagemanagement.Activity.SendMessagePage;
import com.wentao.messagemanagement.Adapter.DialContactsAdapter;
import com.wentao.messagemanagement.db.output.DialInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/12/1.
 */

public class NotifyList {

    private List<DialInfo> choiceList, searchList, list;
    private RecyclerView.Adapter  choiceAdapter, searchAdapter;
    private List<Integer> indexList;
    public static NotifyList Notify;

    private NotifyList(List<DialInfo> l0, List<DialInfo> l1, List<DialInfo> l2,RecyclerView.Adapter a1, RecyclerView.Adapter a2){
        list = l0;
        choiceList = l1;
        searchList = l2;
        choiceAdapter = a1;
        searchAdapter = a2;
        indexList = new ArrayList<>();
    }

    public static void initNotify(List<DialInfo> l0, List<DialInfo> l1, List<DialInfo> l2, RecyclerView.Adapter a1, RecyclerView.Adapter a2){
        Notify = new NotifyList(l0,l1,l2,a1,a2);
    }

    public void choiceItem(int index, boolean flag) {
        if (index != -1)
        if (flag) {
            choiceList.add(0, list.get(index));
            indexList.add(0,index);
        } else {
            choiceList.remove(indexList.indexOf(index));
            indexList.remove(indexList.indexOf(index));
        }
        choiceAdapter.notifyDataSetChanged();
    }

    public int getIndex(int index) {
        for(int i = 0; i < list.size(); i++) {
            if (list.get(i).getPhone().startsWith(searchList.get(index).getPhone()) &&
                    list.get(i).getName().startsWith(searchList.get(index).getName()))
            {
                return i;
            }
        }
        return -1;
    }

    public void notChoiceItem(int index) {
        if (indexList.get(index) != -1) {
            //完成对checkbox的修改。
            DialContactsAdapter.radioButtons.get(indexList.get(index)).setChecked(false);
            DialContactsAdapter.checkList.set(indexList.get(index), false);
        }
        choiceList.remove(index);
        indexList.remove(index);
        choiceAdapter.notifyDataSetChanged();
    }

    public void addSearchItem(String newText) {
        if (!DataHandler.isNumber(newText)) {
            Toast.makeText(SendMessagePage.getInstance(), "联系人 "+newText+" 不存在",Toast.LENGTH_SHORT).show();
            return;
        }

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
        indexList.add(0,-1);
        choiceAdapter.notifyDataSetChanged();
    }

    public void searchList(String phone) {
        List<DialInfo> l = new ArrayList<>();
        if (!phone.isEmpty() && phone.length() > 0) {
            DataHandler.getDialList(l, phone);
            if (!searchList.isEmpty())
                searchList.clear();
            if (!choiceList.isEmpty())
                for (int i = 0; i < l.size(); i++) {
                    for (int j = 0; j < choiceList.size(); j++) {
                        if(l.get(i).getPhone().startsWith(choiceList.get(j).getPhone())) {
                            break;
                        } else if (j == choiceList.size() - 1) {
                            searchList.add(l.get(i));
                        }
                    }
                }
            else
            searchList.addAll(l);
        }
        searchAdapter.notifyDataSetChanged();
    }
}
