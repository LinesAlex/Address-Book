package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.tool.DataHandler;
import com.wentao.messagemanagement.tool.NotifyList;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DialContactsAdapter extends RecyclerView.Adapter<DialContactsAdapter.ViewHolder> {

    private List<DialInfo> mDialList = new LinkedList<>();
    private boolean chioceFlag = false;
    private EditText editText;
    public DialContactsAdapter (List<DialInfo> list) {
        mDialList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_name_dial.setText(mDialList.get(position).getName());
        holder.tv_phone_dial.setText(mDialList.get(position).getPhone());
        if (chioceFlag) {
            holder.btn_choice.setVisibility(View.VISIBLE);
            holder.btn_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotifyList.Notify.choiceItem(position);
                }
            });
        } else {
            final String text = holder.tv_phone_dial.getText().toString();
            holder.fl_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(text);
                    DataHandler.getDialList(mDialList, text);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void setChioceFlag() {chioceFlag = true;}
    public void setViewHolderEditText(EditText text) {
        editText = text;
    }

    @Override
    public int getItemCount() {
        return mDialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone_dial, tv_name_dial;
        FrameLayout fl_click;
        Button btn_choice;
        ViewHolder(View itemView) {
            super(itemView);
            tv_name_dial = itemView.findViewById(R.id.tv_name_dial);
            tv_phone_dial = itemView.findViewById(R.id.tv_phone_dial);
            fl_click = itemView.findViewById(R.id.fl_click_dial);
            btn_choice = itemView.findViewById(R.id.btn_choice);
        }
    }
}
