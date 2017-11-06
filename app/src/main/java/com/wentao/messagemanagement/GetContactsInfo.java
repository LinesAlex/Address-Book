package com.wentao.messagemanagement;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/11/4.
 */

public class GetContactsInfo{
    public static ArrayList<ContactsInfo> ContactsInfos = new ArrayList<ContactsInfo>();//联系人基本信息

    private void getContacts() {//_________________________________finish___________________________________
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uriCallInfo = CallLog.Calls.CONTENT_URI;//// TODO: 2017/11/6
        Uri uriContent = ContactsContract.Contacts.CONTENT_URI;
        Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
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
                getInfo(uriPhone, phoneProjection, contactsInfo);
                getInfo(uriEmail, emailProjection, contactsInfo);
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
    private void getInfo(Uri uri, String[] projection, ContactsInfo info){
        Cursor cursor = MainActivity.getInstance().getContentResolver().query(
                uri, projection, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + info.getId(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                info.setEmail(cursor.getString(0));
            } while (cursor.moveToNext());
        }
    }
    //初始化ContactsInfos
    public void initContactsInfos(){//输出以ContactsInfo为元素的ArrayList.
        getContacts();
    }
}
