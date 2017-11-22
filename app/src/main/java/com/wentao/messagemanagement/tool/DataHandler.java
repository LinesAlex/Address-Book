package com.wentao.messagemanagement.tool;

import android.util.Log;

import com.wentao.messagemanagement.db.output.ContactsInfo;
import com.wentao.messagemanagement.db.input.MContacts;
import com.wentao.messagemanagement.db.input.MEmail;
import com.wentao.messagemanagement.db.input.MPhone;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/11/22.
 */

public class DataHandler{
    public static List<ContactsInfo> getContacts() {
        List<MContacts> contacts = DataSupport.findAll(MContacts.class);
        List<ContactsInfo> infos = new ArrayList<>();
        for (MContacts c : contacts) {
            ContactsInfo info = new ContactsInfo();
            info.setName(c.getName());
            info.setPinyin(c.getSurname());
            info.setId(c.getMid());
            if (!DataSupport.where("mid = ?",c.getMid()).find(MEmail.class).isEmpty()) {
                String phone = DataSupport.where("mid = ?", c.getMid()).find(MEmail.class).get(0).getEmail();
                info.setEmail(check(phone));
            }
            if (!DataSupport.where("mid = ?",c.getMid()).find(MEmail.class).isEmpty()) {
                String email = DataSupport.where("mid = ?", c.getMid()).find(MPhone.class).get(0).getPhone();
                info.setPhoneNumber(check(email));
            }
            infos.add(info);
        }

        Collections.sort(infos,new ContactsComparator());//比较
        for (int i = 0; i < infos.size(); i++) {
            infos.get(i).setCount(i + 1);
        }
        return infos;
    }
    private static String check(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return str;
        }
    }
}
