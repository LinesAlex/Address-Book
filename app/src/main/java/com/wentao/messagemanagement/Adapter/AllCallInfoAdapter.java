package com.wentao.messagemanagement.Adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.Activity.MessageAndCall;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.CallInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AllCallInfoAdapter extends RecyclerView.Adapter<AllCallInfoAdapter.ViewHolder>{
    private static List<Integer> gonePositions = new ArrayList<>();
    private List<CallInfo> mCallInfos = new LinkedList<>();
    private List<Integer> count = new ArrayList<>();

    public AllCallInfoAdapter(List<CallInfo> infos) {
       filterList(infos);
    }

    private void filterList(List<CallInfo> infos) {
        List<String> type = new ArrayList<>();
        List<String> time = new ArrayList<>();
        List<String> phone = new ArrayList<>();

        List<String> timeInfos = new ArrayList<>();
        for (int i = 0; i < infos.size(); i++) {
            String m = infos.get(i).getTime().substring(0, infos.get(i).getTime().length() - 2);
            String p = infos.get(i).getPhoneNumber();
            String t = infos.get(i).getType();
            if (!phone.contains(p) ||
                    !type.get(count.size() - 1).contains(t) ||
                    !time.get(count.size() - 1).contains(m.substring(0, m.length() - 2)) ||
                    !phone.get(count.size() - 1).contains(p)) {
                type.add(t);
                time.add(m);
                phone.add(p);
                mCallInfos.add(infos.get(i));
                count.add(1);
            } else {
                int c = mCallInfos.size() - 1;
                count.set(c, count.get(c) + 1);
            }
        }

        for (int i = 0; i < mCallInfos.size(); i++) {
            if (!timeInfos.contains(mCallInfos.get(i).getTime().split(" ")[0])) {
                timeInfos.add(mCallInfos.get(i).getTime().split(" ")[0]);
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
        final CallInfo callInfo = mCallInfos.get(position);
        String countStr = "";
        if (count.get(position) != 1) {
            countStr = " ("+ count.get(position) +")";
        }
        holder.tv_time_mc.setText(check(callInfo.getTime()).split(" ")[1]);

        holder.tv_info_mc.setText(check(callInfo.getType()));
        holder.tv_name_mc.setText(check(callInfo.getName()) + countStr);
        holder.tv_first_letter.setText(check(callInfo.getName()).substring(0,1));
        if (gonePositions.contains(position)) {
            holder.tv_day_mc.setText(check(callInfo.getTime()).split(" ")[0]);
            holder.tv_day_mc.setVisibility(View.VISIBLE);}
        else {holder.tv_day_mc.setVisibility(View.GONE);}
        holder.fl_click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(MessageAndCall.getInstance(),
                        Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" +  callInfo.getPhoneNumber()));
                    MessageAndCall.getInstance().startActivity(intent);
                }
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
        return mCallInfos.size();
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
