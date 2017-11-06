package com.wentao.messagemanagement;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsInfo> {
    private int resourceId;
    private String letter;
    private View view;
    private LinearLayout liner_line;
    private TextView tv_fristletter, count, name, phone, email, fristname;
    private Button btn_info, btn_message, btn_call, btn_showmenu;
    private static LinearLayout PriorLinear,Linear;
    private static Button PriorMenu, Menu;


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
        setView(item);
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
        btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_showmenu = (Button) view.findViewById(R.id.btn_showmenu);
        count = (TextView) view.findViewById(R.id.tv_count);
        name = (TextView) view.findViewById(R.id.tv_name);
        phone = (TextView) view.findViewById(R.id.tv_phone);
        email = (TextView) view.findViewById(R.id.tv_email);
        fristname = (TextView) view.findViewById(R.id.tv_fristname);
        tv_fristletter = (TextView) view.findViewById(R.id.tv_fristletter);
        liner_line = (LinearLayout) view.findViewById(R.id.liner_line);
    }

    public void setView(ContactsInfo item) {
        btn_info.setOnClickListener(new ItemsClickListener(view));
        btn_message.setOnClickListener(new ItemsClickListener(view));
        btn_call.setOnClickListener(new ItemsClickListener(view));
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
                case R.id.btn_call:{
                    String phoneNumber = ((TextView) view.findViewById(R.id.tv_phone)).getText().toString().trim();
                    if(ActivityCompat.checkSelfPermission(MainActivity.getInstance(),
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        MainActivity.getInstance().startActivity(intent);
                    }
                }break;
                case R.id.btn_message : {
                    String phoneNumber = ((TextView) view.findViewById(R.id.tv_phone)).getText().toString().trim();
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
