package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.DialInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DialContactsAdapter extends RecyclerView.Adapter<DialContactsAdapter.ViewHolder> {

    private List<DialInfo> mDialList = new LinkedList<>();
    public DialContactsAdapter (List<DialInfo> list) {
        mDialList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_name_dial.setText(mDialList.get(position).getName());
        holder.tv_phone_dial.setText(mDialList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return mDialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone_dial, tv_name_dial;
        ViewHolder(View itemView) {
            super(itemView);
            tv_name_dial = itemView.findViewById(R.id.tv_name_dial);
            tv_phone_dial = itemView.findViewById(R.id.tv_phone_dial);
        }
    }

}
