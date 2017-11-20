package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.CallInfo;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AllCallInfoAdapter extends RecyclerView.Adapter<AllCallInfoAdapter.ViewHolder>{
    static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout line_day;
        TextView tv_day_mc, tv_first_letter, tv_name_mc, tv_info_mc, tv_time_mc;
        ViewHolder(View view){
            super(view);
            line_day = view.findViewById(R.id.line_day);
            tv_day_mc = view.findViewById(R.id.tv_day_mc);
            tv_first_letter = view.findViewById(R.id.tv_first_letter);
            tv_name_mc = view.findViewById(R.id.tv_name_mc);
            tv_info_mc = view.findViewById(R.id.tv_info_mc);
            tv_time_mc = view.findViewById(R.id.tv_time_mc);
        }
    }
    private LinkedList<CallInfo> mCallInfos;
    public AllCallInfoAdapter(LinkedList<CallInfo> callInfos) {
        mCallInfos = callInfos;
        Log.i("AllCallInfoAdapter : ", "do this " + callInfos.size());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mc, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.i("Adapter : ", "do this");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        CallInfo callInfo = mCallInfos.get(position);
//        holder.tv_time_mc.setText(callInfo.getTime().split(" ")[1]);
//        holder.tv_day_mc.setText(callInfo.getTime().split(" ")[0]);
//        holder.tv_info_mc.setText(callInfo.getType());
//        holder.tv_name_mc.setText(callInfo.getName());
//        holder.tv_first_letter.setText(callInfo.getName().substring(0,1));
        CallInfo callInfo = mCallInfos.get(position);
        holder.tv_time_mc.setText(check(callInfo.getTime()));
        holder.tv_day_mc.setText(check(callInfo.getTime()));
        holder.tv_info_mc.setText(check(callInfo.getType()));
        holder.tv_name_mc.setText(check(callInfo.getName()));
        holder.tv_first_letter.setText(check(callInfo.getName()));
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
        return mCallInfos.size();
    }
}
