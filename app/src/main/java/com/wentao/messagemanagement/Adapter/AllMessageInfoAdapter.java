package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.Intro;
import com.wentao.messagemanagement.db.MessageInfo;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AllMessageInfoAdapter extends RecyclerView.Adapter<AllMessageInfoAdapter.ViewHolder>{
    private List<MessageInfo> mMessageInfos = new LinkedList<>();
    private static List<Integer> gonePositions = new ArrayList<>();
    public AllMessageInfoAdapter(LinkedList<MessageInfo> messageInfos) {
        List<String> phoneInfos = new LinkedList<>();
        for (MessageInfo info : messageInfos) {
            if (!phoneInfos.contains(info.getPhoneNumber())) {
                phoneInfos.add(info.getPhoneNumber());
                mMessageInfos.add(info);
            }
        }

        Collections.reverse(mMessageInfos);
        List<String> timeInfos = new ArrayList<>();
        for (int i = 0; i < mMessageInfos.size(); i++) {
            if (!timeInfos.contains(mMessageInfos.get(i).getDate().split(" ")[0])) {
                timeInfos.add(mMessageInfos.get(i).getDate().split(" ")[0]);
                gonePositions.add(i);
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MessageInfo messageInfo = mMessageInfos.get(position);
        holder.tv_time_mc.setText(check(messageInfo.getDate()).split(" ")[1]);
        holder.tv_day_mc.setText(check(messageInfo.getDate()).split(" ")[0]);
        String message = messageInfo.getSmsbody();
        holder.tv_info_mc.setText(check(message.length() > 10 ? message.substring(0, 10) + "..." : message));
        if (DataSupport.findAll(Intro.class).size() > 0 && DataSupport.where("phone = ?", messageInfo.getPhoneNumber()).find(Intro.class).size() > 0) {
            List<Intro> intro = DataSupport.where("phone = ?", messageInfo.getPhoneNumber()).find(Intro.class);
            holder.tv_name_mc.setText(intro.get(0).getName());
            holder.tv_name_mc.setText(intro.get(0).getName().substring(0, 1));
        } else if(messageInfo.getName() != null){
            holder.tv_name_mc.setText(check(messageInfo.getName()));
            holder.tv_first_letter.setText(check(messageInfo.getName()).substring(0,1));
        } else {
            holder.tv_name_mc.setText(check(messageInfo.getPhoneNumber()));
            holder.tv_first_letter.setText(check(messageInfo.getPhoneNumber()).substring(0,1));
        }
        if (gonePositions.contains(position)) {holder.line_day.setVisibility(View.VISIBLE);}
        else {holder.line_day.setVisibility(View.GONE);}
    }
    private String check(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            return str;
        }
    }
    @Override
    public int getItemCount() {
        return  mMessageInfos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout line_day;
        TextView tv_day_mc, tv_first_letter, tv_name_mc, tv_info_mc, tv_time_mc;
        public ViewHolder(View view){
            super(view);
            line_day = view.findViewById(R.id.line_day);
            tv_day_mc = view.findViewById(R.id.tv_day_mc);
            tv_first_letter = view.findViewById(R.id.tv_first_letter);
            tv_name_mc = view.findViewById(R.id.tv_name_mc);
            tv_info_mc = view.findViewById(R.id.tv_info_mc);
            tv_time_mc = view.findViewById(R.id.tv_time_mc);
        }
    }
}
