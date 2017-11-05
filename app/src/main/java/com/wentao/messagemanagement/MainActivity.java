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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ActivityOfContactsInfo.class);
                intent.putExtra("name",((TextView) view.findViewById(R.id.tv_name)).getText());
                intent.putExtra("phone",((TextView) view.findViewById(R.id.tv_phone)).getText());
                intent.putExtra("email",((TextView) view.findViewById(R.id.tv_email)).getText());
                intent.putExtra("count",((TextView) view.findViewById(R.id.tv_count)).getText());
                startActivity(intent);
            }
        });
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
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
//                android.R.layout.simple_list_item_1,
//                getContactsInfo.getLinkedOfContactsInfo());//简单显示

        ContactsAdapter adapter = new ContactsAdapter(
                MainActivity.this,
                R.layout.contacts_item,
                getContactsInfo.getContactsInfos());
        contactsListView.setAdapter(adapter);//将姓名及电话号码显示到ListView上
        setCatalog();
    }

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
    private TextView setListSideBar(final String letter, int letterHeight) {
        TextView letterText = new TextView(MainActivity.this);
        letterText.setText(letter);
        letterText.setTextSize(letterHeight / 5 - 3);
        letterText.setHeight(letterHeight - 10);
        letterText.setGravity(Gravity.CENTER);
        letterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactsListView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ContactsAdapter.LetterToPosition.containsKey(letter)) {
                            contactsListView.smoothScrollToPosition(ContactsAdapter.LetterToPosition.get(letter));
                            Toast.makeText(MainActivity.this, letter, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "NULL " + letter, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return letterText;
    }
}
