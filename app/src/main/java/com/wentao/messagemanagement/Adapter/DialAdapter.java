package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wentao.messagemanagement.R;
import com.wentao.messagemanagement.db.output.DialInfo;
import com.wentao.messagemanagement.tool.DataHandler;
import com.wentao.messagemanagement.tool.NotifyList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DialAdapter extends RecyclerView.Adapter<DialAdapter.ViewHolder> {
    public static int COMMON = 0;
    public static int CHOICE_ITEM = 1;
    public static int SEARCH_ITEM = 2;
    private int STATE = 0;

    public static List<Boolean> searchPositions = null;
    public static List<Boolean> checkPositions = null;
    private List<Integer> gonePositions = new ArrayList<>();
    private List<DialInfo> mDialList = new LinkedList<>();
    private EditText phoneText;
    public DialAdapter(List<DialInfo> list, int state) {
        mDialList = list;
        STATE = state;

        if ((gonePositions.isEmpty() || gonePositions.size() == 0) && STATE == CHOICE_ITEM ){
            List<String> names = new ArrayList<>();
            for (int i = 0; i < mDialList.size(); i++) {
                if (!names.contains(mDialList.get(i).getSurname().toUpperCase())) {
                    names.add(mDialList.get(i).getSurname().toUpperCase());
                    gonePositions.add(i);
                }
            }
        }
        if (STATE == CHOICE_ITEM && checkPositions == null) {
            checkPositions = new ArrayList<>();
            for (int i = 0; i < mDialList.size(); i++) {
                checkPositions.add(false);
            }
        }
        if (STATE == SEARCH_ITEM && searchPositions == null) {
            searchPositions = new LinkedList<>();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_name_dial.setText(mDialList.get(position).getName());
        holder.tv_phone_dial.setText(mDialList.get(position).getPhone());
        holder.tv_surname.setText(mDialList.get(position).getSurname());
        holder.tv_title_surname.setText(mDialList.get(position).getSurname().toUpperCase());
        if (gonePositions.contains(position) && STATE != SEARCH_ITEM) {
            holder.tv_title_surname.setVisibility(View.VISIBLE);
        } else {
            holder.tv_title_surname.setVisibility(View.GONE);
        }
        if (STATE == COMMON) {
            final String text = holder.tv_phone_dial.getText().toString();
            holder.fl_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneText.setText(text);
                    DataHandler.getDialList(mDialList, text);
                    notifyDataSetChanged();
                }
            });
        } else if (STATE == CHOICE_ITEM) {
            holder.btn_choice.setVisibility(View.VISIBLE);
            if (checkPositions.get(position)) {holder.Check(true);}
            else { holder.Check(false);}
            holder.btn_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkPositions.get(position)){
                        NotifyList.Notify.choiceItem(position, false);
                        checkPositions.set(position, false);
                        holder.Check(false);
                    } else{
                        NotifyList.Notify.choiceItem(position, true);
                        checkPositions.set(position, true);
                        holder.Check(true);
                    }
                }
            });
        } else if (STATE == SEARCH_ITEM) {
            holder.btn_choice.setVisibility(View.VISIBLE);
            if (searchPositions.get(position)) {holder.Check(true);}
            else { holder.Check(false);}
            holder.btn_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = NotifyList.Notify.getIndex(position);
                    if (searchPositions.get(position)) {
                        NotifyList.Notify.choiceItem(index,false);
                        checkPositions.set(index, false);
                        searchPositions.set(position, false);
                        holder.Check(false);
                    } else {
                        NotifyList.Notify.choiceItem(index,true);
                        checkPositions.set(index, true);
                        searchPositions.set(position, true);
                        holder.Check(true);
                    }
                }
            });
        }
    }

    public void setViewHolderEditText(EditText text) {
        phoneText = text;
    }

    @Override
    public int getItemCount() {
        return mDialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone_dial, tv_name_dial, tv_surname, tv_title_surname;
        FrameLayout fl_click;
        Button btn_choice;

        ViewHolder(View itemView) {
            super(itemView);
            tv_title_surname = itemView.findViewById(R.id.tv_title_surname);
            tv_name_dial = itemView.findViewById(R.id.tv_name_dial);
            tv_phone_dial = itemView.findViewById(R.id.tv_phone_dial);
            fl_click = itemView.findViewById(R.id.fl_click_dial);
            btn_choice = itemView.findViewById(R.id.btn_choice);
            tv_surname = itemView.findViewById(R.id.tv_surname);
        }

        void Check(boolean check) {
            if (check) {
                btn_choice.setBackgroundResource(R.drawable.btn_choice);
            } else {
                btn_choice.setBackgroundResource(R.drawable.btn_choice_not);
            }
        }
    }
}
