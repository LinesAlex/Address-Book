package com.wentao.messagemanagement.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.CallInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class CallInfoAdapter extends ArrayAdapter<CallInfo>{
    private int resourceId;
    private View view;
    private TextView tv_time, tv_type, tv_duration;
    private ImageView iv_type;
    public CallInfoAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<CallInfo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CallInfo item = getItem(position);
        view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        initView();
        setView(item);
        return view;
    }
    private void initView() {
        tv_time = view.findViewById(R.id.tv_time);
        tv_type = view.findViewById(R.id.tv_type);
        tv_duration = view.findViewById(R.id.tv_duration);
        iv_type = view.findViewById(R.id.iv_type);

    }
    private void setView(CallInfo item) {
        tv_time.setText(item.getTime());
        tv_type.setText(item.getType());
        tv_duration.setText(item.getDuration());
        switch (item.getType()) {
            case "呼入" :iv_type.setImageResource(R.drawable.icon_call_in);break;
            case "呼出" :iv_type.setImageResource(R.drawable.icon_call_out);break;
            case "未接" :iv_type.setImageResource(R.drawable.icon_call_miss);break;
            case "挂断" :iv_type.setImageResource(R.drawable.icon_call_handup);break;
            default:break;
        }
    }
}