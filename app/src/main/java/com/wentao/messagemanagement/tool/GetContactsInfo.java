package com.wentao.messagemanagement.tool;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wentao.messagemanagement.Activity.ActivityOfAddContact;
import com.wentao.messagemanagement.Activity.ActivityOfContactsInfo;
import com.wentao.messagemanagement.Activity.MainActivity;
import com.wentao.messagemanagement.db.CallInfo;
import com.wentao.messagemanagement.db.ContactsInfo;
import com.wentao.messagemanagement.db.MessageInfo;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/4.
 */

public class GetContactsInfo {
    public static ArrayList<ContactsInfo> ContactsInfos= new ArrayList<>();//联系人基本信息
    public static ArrayList<CallInfo> CallInfos = new ArrayList<>();
    public static ArrayList<MessageInfo> MessageInfos = new ArrayList<>();
    //初始化ContactsInfos


    public static ArrayList<CallInfo> getCallInfo(String phoneNumber,String id) {
        CallInfos.clear();
        phoneNumber = phoneNumber.replace("-","").replace(" ","");
        Cursor cursor = ActivityOfContactsInfo.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI
                , null, CallLog.Calls.NUMBER + "=" + phoneNumber
                , null, CallLog.Calls.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CallInfo callInfo = new CallInfo();
                callInfo.setId(id);

                callInfo.setPhoneNumber(phoneNumber);
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:callInfo.setType("呼入");break;
                    case CallLog.Calls.OUTGOING_TYPE:callInfo.setType("呼出");break;
                    case CallLog.Calls.MISSED_TYPE:  callInfo.setType("未接");break;
                    default:callInfo.setType("挂断");break;
                }
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //呼叫时间
                callInfo.setTime(MTime.formatForDate(date));
                //通话时长
                callInfo.setDuration(MTime.formatDuration(cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))));
                CallInfos.add(callInfo);
            }while(cursor.moveToNext());
        }
        return CallInfos;
    }

    public static ArrayList<MessageInfo> getMessageInfo(String phoneNumber, String id ,AppCompatActivity activity){
        MessageInfos.clear();
        Cursor cursor = activity.getContentResolver().query(Telephony.Sms.CONTENT_URI
                , null, null
                , null, Telephony.Sms.DEFAULT_SORT_ORDER);
        if (cursor != null && cursor.moveToFirst()) {
            do{
                if (phoneNumber.compareTo(cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS))) == 0) {
                    MessageInfo info = new MessageInfo();
                    info.setId(id);
                    info.setPhoneNumber(phoneNumber.replace("-", "").replace(" ", ""));
                    info.setName(cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON)));
                    info.setSmsbody(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE))));
                    info.setDate(MTime.formatForDate(date));
                    switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_SENT:   info.setType("送达");    break;
                        case Telephony.Sms.MESSAGE_TYPE_DRAFT:  info.setType("草稿");    break;
                        case Telephony.Sms.MESSAGE_TYPE_FAILED: info.setType("发送失败"); break;
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:  info.setType("接收");    break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX: info.setType("待发");    break;
                        default:info.setType("重新发送");break;
                    }
                    if (info.getSmsbody() == null)
                        info.setSmsbody("-");
                    MessageInfos.add(info);
                }
            }while(cursor.moveToNext());
        }
        Collections.reverse(MessageInfos);
        return MessageInfos;
    }

    public static ArrayList<ContactsInfo>  getContacts() {//_________________________________finish___________________________________
        //联系人的Uri，也就是content://com.android.contacts/contacts
        ContactsInfos.clear();
        Uri uriContent = ContactsContract.Contacts.CONTENT_URI;
        Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;

        String emailId = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String phoneId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;

        String[] projection = new String[] {ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.SORT_KEY_PRIMARY};//指定获取_id和display_name两列数据，display_name即为姓名
        String[] phoneProjection = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};//指定获取NUMBER这一列数据
        String[] emailProjection = new String[] {ContactsContract.CommonDataKinds.Email.ADDRESS};//指定获取ADDRESS这一列数据

        Cursor cursorOfContactsInfo = MainActivity.getInstance().getContentResolver().query(uriContent, projection, null, null, null);//根据Uri查询相应的ContentProvider，cursor为获取到的数据集

        if (cursorOfContactsInfo != null && cursorOfContactsInfo.moveToFirst()) {
            do {
                ContactsInfo contactsInfo = new ContactsInfo();
                contactsInfo.setId(cursorOfContactsInfo.getString(0));
                contactsInfo.setName(cursorOfContactsInfo.getString(1));//获取姓名
                contactsInfo.setPinyin(cursorOfContactsInfo.getString(2).substring(0,1));
                getInfo( contactsInfo, contactsInfo.getPhoneNumber(), uriPhone, phoneProjection, phoneId);
                getInfo( contactsInfo, contactsInfo.getEmail(), uriEmail, emailProjection, emailId);
                ContactsInfos.add(contactsInfo);
            } while (cursorOfContactsInfo.moveToNext());
        }
        sortContactsInfos();
        return ContactsInfos;
    }

    private static ArrayList<String> getInfo( ContactsInfo info, ArrayList<String> setStr, Uri uri, String[] projection, String id){
        Cursor cursor = MainActivity.getInstance().getContentResolver().query(uri, projection,id  + "=" + info.getId(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                setStr.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return setStr;
    }

    private static void sortContactsInfos(){
        Collections.sort(ContactsInfos,new ContactsComparator());//比较
        for (int i = 0; i < ContactsInfos.size(); i++) {
            ContactsInfos.get(i).setCount(i + 1);
        }
    }

    public static boolean update(String id, String phone, String name, String email, boolean[] FlagOfInfo) {
        //插入raw_contacts表，并获取_id属性
        String rawId = getRawId(ActivityOfAddContact.getInstance(), id);
        ContentResolver resolver = ActivityOfAddContact.getInstance().getContentResolver();
        ContentValues values = new ContentValues();
        if (!name.equals("")) {
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + " = ?"
                    , new String[]{rawId});

        }

        if (!phone.equals("") && !FlagOfInfo[1]) {
            values.clear();
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
                    , new String[]{rawId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE});
        } else if (FlagOfInfo[1]){
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }

        if (!email.equals("") && !FlagOfInfo[2]) {
            values.clear();
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
            resolver.update(ContactsContract.Data.CONTENT_URI
                    , values
                    , ContactsContract.Data.RAW_CONTACT_ID + "= ? AND " + ContactsContract.Data.MIMETYPE + " = ?"
                    , new String[]{rawId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE});
        } else if (FlagOfInfo[2]) {
            values.clear();
            values.put(ContactsContract.Data.RAW_CONTACT_ID, rawId);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
            values.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email);
            values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE);
            resolver.insert(ContactsContract.Data.CONTENT_URI, values);
        }
        return true;
    }
    public static String insert(String phone, String name, String email) {

        ContentResolver resolver = ActivityOfAddContact.getInstance().getContentResolver();
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
        return getId(ActivityOfAddContact.getInstance(), rawId + "");
    }
    public static boolean delete(Context context, String id) {
        String rawId = getRawId(context, id);
        context.getContentResolver().delete(
                ContactsContract.RawContacts.CONTENT_URI
                , ContactsContract.RawContacts._ID + " = ?"
                , new String[]{rawId});
        return true;
    }
    public static String getRawId(Context context, String id) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI, null,
                ContactsContract.RawContacts.CONTACT_ID +" = ?",
                new String[]{id}, null);
        String rawId = "";
        if (cursor.moveToFirst())
        {
            // 读取第一条记录的RawContacts._ID列的值
            rawId = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
        }
        return rawId;
    }
    public static String getId(Context context, String rawid){
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI, null,
                ContactsContract.RawContacts._ID + " = ?",
                new String[]{rawid}, null);
        String id = "";
        if (cursor.moveToFirst())
        {
            id = cursor.getString(cursor.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
        }
        return id;
    }
}
