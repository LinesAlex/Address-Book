package com.wentao.messagemanagement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView contactsListView;
    private GetContactsInfo getContactsInfo = new GetContactsInfo();

    private static MainActivity instance;

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = MainActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsListView = (ListView) findViewById(R.id.lv_contacts);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!=
                PackageManager.PERMISSION_GRANTED){//没有权限需要动态获取
            //动态请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},1);

        } else {
            setContactsListView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    setContactsListView();
                } else {
                    Toast.makeText(MainActivity.this,"没有读取通讯录的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }

    }



    private void setContactsListView(){
        //getContacts()方法获取联系人的姓名及电话号码
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                android.R.layout.simple_list_item_1,
//                getContactsInfo.getLinkedOfContactsInfo());

        ContactsAdapter adapter = new ContactsAdapter(MainActivity.this,
                R.layout.contacts_item,
                getContactsInfo.getContactsInfos());
        //将姓名及电话号码显示到ListView上
        contactsListView.setAdapter(adapter);
    }

}
