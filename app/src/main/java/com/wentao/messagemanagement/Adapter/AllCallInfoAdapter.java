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

import com.wentao.messagemanagement.Activity.ContactsList;
import com.wentao.messagemanagement.Activity.MessageAndCall;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.CallInfo;
import com.wentao.messagemanagement.db.input.Intro;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class AllCallInfoAdapter extends RecyclerView.Adapter<AllCallInfoAdapter.ViewHolder>{
    private static List<Integer> gonePositions = new ArrayList<>();
    private LinkedList<CallInfo> mCallInfos;
    View view;
    public AllCallInfoAdapter(LinkedList<CallInfo> callInfos) {
        mCallInfos = callInfos;
        List<String> infos = new ArrayList<>();
        for (int i = 0; i < mCallInfos.size(); i++) {
            if (!infos.contains(mCallInfos.get(i).getTime().split(" ")[0])) {
                infos.add(mCallInfos.get(i).getTime().split(" ")[0]);
                gonePositions.add(i);
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
        final CallInfo callInfo = mCallInfos.get(position);
        holder.tv_time_mc.setText(check(callInfo.getTime()).split(" ")[1]);
        holder.tv_day_mc.setText(check(callInfo.getTime()).split(" ")[0]);
        holder.tv_info_mc.setText(check(callInfo.getType()));
        holder.tv_name_mc.setText(check(callInfo.getName()));
        holder.tv_first_letter.setText(check(callInfo.getName()).substring(0,1));
        if (gonePositions.contains(position)) {
            holder.line_day.setVisibility(View.VISIBLE);}
        else {holder.line_day.setVisibility(View.GONE);}

        view.setOnClickListener(new View.OnClickListener(){
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
}
