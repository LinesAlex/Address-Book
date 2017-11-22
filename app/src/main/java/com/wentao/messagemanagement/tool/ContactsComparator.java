package com.wentao.messagemanagement.tool;

import com.wentao.messagemanagement.db.output.ContactsInfo;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ContactsComparator implements Comparator<ContactsInfo> {
    @Override
    public int compare(ContactsInfo o1, ContactsInfo o2) {
        String n1 = !o1.getName().isEmpty()?o1.getName().substring(0,1) : "#";
        String n2 = o2.getName().isEmpty()?o2.getName().substring(0,1) : "#";
        int flag = o1.getPinyin().compareTo(o2.getPinyin());
        if (flag > 0 )
            return 1;
        else if (flag == 0 && n1.equals(n2))
            return 0;
        else
            return -1;
    }
    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
