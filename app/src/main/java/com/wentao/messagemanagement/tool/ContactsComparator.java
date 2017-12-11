package com.wentao.messagemanagement.tool;

import android.util.Log;

import com.wentao.messagemanagement.db.Infos;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/5.
 */

public class ContactsComparator implements Comparator<Infos> {
    @Override
    public int compare(Infos o1, Infos o2) {
        String n1 = !o1.getName().isEmpty()?o1.getName().substring(0,1) : "Z";
        String n2 = !o2.getName().isEmpty()?o2.getName().substring(0,1) : "Z";

        String Sur1 = !o1.getSurname().isEmpty()?o1.getSurname() : "Z";
        String Sur2 = !o2.getSurname().isEmpty()?o2.getSurname() : "Z";
        int f1 = Sur1.toLowerCase().compareTo(Sur2.toLowerCase());
        int f2 = n1.compareTo(n2);
        if (f1 > 0)
            return 1;
        else if (f1 == 0 && f2 > 0)
            return 1;
        else if (f1 == 0 && f2 == 0)
            return 0;
        else if (f1 == 0 && f2 < 0)
            return -1;
        else
            return -1;
    }
    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
