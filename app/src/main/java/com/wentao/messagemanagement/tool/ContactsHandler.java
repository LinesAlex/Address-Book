package com.wentao.messagemanagement.tool;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;

import com.wentao.messagemanagement.Activity.AddContact;
import com.wentao.messagemanagement.Activity.ContactsPage;
import com.wentao.messagemanagement.db.output.CallInfo;
import com.wentao.messagemanagement.db.input.MContacts;
import com.wentao.messagemanagement.db.input.MEmail;
import com.wentao.messagemanagement.db.input.MPhone;
import com.wentao.messagemanagement.db.output.MessageInfo;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsHandler {
    public static ArrayList<CallInfo> CallInfos = new ArrayList<>();
    public static ArrayList<MessageInfo> MessageInfos = new ArrayList<>();

    static void getAllMessages(Context context, List<MessageInfo> list){
        String[] projection = new String[] {
                Telephony.Sms.ADDRESS
                , Telephony.Sms.BODY
                , Telephony.Sms.DATE
                , Telephony.Sms.TYPE
                , Telephony.Sms.PERSON};
        Cursor cursor = context.getContentResolver().query(
                Telephony.Sms.CONTENT_URI
                , projection
                , null
                , null
                , Telephony.Sms.DEFAULT_SORT_ORDER);
        List<String> checkHasPhone = new LinkedList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do{
                String phone = phoneCheck(cursor.getLong(0) + "");
                if (!checkHasPhone.contains(phone)) {
                    checkHasPhone.add(phone);
                    MessageInfo info = new MessageInfo();
                    info.setPhoneNumber(phone);
                    info.setSmsbody(cursor.getString(1).equals("") ? "-" : cursor.getString(1));
                    Date date = new Date(Long.parseLong(cursor.getString(2)));
                    info.setDate(TimeTool.formatForDate(date));
                    switch (cursor.getInt(3)) {
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            info.setType("送达");
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_DRAFT:
                            info.setType("草稿");
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_FAILED:
                            info.setType("发送失败");
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            info.setType("接收");
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            info.setType("待发");
                            break;
                        default:
                            info.setType("重新发送");
                            break;
                    }
                    if (Objects.equals(cursor.getString(4), ""))
                        info.setName(cursor.getString(4));
                    else if (DataSupport.where("phone = ?", phone).find(MPhone.class).size() > 0) {
                        String id = DataSupport.where("phone = ?", phone).find(MPhone.class).get(0).getMid();
                        info.setName(DataHandler.getName(id));
                        info.setId(id);
                    } else {
                        info.setName(phone);
                    }
                    list.add(info);
                }
            }while(cursor.moveToNext());
        }
    }

    static void getAllCalls(Context context, List<CallInfo> list) {
        String[] projection = new String[] {
                CallLog.Calls.TYPE
                , CallLog.Calls.DATE
                , CallLog.Calls.DURATION
                , CallLog.Calls.NUMBER
                , CallLog.Calls.CACHED_NAME
        };
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI
                , projection
                , null
                , null
                , CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CallInfo callInfo = new CallInfo();
                switch (Integer.parseInt(cursor.getString(0))) {
                    case CallLog.Calls.INCOMING_TYPE:callInfo.setType("呼入");break;
                    case CallLog.Calls.OUTGOING_TYPE:callInfo.setType("呼出");break;
                    case CallLog.Calls.MISSED_TYPE:  callInfo.setType("未接");break;
                    default:callInfo.setType("挂断");break;
                }
                //呼叫时间
                Date date = new Date(Long.parseLong(cursor.getString(1)));
                String phone = phoneCheck(cursor.getLong(3) + "");
                callInfo.setTime(TimeTool.formatForDate(date));
                //通话时长
                callInfo.setDuration(TimeTool.formatDuration(cursor.getLong(2)));
                callInfo.setPhoneNumber(phone);

                if (Objects.equals(cursor.getString(4), "")) {
                    callInfo.setName(cursor.getString(4));
                } else if (DataSupport.where("phone = ?",phone).find(MPhone.class).size() > 0) {
                    String id = DataSupport.where("phone = ?",phone).find(MPhone.class).get(0).getMid();
                    callInfo.setName(DataHandler.getName(id));
                    callInfo.setId(id);
                } else {
                    callInfo.setName(phone);
                }

                list.add(callInfo);
            }while(cursor.moveToNext());
        }
    }

    //初始化ContactsInfos
    public static ArrayList<CallInfo> getCallInfo(String phoneNumber,String id) {
        CallInfos.clear();
        String[] projection = new String[] {
                CallLog.Calls.CACHED_NAME
                , CallLog.Calls.TYPE
                , CallLog.Calls.DATE
                , CallLog.Calls.DURATION
        };
        Cursor cursor = ContactsPage.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI
                , projection
                , CallLog.Calls.NUMBER + "=" + phoneNumber
                , null
                , CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CallInfo callInfo = new CallInfo();
                callInfo.setId(id);
                callInfo.setName(cursor.getString(0));
                callInfo.setPhoneNumber(phoneNumber);
                switch (Integer.parseInt(cursor.getString(1))) {
                    case CallLog.Calls.INCOMING_TYPE:callInfo.setType("呼入");break;
                    case CallLog.Calls.OUTGOING_TYPE:callInfo.setType("呼出");break;
                    case CallLog.Calls.MISSED_TYPE:  callInfo.setType("未接");break;
                    default:callInfo.setType("挂断");break;
                }
                //呼叫时间
                Date date = new Date(Long.parseLong(cursor.getString(2)));
                callInfo.setTime(TimeTool.formatForDate(date));
                //通话时长
                callInfo.setDuration(TimeTool.formatDuration(cursor.getLong(3)));
                CallInfos.add(callInfo);
            }while(cursor.moveToNext());
        }
        return CallInfos;
    }

    public static ArrayList<MessageInfo> getMessageInfo(String phoneNumber, String id ,AppCompatActivity activity){
        MessageInfos.clear();
        String[] projection = new String[] {
                Telephony.Sms.ADDRESS
                , Telephony.Sms.PERSON
                , Telephony.Sms.BODY
                , Telephony.Sms.DATE
                , Telephony.Sms.TYPE};
        Cursor cursor = activity.getContentResolver().query(
                Telephony.Sms.CONTENT_URI
                , projection
                , null
                , null
                , Telephony.Sms.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do{
                if ((cursor.getLong(0) + "").contains(phoneNumber)) {
                    MessageInfo info = new MessageInfo();
                    info.setId(id);
                    info.setPhoneNumber(phoneNumber);
                    info.setName(cursor.getString(1));
                    info.setSmsbody(cursor.getString(2).equals("") ? "-" : cursor.getString(2));
                    Date date = new Date(Long.parseLong(cursor.getString(3)));
                    info.setDate(TimeTool.formatForDate(date));
                    switch (Integer.parseInt(cursor.getString(4))) {
                        case Telephony.Sms.MESSAGE_TYPE_SENT:   info.setType("送达");    break;
                        case Telephony.Sms.MESSAGE_TYPE_DRAFT:  info.setType("草稿");    break;
                        case Telephony.Sms.MESSAGE_TYPE_FAILED: info.setType("发送失败"); break;
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:  info.setType("接收");    break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX: info.setType("待发");    break;
                        default:info.setType("重新发送");break;
                    }
                    MessageInfos.add(info);
                }
            }while(cursor.moveToNext());
        }
        Collections.reverse(MessageInfos);
        return MessageInfos;
    }

    static void getContacts(Context context) {
        DataSupport.deleteAll(MPhone.class);
        DataSupport.deleteAll(MEmail.class);
        DataSupport.deleteAll(MContacts.class);
//____________________________________________finish________________________________________________
        //联系人的Uri，也就是content://com.android.contacts/contacts
        String[] projection = new String[] {
                ContactsContract.Contacts._ID
                , ContactsContract.Contacts.DISPLAY_NAME
                , ContactsContract.Contacts.SORT_KEY_PRIMARY};//指定获取_id和display_name两列数据，display_name即为姓名

        Cursor cursorInfo = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI
                , projection, null, null, null);//根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        if (cursorInfo != null && cursorInfo.moveToFirst()) {
            do {

                MContacts contacts = new MContacts();
                contacts.setMid(cursorInfo.getString(0));
                contacts.setName(cursorInfo.getString(1));
                contacts.setSurname(getPinyin(contacts.getName().charAt(0)));
//--------------------------------------get phone number-------------------------------------------
                Cursor cursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                        , new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER}
                        , ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + "=" + contacts.getMid()
                        , null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        MPhone phone = new MPhone();
                        phone.setMid(contacts.getMid());
                        phone.setPhone(cursor.getLong(0) + "");
                        phone.save();
                    } while (cursor.moveToNext());
                }
