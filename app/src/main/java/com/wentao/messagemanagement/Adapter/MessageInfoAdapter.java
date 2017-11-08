package com.wentao.messagemanagement.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import com.wentao.messagemanagement.db.MessageInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8.
 */

public class MessageInfoAdapter extends ArrayAdapter<MessageInfo> {

    public MessageInfoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MessageInfo> objects) {
        super(context, resource, objects);
    }
}
