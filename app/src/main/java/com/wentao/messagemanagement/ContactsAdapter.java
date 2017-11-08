package com.wentao.messagemanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsInfo> {
    private int resourceId;
    private String letter;
    private View view;
    private LinearLayout liner_line, liner_right;
    private TextView tv_first_letter, tv_count, tv_name, tv_phone, tv_email, tv_first_name;
    private Button btn_info, btn_message, btn_call, btn_show_menu;
    private static LinearLayout PriorLinear,Linear;
    private static Button PriorMenu, Menu;

    private final Object mLock = new Object();
    private ArrayList<ContactsInfo> mObjects;
    private ArrayList<ContactsInfo> mOriginalValues;
    private boolean visibleFlag=true;

    static public HashMap<String, Integer> LetterToPosition = new HashMap<String, Integer>();
    public ContactsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ContactsInfo> objects) {
        super(context, textViewResourceId, objects);
        mObjects = (ArrayList<ContactsInfo>) objects;
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactsInfo item = getItem(position);
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        initView();
        if (visibleFlag) {setShowLine(item, position);}
        else {setSearchView();}
        setView(item);
        return view;
    }

    public void setSearchView(){
        tv_first_name.setVisibility(View.GONE);
        tv_count.setVisibility(View.GONE);
        liner_right.setVisibility(View.VISIBLE);
    }

    public void setShowLine(ContactsInfo item, int position){
        letter = item.getPinyin();
        if (!LetterToPosition.containsKey(letter)||LetterToPosition.containsValue(position))
        {
            liner_line.setVisibility(View.VISIBLE);
            tv_first_letter.setText(letter);
            if (!LetterToPosition.containsKey(letter))
                LetterToPosition.put(letter, position);
        }
    }

    public void initView() {
        btn_info = (Button) view.findViewById(R.id.btn_info);
        btn_message = (Button) view.findViewById(R.id.btn_message);
        btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_show_menu = (Button) view.findViewById(R.id.btn_showmenu);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_phone);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_first_name = (TextView) view.findViewById(R.id.tv_fristname);
        tv_first_letter = (TextView) view.findViewById(R.id.tv_fristletter);
        liner_line = (LinearLayout) view.findViewById(R.id.liner_line);
        liner_right = (LinearLayout) view.findViewById(R.id.liner_right);
    }

    public void setView(ContactsInfo item) {
        btn_info.setOnClickListener(new ItemsClickListener(view, item));
        btn_message.setOnClickListener(new ItemsClickListener(view, item));
        btn_call.setOnClickListener(new ItemsClickListener(view, item));
        btn_show_menu.setOnClickListener(new ItemsClickListener(view, item));
        tv_count.setText(item.getCount() + "");
        tv_name.setText(item.getName());
        tv_first_name.setText(item.getName().substring(0,1));
        String phoneNumber = checkOutItem(item.getPhoneNumber());
        String emailAdress = checkOutItem(item.getEmail());
        tv_phone.setText(phoneNumber);
        tv_email.setText(emailAdress);
    }

    class ItemsClickListener implements View.OnClickListener {
        private View view;
        private ContactsInfo item;
        public ItemsClickListener(View view, ContactsInfo item) {
            this.view = view;
            this.item = item;
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_call:{
                    String phoneNumber = checkOutItem(item.getPhoneNumber()).trim();
                    if(ActivityCompat.checkSelfPermission(MainActivity.getInstance(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        MainActivity.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_message : {
                    String phoneNumber = checkOutItem(item.getPhoneNumber()).trim();
                    if(ActivityCompat.checkSelfPermission(MainActivity.getInstance(),
                            Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Uri uri = Uri.parse("smsto:" + phoneNumber);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "");
                        MainActivity.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_info : {
                    Intent intent = new Intent(MainActivity.getInstance(), ActivityOfContactsInfo.class);
                    intent.putExtra("name", ((TextView) view.findViewById(R.id.tv_name)).getText());
                    intent.putExtra("count", ((TextView) view.findViewById(R.id.tv_count)).getText());
                    intent.putExtra("phone", ((TextView) view.findViewById(R.id.tv_phone)).getText());
                    intent.putExtra("email", ((TextView) view.findViewById(R.id.tv_email)).getText());
                    intent.putExtra("id", item.getId());
                    MainActivity.getInstance().startActivity(intent);
                }break;
                case  R.id.btn_showmenu : {
                    Linear = (LinearLayout) view.findViewById(R.id.liner_info);
                    Menu = (Button) view.findViewById(R.id.btn_showmenu);
                    if(Linear.getVisibility() == View.GONE)
                    {
                        Linear.setVisibility(View.VISIBLE);
                        Menu.setBackgroundResource(R.drawable.show_menu_bottom);
                        if (PriorLinear != null && PriorMenu != null && PriorLinear != Linear){
                            PriorLinear.setVisibility(View.GONE);
                            PriorMenu.setBackgroundResource(R.drawable.show_menu);
                        }
                    } else {
                        Menu.setBackgroundResource(R.drawable.show_menu);
                        Linear.setVisibility(View.GONE);
                    }
                    PriorMenu = Menu;
                    PriorLinear = Linear;
                }break;
                default : break;
            }
        }
    }

    //检测ArrayList元素是否为空
    private String checkOutItem(ArrayList<String> str)
    {
        if (str.isEmpty()) {return "NULL";}
        else {return str.get(0);}
    }
    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            protected Filter.FilterResults performFiltering(CharSequence prefix) {
                final Filter.FilterResults results = new Filter.FilterResults();

                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList<>(mObjects);
                    }
                }


                if (prefix == null || prefix.length() == 0) {
                    final ArrayList<ContactsInfo> list;
                    synchronized (mLock) {
                        list = new ArrayList<>(mOriginalValues);
                    }
                    results.values = list;
                    results.count = list.size();
                    visibleFlag = true;
                } else {

                    final String prefixString = prefix.toString().toLowerCase();
                    final ArrayList<ContactsInfo> values;
                    synchronized (mLock) {
                        values = new ArrayList<>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<ContactsInfo> newValues = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        final ContactsInfo value = values.get(i);
                        final String nameText = value.getName().toString().replace(" ", "").toLowerCase();//change
                        String phoneText = "";
                        for (String str : value.getPhoneNumber())
                        {
                            phoneText += " " + str.replace("-","").replace(" ","");
                        }
                        // First match against the whole, non-splitted value
                        if (nameText.contains(prefixString) || phoneText.contains(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = (nameText +" "+ phoneText).split(" ");
                            for (String word : words) {
                                if (word.contains(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                    visibleFlag = false;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                //noinspection unchecked
                mObjects = (ArrayList<ContactsInfo>) results.values;
                clear();
                addAll(mObjects);
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

}
