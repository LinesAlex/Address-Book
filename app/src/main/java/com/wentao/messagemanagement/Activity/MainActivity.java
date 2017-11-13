package com.wentao.messagemanagement.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.wentao.messagemanagement.Adapter.ContactsAdapter;
import com.wentao.messagemanagement.tool.GetContactsInfo;
import com.wentao.messagemanagement.R;

public class MainActivity extends AppCompatActivity {
    
    private ContactsAdapter contactsAdapter;
    private ListView contactsListView;
    private SwipeRefreshLayout swipeRefresh;
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
        initListView();
    }

    //初始化ListView
    private void initListView(){

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {refresh();}}
        );
        SearchView searchView = (SearchView) findViewById(R.id.sv_search);
        searchView.setIconifiedByDefault(true);//显示图标及输入框
        searchView.setSubmitButtonEnabled(true);//添加提交搜索按钮
        contactsListView = (ListView) findViewById(R.id.lv_contacts);
        contactsAdapter = new ContactsAdapter(MainActivity.this, R.layout.contacts_item, GetContactsInfo.getContacts());//第一次
        contactsListView.setAdapter(contactsAdapter);//将姓名及电话号码显示到ListView上
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
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.btn_addition);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityOfAddContact.class);
                intent.putExtra("Flag",false);
                startActivityForResult(intent, 1);
            }
        });

        setCatalog();
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GetContactsInfo.getContacts();
                        contactsAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1 :
                if (resultCode == RESULT_OK) {
                    String name = data.getStringExtra("name");
                    refresh();
                    CoordinatorLayout coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
                    Snackbar.make(coordinatorLayout, "联系人 " + name + " 添加成功!", Snackbar.LENGTH_SHORT).show();
                }break;
            default : break;
        }
    }
}