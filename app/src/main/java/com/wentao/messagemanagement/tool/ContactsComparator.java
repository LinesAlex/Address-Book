package com.wentao.messagemanagement.tool;

import com.wentao.messagemanagement.db.Infos;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ContactsComparator implements Comparator<Infos> {
    @Override
    public int compare(Infos o1, Infos o2) {
        String n1 = !o1.getName().isEmpty()?o1.getName().substring(0,1) : "#";
        String n2 = !o2.getName().isEmpty()?o2.getName().substring(0,1) : "#";
        String Sur1 = !o1.getSurname().isEmpty()?o1.getSurname() : "Z";
        String Sur2 = !o2.getSurname().isEmpty()?o2.getSurname() : "Z";
        int flag = Sur1.toLowerCase().compareTo(Sur2.toLowerCase());
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
