package com.wentao.messagemanagement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsAdapter extends ArrayAdapter<ContactsInfo> {
    private int resourceId;

    public ContactsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<ContactsInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactsInfo item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView id = (TextView) view.findViewById(R.id.tv_id);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        TextView phone = (TextView) view.findViewById(R.id.tv_phone);
        TextView email = (TextView) view.findViewById(R.id.tv_email);
        id.setText(item.getId().toString());
        name.setText("Name : " + item.getName());
        phone.setText("Phone : " + item.getPhoneNumber(0));
        email.setText("Email : " + item.getEmail());
        return view;
    }
}
