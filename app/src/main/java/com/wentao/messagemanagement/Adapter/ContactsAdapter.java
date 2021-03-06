package com.wentao.messagemanagement.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.Activity.ContactsPage;
import com.wentao.messagemanagement.Activity.MessagePage;
import com.wentao.messagemanagement.Activity.ContactsList;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.ContactsInfo;
import com.wentao.messagemanagement.tool.DataHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsInfo> {
    private int resourceId;
    private View view;

    private LinearLayout liner_line, liner_right;
    private TextView tv_first_letter, tv_count, tv_name, tv_phone, tv_email, tv_first_name;
    private Button btn_info, btn_message, btn_call, btn_show_menu;
    private static LinearLayout PriorLinear;//用于menu的显示
    private static Button PriorMenu;
//----------------------------------------------------------------用于getFilter()方法
    private final Object mLock = new Object();
    private ArrayList<ContactsInfo> mObjects;
    private ArrayList<ContactsInfo> mOriginalValues;
    private boolean visibleFlag=true;
//----------------------------------------------------------------
    static public HashMap<String, Integer> LetterToPosition = new HashMap<>();
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
        if (visibleFlag) {
            setShowLine(item, position);
        } else {
            setSearchView();
        }
        setView(item);
        return view;
    }

    private void setSearchView(){
        tv_first_name.setVisibility(View.GONE);
        tv_count.setVisibility(View.GONE);
        liner_right.setVisibility(View.VISIBLE);
    }

    private void setShowLine(ContactsInfo item, int position){
        String letter = item.getSurname().toUpperCase();
        if (!LetterToPosition.containsKey(letter)||LetterToPosition.containsValue(position))
        {
            liner_line.setVisibility(View.VISIBLE);
            tv_first_letter.setText(letter);
            if (!LetterToPosition.containsKey(letter))
                LetterToPosition.put(letter, position);
        }
    }

    private void initView() {
        btn_info =  view.findViewById(R.id.btn_info);
        btn_message = view.findViewById(R.id.btn_message);
        btn_call =  view.findViewById(R.id.btn_call);
        btn_show_menu = view.findViewById(R.id.btn_showmenu);
        tv_count =  view.findViewById(R.id.tv_count);
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        tv_first_name = view.findViewById(R.id.tv_fristname);
        tv_first_letter = view.findViewById(R.id.tv_fristletter);
        liner_line = view.findViewById(R.id.liner_line);
        liner_right = view.findViewById(R.id.liner_right);
    }

    private void setView(ContactsInfo item) {
        btn_info.setOnClickListener(new ItemsClickListener(view, item.getId()));
        btn_message.setOnClickListener(new ItemsClickListener(view, item.getId()));
        btn_call.setOnClickListener(new ItemsClickListener(view, item.getId()));
        btn_show_menu.setOnClickListener(new ItemsClickListener(view, item.getId()));
        tv_count.setText(item.getCount() + "");
        tv_name.setText(item.getName());
        tv_first_name.setText(item.getName().substring(0,1));
        tv_phone.setText(item.getPhoneNumber().get(0));
        tv_email.setText(item.getEmail().get(0));
    }

    private class ItemsClickListener implements View.OnClickListener {
        private View view;
        private String id;
        ItemsClickListener(View view, String mid) {
            this.view = view;
            this.id = mid;
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_call:{
                    String phoneNumber = DataHandler.getPhone(id);
                    if(ActivityCompat.checkSelfPermission(ContactsList.getInstance(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        ContactsList.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_message : {
                    Intent intent = new Intent(ContactsList.getInstance(), MessagePage.class);
                    intent.putExtra("id", id);
                    ContactsList.getInstance().startActivity(intent);
                }break;
                case R.id.btn_info : {
                    Intent intent = new Intent(ContactsList.getInstance(), ContactsPage.class);
                    intent.putExtra("id", id);
                    ContactsList.getInstance().startActivityForResult(intent, 2);
                }break;
                case  R.id.btn_showmenu : {
                    LinearLayout linear = view.findViewById(R.id.liner_info);
                    Button menu = view.findViewById(R.id.btn_showmenu);
                    if(linear.getVisibility() == View.GONE)
                    {
                        linear.setVisibility(View.VISIBLE);
                        menu.setBackgroundResource(R.drawable.button_show_menu_2);
                        if (PriorLinear != null && PriorMenu != null && PriorLinear != linear){
                            PriorLinear.setVisibility(View.GONE);
                            PriorMenu.setBackgroundResource(R.drawable.button_show_menu_1);
                        }
                    } else {
                        menu.setBackgroundResource(R.drawable.button_show_menu_1);
                        linear.setVisibility(View.GONE);
                    }
                    PriorMenu = menu;
                    PriorLinear = linear;
                }break;
                default : break;
            }
        }
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter(){
            protected FilterResults performFiltering(CharSequence prefix) {
                final FilterResults results = new FilterResults();

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
                        final String nameText = value.getName().replace(" ", "").toLowerCase();//change
                        String phoneText = "";
                        for (String str : value.getPhoneNumber())
                        {
                            phoneText += " " + str;
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
            protected void publishResults(CharSequence constraint, FilterResults results) {
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
    }
}