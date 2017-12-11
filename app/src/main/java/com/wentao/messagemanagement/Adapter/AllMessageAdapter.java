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

public class AllMessageAdapter extends RecyclerView.Adapter<AllMessageAdapter.ViewHolder>{
    private List<MessageInfo> mMessageInfos = new LinkedList<>();
    private List<Integer> gonePositions = new ArrayList<>();
    public static final int OTHER = 1;
    public static final int COMMON = 0;
    private int STATE = 0;
    public AllMessageAdapter(List<MessageInfo> infos, int state) {
        STATE = state;
        mMessageInfos.addAll(infos);
        setGonePosition(mMessageInfos);
    }

    private void setGonePosition(List<MessageInfo> infos) {
        List<String> timeInfos = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            if (!timeInfos.contains(infos.get(i).getDate().split(" ")[0])) {
                timeInfos.add(infos.get(i).getDate().split(" ")[0]);
                gonePositions.add(i);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mc, parent, false);
        return new ViewHolder(view);
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
        if (STATE == OTHER) {
            if (position == 0) {
                holder.tv_day_mc.setVisibility(View.VISIBLE);
                holder.tv_day_mc.setText("服务类短信\n " + check(messageInfo.getDate()).split(" ")[0]);
            }
            if (message.startsWith("【")) {
                String name = message.split("】")[0].replace("【", "");
                holder.tv_name_mc.setText(name);
                holder.tv_first_letter.setText(name.substring(0,1));
            } else if (message.substring(message.length() - 1).equals("】")) {
                String name = message.split("【")[1].replace("】", "");
                holder.tv_name_mc.setText(name);
                holder.tv_first_letter.setText(name.substring(0,1));
            }
        }
        holder.fl_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageAndCall.getInstance(), MessagePage.class);
                intent.putExtra("phone", messageInfo.getPhone());
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
