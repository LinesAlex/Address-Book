package com.wentao.messagemanagement.tool;

import android.util.Log;

import com.wentao.messagemanagement.db.input.Intro;
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
    public static void getContacts() {
        ContactsInfo.List.clear();
        List<MContacts> contacts = DataSupport.findAll(MContacts.class);
        for (MContacts c : contacts) {
            ContactsInfo info = new ContactsInfo();
            info.setName(c.getName());
            info.setPinyin(c.getSurname());
            info.setId(c.getMid());
            info.setPhoneNumber(getPhone(c.getMid()));
            info.setEmail(getEmail(c.getMid()));
            Log.i("Name   ",c.getName());
            Log.i("Title   ",c.getSurname());
            Log.i("Mid   ",c.getMid());
            Log.i("Phone   ", "-" + info.getPhoneNumber().get(0) + "-");
            Log.i("Email   ", "-" + info.getEmail().get(0) + "-\n");

            ContactsInfo.List.add(info);
        }

        Collections.sort(ContactsInfo.List,new ContactsComparator());//比较
        for (int i = 0; i < ContactsInfo.List.size(); i++) {
            ContactsInfo.List.get(i).setCount(i + 1);
        }
    }
    public static String getPhone(String mid) {
        if (!DataSupport.where("mid = ?", mid).find(MPhone.class).isEmpty()) {
            String phone = DataSupport.where("mid = ?", mid).find(MPhone.class).get(0).getPhone();
            return check(phone.trim(), "");
        } else {
            return "";
        }
    }
    public static String getEmail(String mid) {
        if (!DataSupport.where("mid = ?", mid).find(MEmail.class).isEmpty()) {
            String email = DataSupport.where("mid = ?", mid).find(MEmail.class).get(0).getEmail();
            return check(email.trim(), "");
        } else {
            return "";
        }
    }
    public static String getName(String id) {
        return DataSupport.where("mid = ?", id).find(MContacts.class).get(0).getName();
    }

    public static void updateIntro(String id, String[] infos) {
        Intro intro = new Intro();
        intro.setPhone(infos[0]);
        intro.setName(infos[1]);
        intro.setEmail(infos[2]);
        intro.setAddress(infos[3]);
        intro.setJob(infos[4]);
        intro.setAge(infos[5]);
        intro.updateAll("mid = ?", id);
    }

    public static void insertIntro(String id, String[] infos){
        Intro intro = new Intro();
        intro.setMid(id);
        intro.setPhone(infos[0]);
        intro.setName(infos[1]);
        intro.setEmail(infos[2]);
        intro.setAddress(infos[3]);
        intro.setJob(infos[4]);
        intro.setAge(infos[5]);
        intro.save();
        MContacts contact = new MContacts();
    }
    public static void deleteIntro(String id) {
        DataSupport.deleteAll(Intro.class, "mid = ?", id);
        DataSupport.deleteAll(MPhone.class, "mid = ?", id);
        DataSupport.deleteAll(MEmail.class, "mid = ?", id);
        DataSupport.deleteAll(MContacts.class, "mid = ?", id);
    }
    public static Intro getIntro(String id) {
        if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            return intros.get(0);
        }
        return null;
    }

    private static String check(String str, String ostr) {
        if (str == null || str.isEmpty() || str.equals("")) {
            return ostr;
        } else {
            return str;
        }
    }
}
