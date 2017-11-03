package com.wentao.messagemanagement;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Button mGetContactsButton;
    ListView mContactsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGetContactsButton = (Button) findViewById(R.id.btn_contacts);
        mContactsListView = (ListView) findViewById(R.id.lv_contacts);
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!=
                PackageManager.PERMISSION_GRANTED){//没有权限需要动态获取
            //动态请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},1);

        }
        mGetContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getContacts()方法获取联系人的姓名及电话号码
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getContacts());
                //将姓名及电话号码显示到ListView上
                mContactsListView.setAdapter(adapter);
            }
        });
    }
    private LinkedList<String> getContacts() {
        //联系人的Uri，也就是content://com.android.contacts/contacts
        Uri uriContent = ContactsContract.Contacts.CONTENT_URI;
        Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //指定获取_id和display_name两列数据，display_name即为姓名
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
        Cursor cursor = this.getContentResolver().query(uriContent, projection, null, null, null);
        LinkedList<String> linkedList = new LinkedList<String>();
        StringBuilder stringBuilder = new StringBuilder();
        if (cursor != null && cursor.moveToFirst()) {
            do {

                Long id = cursor.getLong(0);
                String name = cursor.getString(1);//获取姓名
                stringBuilder.append(id + "\n姓名：" + name);
                //指定获取NUMBER这一列数据
                String[] phoneProjection = new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER};
                //根据联系人的ID获取此人的电话号码
                Cursor phonesCusor = this.getContentResolver().query(
                        uriPhone, phoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

                //因为每个联系人可能有多个电话号码，所以需要遍历
                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                    do {
                        String phoneNumber = phonesCusor.getString(0);
                        stringBuilder.append("\n电话号码：" + phoneNumber);
                    }while (phonesCusor.moveToNext());
                }
                linkedList.add(stringBuilder.toString());
                stringBuilder.delete(0,stringBuilder.length());
            } while (cursor.moveToNext());
        }
        return linkedList;
    }
}
