package com.wentao.messagemanagement.Fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wentao.messagemanagement.Adapter.AllCallInfoAdapter;
import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.tool.ContactsHandler;
import com.wentao.messagemanagement.tool.DataHandler;

/**
 * Created by Administrator on 2017/11/14.
 */

public class CallPageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        RecyclerView call = view.findViewById(R.id.rv_all_call);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        AllCallInfoAdapter adapter = new AllCallInfoAdapter(DataHandler.getAllCalls(getContext()));
        call.setLayoutManager(manager);
        call.setAdapter(adapter);
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
