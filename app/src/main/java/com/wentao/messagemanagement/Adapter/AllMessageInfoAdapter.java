package com.wentao.messagemanagement.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.Activity.MessageAndCall;
import com.wentao.messagemanagement.Activity.MessagePage;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.MessageInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AllMessageInfoAdapter extends RecyclerView.Adapter<AllMessageInfoAdapter.ViewHolder>{
    private List<MessageInfo> mMessageInfos = new LinkedList<>();
    private List<Integer> gonePositions = new ArrayList<>();
    private int MessageIndex;
    View view;

    public AllMessageInfoAdapter(List<MessageInfo> infos) {
        filterList(infos);
    }

    private void filterList(List<MessageInfo> infos) {
        List<MessageInfo> otherMessage = new ArrayList<>();

        List<String> phoneInfos = new LinkedList<>();

        for (MessageInfo info : infos) {
            if (!phoneInfos.contains(info.getPhoneNumber())) {
                phoneInfos.add(info.getPhoneNumber());
                if (info.getPhoneNumber().startsWith("106"))
                {
                    otherMessage.add(info);
                } else {
                    mMessageInfos.add(info);
                }
            }
        }
        setGonePosition(mMessageInfos, 0);
        setGonePosition(otherMessage, mMessageInfos.size() - 1);
        MessageIndex = mMessageInfos.size() - 1;
        mMessageInfos.addAll(otherMessage);
    }

    private void setGonePosition(List<MessageInfo> infos, int index) {
        List<String> timeInfos = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            if (!timeInfos.contains(infos.get(i).getDate().split(" ")[0])) {
                timeInfos.add(infos.get(i).getDate().split(" ")[0]);
                gonePositions.add(index + i);
            }
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MessageInfo messageInfo = mMessageInfos.get(position);
        holder.tv_time_mc.setText(check(messageInfo.getDate()).split(" ")[1]);
        String message = messageInfo.getSmsbody();
        holder.tv_info_mc.setText(check(message.length() > 15 ? message.substring(0, 15) + "..." : message));
        holder.tv_name_mc.setText(check(messageInfo.getName()));
        holder.tv_first_letter.setText(check(messageInfo.getName()).substring(0,1));
        if (gonePositions.contains(position)) {
            holder.tv_day_mc.setVisibility(View.VISIBLE);
            holder.tv_day_mc.setText(check(messageInfo.getDate()).split(" ")[0]);}
        else {holder.tv_day_mc.setVisibility(View.GONE);}
        if (position == MessageIndex) {holder.tv_day_mc.setText("服务类短信\n "+check(messageInfo.getDate()).split(" ")[0]);}
        holder.fl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.getInstance(), MessagePage.class);
                intent.putExtra("phone", messageInfo.getPhoneNumber());
                intent.putExtra("id", messageInfo.getId());
                MessageAndCall.getInstance().startActivity(intent);
            }
        });
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
        FrameLayout line_day, fl_click;
        TextView tv_day_mc, tv_first_letter, tv_name_mc, tv_info_mc, tv_time_mc;
        ViewHolder(View view){
            super(view);
            line_day = view.findViewById(R.id.line_day);
            tv_day_mc = view.findViewById(R.id.tv_day_mc);
            tv_first_letter = view.findViewById(R.id.tv_first_letter);
            tv_name_mc = view.findViewById(R.id.tv_name_mc);
            tv_info_mc = view.findViewById(R.id.tv_info_mc);
            tv_time_mc = view.findViewById(R.id.tv_time_mc);
            fl_click = view.findViewById(R.id.fl_click);
        }
    }
}
