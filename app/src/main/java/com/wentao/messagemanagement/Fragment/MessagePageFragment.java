package com.wentao.messagemanagement.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wentao.messagemanagement.Adapter.AllMessageInfoAdapter;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.tool.ContactsHandler;

/**
 * Created by Administrator on 2017/11/14.
 */

public class MessagePageFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        RecyclerView message = view.findViewById(R.id.rv_all_message);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        message.setLayoutManager(manager);
        ContactsHandler.getAllMessages(getContext());
        AllMessageInfoAdapter messageInfoAdapter = new AllMessageInfoAdapter(ContactsHandler.AllMessages);
        message.setAdapter(messageInfoAdapter);
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
