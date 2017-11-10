package com.wentao.messagemanagement.tool;

import com.wentao.messagemanagement.db.ContactsInfo;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ContactsComparator implements Comparator<ContactsInfo> {
    @Override
    public int compare(ContactsInfo o1, ContactsInfo o2) {
        ContactsInfo one, two;
        one = o1;
        two = o2;
        String oneName = one.getName().substring(0,1);
        String twoName = two.getName().substring(0,1);

        int flag = one.getPinyin().compareTo(two.getPinyin());
        if (flag > 0 )
            return 1;
        else if (flag == 0 && oneName == twoName)
            return 0;
        else
            return -1;
    }
    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
