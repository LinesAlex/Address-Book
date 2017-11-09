package com.wentao.messagemanagement;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private GetContactsInfo getContactsInfo = new GetContactsInfo();
    private ListView contactsListView;
    private SearchView searchView;
    private boolean Flag = false;
    //获取MainActivity上下文
    private static MainActivity instance;
    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = MainActivity.this;
        getPermission(Manifest.permission.READ_CONTACTS, 1);
        getPermission(Manifest.permission.CALL_PHONE, 2);
        getPermission(Manifest.permission.SEND_SMS, 3);
        getPermission(Manifest.permission.READ_CALL_LOG,4);
        getPermission(Manifest.permission.READ_SMS, 5);
        if (Flag) {
            getContactsInfo.initContactsInfos();
            initListView();
        }
    }

    //初始化ListView
    private void initListView(){
        searchView = (SearchView) findViewById(R.id.sv_search);
        searchView.setIconifiedByDefault(true);//显示图标及输入框
        searchView.setSubmitButtonEnabled(true);//添加提交搜索按钮
        ArrayList<ContactsInfo> items = GetContactsInfo.ContactsInfos;
        contactsListView = (ListView) findViewById(R.id.lv_contacts);
        ContactsAdapter adapter = new ContactsAdapter(
                MainActivity.this,
                R.layout.contacts_item,
                items);
        contactsListView.setAdapter(adapter);//将姓名及电话号码显示到ListView上
        contactsListView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() != 0){
                    contactsListView.setFilterText(newText);
                } else {
                    contactsListView.clearTextFilter();
                }
                return true;
            }
        });
        setCatalog();
    }

    //为ListView添加侧边栏
    private void setCatalog() {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        int letterHeight = height / 27;
        LinearLayout  catalog = (LinearLayout) findViewById(R.id.catalog);
        String[] letter = new String[]{ "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#" };
        for (String str : letter) {
            catalog.addView(setListSideBar(str,letterHeight));
        }
    }
    //setListSideBar设置侧边栏及其OnTouch事件
    private TextView setListSideBar(final String letter, int letterHeight) {
        TextView letterText = new TextView(MainActivity.this);
        letterText.setText(letter);
        letterText.setTextSize(letterHeight / 5 - 6);
        letterText.setHeight(letterHeight - 10);
        letterText.setGravity(Gravity.CENTER);
        letterText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                contactsListView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ContactsAdapter.LetterToPosition.containsKey(letter)) {
                            contactsListView.setSelection(ContactsAdapter.LetterToPosition.get(letter));
                            Toast.makeText(MainActivity.this, letter, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "NULL " + letter, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return false;
            }
        });
        return letterText;
    }
    //获取动态权限
    private void getPermission(String permission, int permissionId){
        if(ContextCompat.checkSelfPermission(MainActivity.this, permission)!= PackageManager.PERMISSION_GRANTED){//没有权限需要动态获取
            //动态请求权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission},permissionId);
        } else {
            Flag = true;
        }
    }
    //onRequestPermissionsResult获取动态权限后调用函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Flag = true;
                } else {
                    Flag = false;
                    Toast.makeText(MainActivity.this,"没有读取通讯录的权限。", Toast.LENGTH_SHORT).show();
                }break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Flag = true;
                } else {
                    Flag = false;
                    Toast.makeText(MainActivity.this,"没有拨打电话的权限。", Toast.LENGTH_SHORT).show();
                }break;
            case 3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Flag = true;
                } else {
                    Flag = false;
                    Toast.makeText(MainActivity.this,"没有发送短信的权限。", Toast.LENGTH_SHORT).show();
                }break;
            default:break;
        }
    }
}
