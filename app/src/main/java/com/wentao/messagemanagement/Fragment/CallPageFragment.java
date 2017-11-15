package com.wentao.messagemanagement.Fragment;

import android.support.v4.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wentao.messagemanagement.R;

/**
 * Created by Administrator on 2017/11/14.
 */

public class CallPageFragment extends Fragment {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView call = view.findViewById(R.id.rv_all_call);
//        call.setLayoutManager(layoutManager);
//        AllCallInfoAdapter callInfoAdapter = new AllCallInfoAdapter(   );
//        call.setAdapter(callInfoAdapter);
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
