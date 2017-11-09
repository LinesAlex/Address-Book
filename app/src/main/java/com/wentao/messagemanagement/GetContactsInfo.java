package com.wentao.messagemanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.wentao.messagemanagement.db.CallInfo;
import com.wentao.messagemanagement.db.MessageInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/4.
 */

public class GetContactsInfo {
    public static ArrayList<ContactsInfo> ContactsInfos = new ArrayList<ContactsInfo>();//联系人基本信息

    public static ArrayList<CallInfo> getCallInfo(String phoneNumber,String id) {
        ArrayList<CallInfo> infos = new ArrayList<>();
        Cursor cursor = ActivityOfContactsInfo.getInstance().getContentResolver().query(CallLog.Calls.CONTENT_URI
                , null, CallLog.Calls.NUMBER + "=" + phoneNumber.replace("-","").replace(" ","")
                , null, CallLog.Calls.DEFAULT_SORT_ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                CallInfo callInfo = new CallInfo();
                callInfo.setId(id);
                callInfo.setPhoneNumber(phoneNumber.replace("-","").replace(" ",""));
                //号码
                //cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                //呼叫类型
                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callInfo.setType("呼入");
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callInfo.setType("呼出");
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callInfo.setType("未接");
                        break;
                    default:
                        callInfo.setType("挂断");//应该是挂断.根据我手机类型判断出的
                        break;
                }
                SimpleDateFormat sfd = new SimpleDateFormat("MM月dd日 HH:mm");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //呼叫时间
                callInfo.setTime(sfd.format(date));
                //通话时长
                callInfo.setDuration(formatDuration(cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))));
                infos.add(callInfo);
            }while(cursor.moveToNext());
        }
        return infos;
    }
    public static ArrayList<MessageInfo> getMessageInfo(String phoneNumber, String id){
        ArrayList<MessageInfo> infos = new ArrayList<>();
        Cursor cursor = ActivityOfContactsInfo.getInstance().getContentResolver().query(Telephony.Sms.CONTENT_URI
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
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                    Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE))));
                    info.setDate(dateFormat.format(date));
                    switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE)))) {
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
                        default:info.setType("重新发送");break;
                    }
                    if (info.getSmsbody() == null)
                        info.setSmsbody("-");
                    infos.add(info);
                }
            }while(cursor.moveToNext());
        }
        return infos;
    }

    private void getContacts() {//_________________________________finish___________________________________
        //联系人的Uri，也就是content://com.android.contacts/contacts

        Uri uriContent = ContactsContract.Contacts.CONTENT_URI;
        Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String emailId = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String phoneId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.SORT_KEY_PRIMARY};//指定获取_id和display_name两列数据，display_name即为姓名
        String[] phoneProjection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER};//指定获取NUMBER这一列数据
        String[] emailProjection = new String[] {
                ContactsContract.CommonDataKinds.Email.ADDRESS};//指定获取ADDRESS这一列数据
        Cursor cursorOfContactsInfo = MainActivity.getInstance().getContentResolver().query(
                uriContent, projection, null, null, null);//根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        if (cursorOfContactsInfo != null && cursorOfContactsInfo.moveToFirst()) {
            do {
                ContactsInfo contactsInfo = new ContactsInfo();
                contactsInfo.setId(cursorOfContactsInfo.getInt(0));
                contactsInfo.setName(cursorOfContactsInfo.getString(1));//获取姓名
                contactsInfo.setPinyin(cursorOfContactsInfo.getString(2).substring(0,1));
                getInfo(uriPhone, phoneProjection, contactsInfo, phoneId, contactsInfo.getPhoneNumber());
                getInfo(uriEmail, emailProjection, contactsInfo, emailId, contactsInfo.getEmail());
                ContactsInfos.add(contactsInfo);
            } while (cursorOfContactsInfo.moveToNext());
        }
        sortContactsInfos();
    }
    private void sortContactsInfos(){
        Collections.sort(ContactsInfos,new ContactsComparator());//比较
        for (int i = 0; i < ContactsInfos.size(); i++) {
            ContactsInfos.get(i).setCount(i + 1);
        }
    }
    private ArrayList<String> getInfo(Uri uri, String[] projection, ContactsInfo info, String id, ArrayList<String> setStr){
        Cursor cursor = MainActivity.getInstance().getContentResolver().query(uri, projection,id  + "=" + info.getId(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                setStr.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return setStr;
    }
    //初始化ContactsInfos
    public void initContactsInfos(){//输出以ContactsInfo为元素的ArrayList.
        getContacts();
    }

    private static String formatDuration(long duration) {
        StringBuilder sb = new StringBuilder();

        if (duration == 0) {
            sb.append("00:00");
        } else if (duration > 0 && duration < 60) {
            sb.append("00:");
            if (duration < 10) {
                sb.append("0");
            }
            sb.append(duration);

        } else if (duration > 60 && duration < 3600) {

            long min = duration / 60;
            long sec = duration % 60;
            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        } else if (duration > 3600) {
            long hour = duration / 3600;
            long min = duration % 3600 / 60;
            long sec = duration % 3600 % 60;
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");

            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        }

        return sb.toString();
    }

}
