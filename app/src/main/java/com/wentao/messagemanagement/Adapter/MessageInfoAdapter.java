package com.wentao.messagemanagement.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.MessageInfo;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class MessageInfoAdapter extends ArrayAdapter<MessageInfo> {
    private int resourceId;
    private View view;
    private TextView tv_sms_time, tv_sms_message, tv_sms_type;
    private LinearLayout linear;
    public MessageInfoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MessageInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        MessageInfo item = getItem(position);

        assert item != null;
        switch (item.getType()) {
            case "送达":
            case "草稿":
            case "发送失败":
            case "待发":
            case "重新发送": {initView(1);}break;
            case "接收":     {initView(0);}break;
            default:break;
        }
        setView(item);
        return view;
    }

    private void initView(int choice) {
        switch (choice)
        {
            case 0:{
                tv_sms_time = view.findViewById(R.id.tv_sms_time_get);
                tv_sms_message = view.findViewById(R.id.tv_sms_message_get);
                tv_sms_type = view.findViewById(R.id.tv_sms_type_get);
                linear = view.findViewById(R.id.linear_get);
            }break;
            case 1:{
                tv_sms_time = view.findViewById(R.id.tv_sms_time_send);
                tv_sms_message = view.findViewById(R.id.tv_sms_message_send);
                tv_sms_type = view.findViewById(R.id.tv_sms_type_send);
                linear = view.findViewById(R.id.linear_send);
            }break;
        }
    }
    private void setView(MessageInfo item) {
        linear.setVisibility(View.VISIBLE);
        tv_sms_time.setText(item.getDate());
        tv_sms_message.setText(item.getSmsbody());
        tv_sms_type.setText(item.getType());
    }
}