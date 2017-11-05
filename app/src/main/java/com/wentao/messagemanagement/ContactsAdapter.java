package com.wentao.messagemanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    static public Map<String, Integer> LetterToPosition = new HashMap<String, Integer>();
    static public LinkedList<Integer> Position = new LinkedList<Integer>();
    public ContactsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ContactsInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactsInfo item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.liner_line);
        TextView tv_fristletter = (TextView) view.findViewById(R.id.tv_fristletter);
        letter = item.getPinyin();
        if (!LetterToPosition.containsKey(letter)||Position.contains(position))
        {
            linearLayout.setVisibility(View.VISIBLE);
            tv_fristletter.setText(letter);
            Position.add(position);
        }
        LetterToPosition.put(letter, position);

        TextView count = (TextView) view.findViewById(R.id.tv_count);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        TextView phone = (TextView) view.findViewById(R.id.tv_phone);
        TextView email = (TextView) view.findViewById(R.id.tv_email);
        TextView fristname = (TextView) view.findViewById(R.id.tv_fristname);
        count.setText(item.getCount() + "");
        name.setText(item.getName());
        fristname.setText(item.getName().substring(0,1));
        String phoneNumber = checkOutItem(item.getPhoneNumber());
        String emailAdress = checkOutItem(item.getEmail());
        phone.setText("Phone : " + phoneNumber);
        email.setText("Email : " + emailAdress);
        return view;
    }
    private String checkOutItem(ArrayList<String> str)
    {
        if (str.isEmpty()) {return "无";}
        else {return str.get(0);}
    }
}