//--------------------------------------get email address-------------------------------------------
                cursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI
                        , new String[] {ContactsContract.CommonDataKinds.Email.ADDRESS}
                        , ContactsContract.CommonDataKinds.Email.CONTACT_ID  + "=" + contacts.getMid()
                        , null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        MEmail email = new MEmail();
                        email.setMid(contacts.getMid());
                        email.setEmail(cursor.getString(0));
                        email.save();
                    } while (cursor.moveToNext());
                }
                contacts.save();
            } while (cursorInfo.moveToNext());
        }
    }


    static boolean update(String id, String phone, String name, String email, boolean Flag) {
        //插入raw_contacts表，并获取_id属性
        String rawId = getRawId(AddContact.getInstance(), id);
        ContentResolver resolver = AddContact.getInstance().getContentResolver();
        ContentValues values = new ContentValues();
        if (!name.equals("")) {
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + " = ?"
                    , new String[]{rawId});

        }
        if (!phone.equals("")) {
            values.clear();
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
                    , new String[]{rawId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE});
        }

        if (!email.equals("") && !Flag) {
            values.clear();
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
                    , new String[]{rawId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE});
        } else if (Flag) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }
        return true;
    }
    static String insert(String phone, String name, String email) {

        ContentResolver resolver = AddContact.getInstance().getContentResolver();
        ContentValues values = new ContentValues();
        long rawId = ContentUris.parseId(resolver.insert(ContactsContract.RawContacts.CONTENT_URI, values));
        if (!name.equals("")) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }
        if (!phone.equals("")) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }
        if (!email.equals("")) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }
        return getId(AddContact.getInstance(), rawId + "");
    }
    static boolean delete(String id) {
        String rawId = getRawId(ContactsPage.getInstance(), id);
        ContactsPage.getInstance().getContentResolver().delete(
                ContactsContract.RawContacts.CONTENT_URI
                , ContactsContract.RawContacts._ID + " = ?"
                , new String[]{rawId});
        return true;
    }

    private static String getRawId(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] {ContactsContract.RawContacts._ID},
                ContactsContract.RawContacts.CONTACT_ID +" = ?",
                new String[]{id}, null);
        assert cursor != null;
        return cursor.moveToFirst() ? cursor.getString(0) : "";
    }

    private static String getId(Context context, String rawid){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] {ContactsContract.RawContacts.CONTACT_ID},
                ContactsContract.RawContacts._ID + " = ?",
                new String[]{rawid}, null);
        assert cursor != null;
        return cursor.moveToFirst() ? cursor.getString(0) : "";
    }

    static String getPinyin(char name) {
        String[] pinyin;
        pinyin = PinyinHelper.toHanyuPinyinStringArray(name);
        if(pinyin == null) return name + "";
        return pinyin[0].charAt(0) + "";
    }

    private static String phoneCheck(String phone){
        if (!phone.substring(0, 1).equals("1")) {
            phone = (phone.contains("1") && (phone.length() > 11)) ? phone.substring(phone.indexOf("1")) : phone;
            return phone;
        } else {
            return phone;
        }
    }
}
