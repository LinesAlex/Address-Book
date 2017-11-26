package com.wentao.messagemanagement.tool;

import com.wentao.messagemanagement.Activity.ContactsPage;
import com.wentao.messagemanagement.db.input.Intro;
import com.wentao.messagemanagement.db.output.ContactsInfo;
import com.wentao.messagemanagement.db.input.MContacts;
import com.wentao.messagemanagement.db.input.MEmail;
import com.wentao.messagemanagement.db.input.MPhone;

import org.litepal.crud.DataSupport;

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

    public static void updateData(String id, String[] infos) {
        Intro intro = new Intro();
        intro.setPhone(infos[0]);
        intro.setName(infos[1]);
        intro.setEmail(infos[2]);
        intro.setAddress(infos[3]);
        intro.setJob(infos[4]);
        intro.setAge(infos[5]);
        intro.updateAll("mid = ?", id);

        MContacts contact = new MContacts();
        MPhone phone = new MPhone();
        MEmail email = new MEmail();
        contact.setSurname(ContactsHandler.getPinyin(infos[1].charAt(0)));
        contact.setName(infos[1]);
        contact.updateAll("mid = ?", id);
        if (DataSupport.where("mid = ?", id).find(MPhone.class).size() > 0) {
            phone.setPhone(infos[0]);
            phone.update(DataSupport.where("mid = ?", id).find(MPhone.class).get(0).getId());
        } else {
            phone.setMid(id);
            phone.setPhone(infos[0]);
            phone.save();
        }
        boolean flag = DataSupport.where("mid = ?", id).find(MEmail.class).size() > 0;
        if (!infos[2].isEmpty() || infos[2].length() > 0) {
            if (flag){
                email.setEmail(infos[2]);
                email.update(DataSupport.where("mid = ?", id).find(MEmail.class).get(0).getId());
            } else {
                email.setMid(id);
                email.setEmail(infos[2]);
                email.save();
            }
        }
        ContactsHandler.update(id, infos[0], infos[1], infos[2], flag);
    }

    public static void insertData(String[] infos){
        String id = ContactsHandler.insert(infos[0], infos[1], infos[2]);
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
        MPhone phone = new MPhone();
        MEmail email = new MEmail();
        contact.setMid(id);
        contact.setSurname(ContactsHandler.getPinyin(infos[1].charAt(0)));
        contact.setName(infos[1]);
        contact.save();
        phone.setMid(id);
        phone.setPhone(infos[0]);
        phone.save();
        if (!infos[2].isEmpty() || infos[2].length() > 0) {
            email.setMid(id);
            email.setEmail(infos[2]);
            email.save();
        }
    }
    public static void deleteData(String id) {
        DataSupport.deleteAll(Intro.class, "mid = ?", id);
        DataSupport.deleteAll(MPhone.class, "mid = ?", id);
        DataSupport.deleteAll(MEmail.class, "mid = ?", id);
        DataSupport.deleteAll(MContacts.class, "mid = ?", id);
        ContactsHandler.delete(id);
    }

    public static Intro getIntro(String id) {
        if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            return intros.get(0);
        }
        return null;
    }
    public static void saveIntro(String id, String[] infos) {
        Intro intro = new Intro();
        intro.setMid(id);
        intro.setPhone(infos[0]);
        intro.setName(infos[1]);
        intro.setEmail(infos[2]);
        intro.setAddress(infos[3]);
        intro.setJob(infos[4]);
        intro.setAge(infos[5]);
        intro.save();
    }

    private static String check(String str, String ostr) {
        if (str == null || str.isEmpty() || str.equals("")) {
            return ostr;
        } else {
            return str;
        }
    }
}
