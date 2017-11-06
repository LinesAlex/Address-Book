package com.wentao.messagemanagement;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsInfo> {
    private int resourceId;
    private String letter;
    private Button btn_info, btn_message, btn_callone, btn_calltwo, btn_showmenu;
    private View view;
    private LinearLayout liner_line;
    private static LinearLayout PriorLinear,Linear;
    private static Button PriorMenu, Menu;
    private TextView tv_fristletter, count, name, phone, email, fristname;


    static public HashMap<String, Integer> LetterToPosition = new HashMap<String, Integer>();
    public ContactsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ContactsInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactsInfo item = getItem(position);
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        initView();
        setShowLine(item, position);
        setValue(item);
        return view;
    }

    public void setShowLine(ContactsInfo item, int position){
        letter = item.getPinyin();
        if (!LetterToPosition.containsKey(letter)||LetterToPosition.containsValue(position))
        {
            liner_line.setVisibility(View.VISIBLE);
            tv_fristletter.setText(letter);
            if (!LetterToPosition.containsKey(letter))
                LetterToPosition.put(letter, position);
        }
    }

    public void initView() {
        btn_info = (Button) view.findViewById(R.id.btn_info);
        btn_message = (Button) view.findViewById(R.id.btn_message);
        btn_callone = (Button) view.findViewById(R.id.btn_callone);
        btn_calltwo = (Button) view.findViewById(R.id.btn_calltwo);
        btn_showmenu = (Button) view.findViewById(R.id.btn_showmenu);
        count = (TextView) view.findViewById(R.id.tv_count);
        name = (TextView) view.findViewById(R.id.tv_name);
        phone = (TextView) view.findViewById(R.id.tv_phone);
        email = (TextView) view.findViewById(R.id.tv_email);
        fristname = (TextView) view.findViewById(R.id.tv_fristname);
        tv_fristletter = (TextView) view.findViewById(R.id.tv_fristletter);
        liner_line = (LinearLayout) view.findViewById(R.id.liner_line);
    }

    public void setValue(ContactsInfo item) {
        btn_info.setOnClickListener(new ItemsClickListener(view));
        btn_message.setOnClickListener(new ItemsClickListener(view));
        btn_callone.setOnClickListener(new ItemsClickListener(view));
        btn_calltwo.setOnClickListener(new ItemsClickListener(view));
        btn_showmenu.setOnClickListener(new ItemsClickListener(view));
        count.setText(item.getCount() + "");
        name.setText(item.getName());
        fristname.setText(item.getName().substring(0,1));
        String phoneNumber = checkOutItem(item.getPhoneNumber());
        String emailAdress = checkOutItem(item.getEmail());
        phone.setText("Phone : " + phoneNumber);
        email.setText("Email : " + emailAdress);
    }

    class ItemsClickListener implements View.OnClickListener {
        private View view;
        public ItemsClickListener(View view) {
            this.view = view;
        }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.btn_callone : break;
                case R.id.btn_calltwo : break;
                case R.id.btn_message : break;
                case R.id.btn_info : {
                    Intent intent = new Intent(MainActivity.getInstance(), ActivityOfContactsInfo.class);
                    intent.putExtra("name", ((TextView) view.findViewById(R.id.tv_name)).getText());
                    intent.putExtra("count", ((TextView) view.findViewById(R.id.tv_count)).getText());
                    intent.putExtra("phone", ((TextView) view.findViewById(R.id.tv_phone)).getText());
                    intent.putExtra("email", ((TextView) view.findViewById(R.id.tv_email)).getText());
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
        if (str.isEmpty()) {return "无";}
        else {return str.get(0);}
    }
}
