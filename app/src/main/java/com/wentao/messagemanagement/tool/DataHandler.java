package com.wentao.messagemanagement.tool;

import android.content.Context;

import com.wentao.messagemanagement.db.input.Intro;
import com.wentao.messagemanagement.db.output.CallInfo;
import com.wentao.messagemanagement.db.output.ContactsInfo;
import com.wentao.messagemanagement.db.input.MContacts;
import com.wentao.messagemanagement.db.input.MEmail;
import com.wentao.messagemanagement.db.input.MPhone;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.db.output.MessageInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by Voter Lin on 2017/11/22.
 * DataHandler is responsible for handles and output data
 *
 */

public class DataHandler{

    static public String NO_PHONE = "NO_PHONE_USE";
    /**
     *use mid get phone email name
     */
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
    public static String getSurname(String id) {
        return DataSupport.where("mid = ?", id).find(MContacts.class).get(0).getSurname();
    }
    /**
     *update local data base and system data base
     */
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
    /**
     *insert local data base and system data base
     */
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
    /**
     *delete local data base and system data base
     */
    public static void deleteData(String id) {
        DataSupport.deleteAll(Intro.class, "mid = ?", id);
        DataSupport.deleteAll(MPhone.class, "mid = ?", id);
        DataSupport.deleteAll(MEmail.class, "mid = ?", id);
        DataSupport.deleteAll(MContacts.class, "mid = ?", id);
        ContactsHandler.delete(id);
    }
    /**
     *use mid get date base "Intro"
     */
    public static Intro getIntro(String id) {
        if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("mid = ?", id).find(Intro.class).size() > 0) {
            List<Intro> intros = DataSupport.where("mid = ?", id).find(Intro.class);
            return intros.get(0);
        }
        return null;
    }
    /**
     *insert date base "Intro"
     */
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

    /**
     *This Data used to create a list
     */
    public static List<ContactsInfo> getContacts() {
        List<ContactsInfo> list = new ArrayList<>();
        List<MContacts> contacts = DataSupport.findAll(MContacts.class);
        for (MContacts c : contacts) {
            ContactsInfo info = new ContactsInfo();
            info.setName(c.getName());
            info.setSurname(c.getSurname());
            info.setId(c.getMid());
            info.setPhoneNumber(getPhone(c.getMid()));
            info.setEmail(getEmail(c.getMid()));
            list.add(info);
        }
        Collections.sort(list,new ContactsComparator());//比较
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setCount(i + 1);
        }
        return list;
    }

    public static void init(Context context) {
        if (DataSupport.findAll(MContacts.class).isEmpty()){
            ContactsHandler.getContacts(context);
        }
    }
    /**
     *use ContactsHandler.getAllMessages() get all messages
     */
    public static List<MessageInfo> getAllMessages(Context context) {
        List<MessageInfo> list = new ArrayList<>();
        ContactsHandler.getAllMessages(context, list);
        List<MessageInfo> mainMessage = new ArrayList<>();
        List<MessageInfo> otherMessage = new ArrayList<>();
        for (MessageInfo info : list) {
            if (info.getPhoneNumber().startsWith("106") || info.getPhoneNumber().length() > 11)
            {
                otherMessage.add(info);
            } else {
                mainMessage.add(info);
            }
        }
        MessageInfo.MessageIndex = mainMessage.size();
        mainMessage.addAll(otherMessage);
        return mainMessage;
    }
    /**
     *use ContactsHandler.getAllCalls() get all calls
     */
    public static List<CallInfo> getAllCalls(Context context) {
        List<CallInfo> list = new ArrayList<>();
        ContactsHandler.getAllCalls(context, list);
        return list;
    }

    public static void getDialList(List<DialInfo> infos, String newText){
        if (!infos.isEmpty())
            infos.clear();
        if (isNumber(newText)) {
            List<MPhone> phones;
            if (Objects.equals(newText, NO_PHONE)) {
                phones = DataSupport.findAll(MPhone.class);
            } else {
                phones = DataSupport.where("phone like ?", "%" + newText + "%").find(MPhone.class);
            }
            for (MPhone p : phones) {
                if (p.getPhone().length() > 0) {
                    DialInfo i = new DialInfo();
                    i.setPhone(p.getPhone());
                    i.setName(getName(p.getMid()));
                    i.setSurname(getSurname(p.getMid()));
                    infos.add(i);
                }
            }
        } else {
            List<MContacts> names;
            if (Objects.equals(newText, NO_PHONE)) {
                names = DataSupport.findAll(MContacts.class);
            } else {
                names = DataSupport.where("name like ?", "%" + newText + "%").find(MContacts.class);
            }
            for (MContacts n : names){
                if (getPhone(n.getMid()).length() > 0) {
                    DialInfo i = new DialInfo();
                    i.setPhone(getPhone(n.getMid()));
                    i.setName(n.getName());
                    i.setSurname(n.getSurname());
                    infos.add(i);
                }
            }
        }
        Collections.sort(infos, new ContactsComparator());
    }

    public static boolean isNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    private static String check(String str, String ostr) {
        if (str == null || str.isEmpty() || str.equals("")) {
            return ostr;
        } else {
            return str;
        }
    }
}
