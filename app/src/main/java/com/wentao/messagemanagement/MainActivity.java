package com.wentao.messagemanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private GetContactsInfo getContactsInfo = new GetContactsInfo();
    private ListView contactsListView;
    private Button btn_info, btn_message, btn_callone, btn_calltwo, btn_showmenu;
    private String priorCount = null, count = null;
    private LinearLayout priorLinearLayout, linearLayout;//// TODO: 2017/11/6

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
        getPermission();
        initListView();
        getContactsInfo.initContactsInfos();
    }

    //初始化ListView
    private void initListView(){
        contactsListView = (ListView) findViewById(R.id.lv_contacts);


//        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                btn_showmenu = (Button) view.findViewById(R.id.btn_showmenu);
//                linearLayout = (LinearLayout) view.findViewById(R.id.liner_info);
//                btn_showmenu.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        linearLayout.setVisibility(View.VISIBLE);
//                        if (priorCount == null) {
//                            linearLayout.setVisibility(View.VISIBLE);
//                        } else if (priorCount != null && priorCount == count) {
//                            linearLayout.setVisibility(View.GONE);
//                        } else if (priorCount != null && priorCount != count) {
//                            linearLayout.setVisibility(View.VISIBLE);
//                            priorLinearLayout.setVisibility(View.GONE);
//                        }
//                        priorLinearLayout = linearLayout;
//                        priorCount = count;
//                    }
//                });
//                linearLayout = (LinearLayout) view.findViewById(R.id.liner_info);
//                count = ((TextView) view.findViewById(R.id.tv_count)).getText().toString();
//                if (priorLinearLayout != null && priorCount.compareTo(count) != 0) {
//                    priorLinearLayout.setVisibility(View.GONE);
//                    linearLayout.setVisibility(View.VISIBLE);
//                } else if(priorLinearLayout != null && priorCount.compareTo(count) == 0) {
//                    linearLayout.setVisibility(View.GONE);
//                } else if (priorLinearLayout == null) {
//                    linearLayout.setVisibility(View.VISIBLE);
//                }
//                priorCount = count;
//                priorLinearLayout = linearLayout; // TODO: 2017/11/6
//            }
//        });
        ContactsAdapter adapter = new ContactsAdapter(
                MainActivity.this,
                R.layout.contacts_item,
                GetContactsInfo.ContactsInfos);
        contactsListView.setAdapter(adapter);//将姓名及电话号码显示到ListView上
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
    private void getPermission(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!=
                PackageManager.PERMISSION_GRANTED){//没有权限需要动态获取
            //动态请求权限
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},1);
        } else {
            initListView();
        }
    }
    //onRequestPermissionsResult获取动态权限后调用函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    initListView();
                } else {
                    Toast.makeText(MainActivity.this,"没有读取通讯录的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:break;
        }
    }
}
