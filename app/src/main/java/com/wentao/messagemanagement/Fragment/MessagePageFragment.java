package com.wentao.messagemanagement.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wentao.messagemanagement.Adapter.AllMessageAdapter;
import com.wentao.messagemanagement.Animation.AnimationUtil;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.MessageInfo;
import com.wentao.messagemanagement.tool.DataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/14.
 */

public class MessagePageFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        List<MessageInfo> om = new ArrayList<>();
        List<MessageInfo> m = new ArrayList<>();
        DataHandler.getAllMessages(getContext(), om, m);

        final RecyclerView message = view.findViewById(R.id.rv_all_message);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        AllMessageAdapter adapter = new AllMessageAdapter(m, AllMessageAdapter.COMMON);
        message.setLayoutManager(manager);
        message.setAdapter(adapter);

        final RecyclerView other_message = view.findViewById(R.id.rv_all_message_other);
        LinearLayoutManager managerO = new LinearLayoutManager(getContext());
        AllMessageAdapter adapterO = new AllMessageAdapter(om, AllMessageAdapter.OTHER);
        other_message.setLayoutManager(managerO);
        other_message.setAdapter(adapterO);

        final Button show = view.findViewById(R.id.btn_show_message);
        final Button show_other = view.findViewById(R.id.btn_show_other_message);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.setAnimationRightToLeftGone(message,getContext());
                AnimationUtil.setAnimationRightToLeftGone(show,getContext());

                AnimationUtil.setAnimationLeftToRightGone(other_message,getContext());
                AnimationUtil.setAnimationLeftToRightGone(show_other,getContext());
            }
        });
        show_other.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AnimationUtil.setAnimationLeftToRightGone(show_other,getContext());
                AnimationUtil.setAnimationLeftToRightGone(other_message,getContext());

                AnimationUtil.setAnimationRightToLeftGone(show,getContext());
                AnimationUtil.setAnimationRightToLeftGone(message,getContext());
            }
        });
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
