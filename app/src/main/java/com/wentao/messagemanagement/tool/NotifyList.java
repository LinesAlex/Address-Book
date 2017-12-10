package com.wentao.messagemanagement.tool;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.wentao.messagemanagement.Activity.SendMessagePage;
import com.wentao.messagemanagement.Adapter.DialAdapter;
import com.wentao.messagemanagement.db.output.DialInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/12/1.
 */

public class NotifyList {

    private List<DialInfo> beChoiceList, searchList, choiceList;
    private RecyclerView.Adapter bechoiceAdapter, searchAdapter;
    private List<Integer> indexList;
    public static NotifyList Notify;
    private NotifyList(List<DialInfo> l0, List<DialInfo> l1, List<DialInfo> l2,RecyclerView.Adapter a1, RecyclerView.Adapter a2){
        choiceList = l0;
        beChoiceList = l1;
        searchList = l2;
        bechoiceAdapter = a1;
        searchAdapter = a2;
        indexList = new ArrayList<>();
    }

    public static void initNotify(List<DialInfo> l0, List<DialInfo> l1, List<DialInfo> l2, RecyclerView.Adapter a1, RecyclerView.Adapter a2){
        Notify = new NotifyList(l0,l1,l2,a1,a2);
    }

    public void choiceItem(int index, boolean flag) {
        if (index != -1)
        if (flag) {
            addBeChoiceItem(choiceList.get(index), index);
        } else {
            removeBeChoiceItem(indexList.indexOf(index));
        }
        bechoiceAdapter.notifyDataSetChanged();
    }

    public int getIndex(int index) {
        for(int i = 0; i < choiceList.size(); i++) {
            if (choiceList.get(i).getPhone().startsWith(searchList.get(index).getPhone()) &&
                    choiceList.get(i).getName().startsWith(searchList.get(index).getName()))
            {
                return i;
            }
        }
        return -1;
    }

    public void notChoiceItem(int index) {
        if (indexList.get(index) != -1) {
            DialAdapter.checkPositions.set(indexList.get(index), false);
        }
        removeBeChoiceItem(index);
        bechoiceAdapter.notifyDataSetChanged();
    }

    public void addSearchItem(String newText) {
        if (!DataHandler.isNumber(newText)) {
            Toast.makeText(SendMessagePage.getInstance(), "联系人 "+newText+" 不存在",Toast.LENGTH_SHORT).show();
            return;
        }
        for (DialInfo info : beChoiceList) {
            if (Objects.equals(newText, info.getPhone())) {
                return;
            }
        }
        DialInfo dialInfo = new DialInfo();
        dialInfo.setName(newText);
        dialInfo.setPhone(newText);
        dialInfo.setContacts(false);
        addBeChoiceItem(dialInfo, -1);
        bechoiceAdapter.notifyDataSetChanged();
    }

    private void addBeChoiceItem(DialInfo info, int index) {
        beChoiceList.add(0,info);
        indexList.add(0,index);
    }

    private void removeBeChoiceItem(int index) {
        beChoiceList.remove(index);
        indexList.remove(index);
    }

    public void searchList(String phone) {
        List<DialInfo> l = new ArrayList<>();
        if (!phone.isEmpty() && phone.length() > 0) {
            DataHandler.getDialList(l, phone);
            if (!searchList.isEmpty())
                searchList.clear();
            if (!beChoiceList.isEmpty())
                for (int i = 0; i < l.size(); i++) {
                    for (int j = 0; j < beChoiceList.size(); j++) {
                        if(l.get(i).getPhone().startsWith(beChoiceList.get(j).getPhone())) {
                            break;
                        } else if (j == beChoiceList.size() - 1) {
                            searchList.add(l.get(i));
                        }
                    }
                }
            else
            searchList.addAll(l);
        }
        DialAdapter.searchPositions.clear();
        for (int i =0 ; i < searchList.size(); i++) {
            DialAdapter.searchPositions.add(false);
        }
        searchAdapter.notifyDataSetChanged();
    }
}
