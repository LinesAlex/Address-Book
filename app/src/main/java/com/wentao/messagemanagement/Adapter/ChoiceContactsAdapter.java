package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.tool.NotifyList;

import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ChoiceContactsAdapter extends RecyclerView.Adapter<ChoiceContactsAdapter.ViewHolder> {
    private List<DialInfo> mChioceList;
    public ChoiceContactsAdapter(List<DialInfo> ChioceList) {
        mChioceList = ChioceList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DialInfo contacts = mChioceList.get(position);
        holder.tv_choice_name.setText(contacts.getName());
        holder.btn_remove_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyList.Notify.notChoiceItem();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChioceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_choice_name;
        Button btn_remove_contacts;
        ViewHolder(View view){
            super(view);
            tv_choice_name = view.findViewById(R.id.tv_choice_name);
            btn_remove_contacts = view.findViewById(R.id.btn_remove_contacts);
        }
    }
}
