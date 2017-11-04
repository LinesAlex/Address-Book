package com.wentao.messagemanagement;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/11/4.
 */

public class GetContactsInfo {
    private ArrayList<ContactsInfo> contactsInfos = new ArrayList<ContactsInfo>();//联系人基本信息
    private void getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uriContent = ContactsContract.Contacts.CONTENT_URI;
        Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Uri uriEmail = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME};//指定获取_id和display_name两列数据，display_name即为姓名
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
                Cursor CusorOfPhone = MainActivity.getInstance().getContentResolver().query(
                        uriPhone, phoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactsInfo.getId(), null, null);//根据联系人的ID获取此人的电话号码
                if (CusorOfPhone != null && CusorOfPhone.moveToFirst()) {//遍历联系人电话
                    do {
                        contactsInfo.setPhoneNumber(CusorOfPhone.getString(0));
                    } while (CusorOfPhone.moveToNext());
                }

                Cursor CusorOfEmail = MainActivity.getInstance().getContentResolver().query(
                        uriEmail, emailProjection, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactsInfo.getId(), null, null);
                if (CusorOfEmail != null && CusorOfEmail.moveToFirst()) {
                    do {
                        contactsInfo.setEmail(CusorOfEmail.getString(0));//CusorOfEmail.getString(0)
                    } while (CusorOfEmail.moveToNext());
                }

                contactsInfos.add(contactsInfo);
            } while (cursorOfContactsInfo.moveToNext());
        }
    }

    public LinkedList<String> getLinkedOfContactsInfo(){//输出以字符串为元素的LinkedList.
        getContacts();
        LinkedList<String> linkedOfContactsInfo = new LinkedList<String>();
        StringBuilder sbContactInfo = new StringBuilder();
        for (int i = 0; i < contactsInfos.size(); i++){
            sbContactInfo.append(
                    "ID : " + contactsInfos.get(i).getId() +
                    "\nName : " + contactsInfos.get(i).getName());
            for (int j = 0; j < contactsInfos.get(i).getPhoneNumber().size(); j++)
            {
                sbContactInfo.append("\nPhoneNumber : " + contactsInfos.get(i).getPhoneNumber(j));
            }
            linkedOfContactsInfo.add(sbContactInfo.toString());
            sbContactInfo.delete(0, sbContactInfo.length());
        }
        return linkedOfContactsInfo;
    }

    public ArrayList<ContactsInfo> getContactsInfos(){//输出以ContactsInfo为元素的ArrayList.
        getContacts();
        ContactsInfo.FIRST_ID = contactsInfos.get(0).getId() - 1;
        return contactsInfos;
    }
}
