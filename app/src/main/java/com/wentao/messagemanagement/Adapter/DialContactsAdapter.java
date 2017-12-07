package com.wentao.messagemanagement.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
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

public class DialContactsAdapter extends RecyclerView.Adapter<DialContactsAdapter.ViewHolder> {

    private List<DialInfo> mDialList = new LinkedList<>();
    private boolean CHOICE_ITEM = false;
    private boolean SEARCH_ITEM = false;
    private EditText editText;
    public static List<RadioButton> radioButtons;
    public static List<Boolean> checkList;
    private static List<Integer> gonePositions = new ArrayList<>();
    public DialContactsAdapter (List<DialInfo> list) {
        mDialList = list;
    }
    public void initGonePositions() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < mDialList.size(); i++) {
            if (!names.contains(mDialList.get(i).getSurname().toUpperCase())) {
                names.add(mDialList.get(i).getSurname().toUpperCase());
                Log.i("I ", i + "");
                gonePositions.add(i);
            }
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
        if (gonePositions.isEmpty() || gonePositions.size() == 0) {
            initGonePositions();
        }
        if (gonePositions.contains(position) && !SEARCH_ITEM) {
            holder.tv_title_surname.setVisibility(View.VISIBLE);
        } else {
            holder.tv_title_surname.setVisibility(View.GONE);
        }

        if (CHOICE_ITEM) {
            holder.btn_choice.setVisibility(View.VISIBLE);
            radioButtons.add(position, holder.btn_choice);
            checkList.add(position, false);
            holder.btn_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkList.get(position)) {
                        checkList.set(position, false);
                        holder.btn_choice.setChecked(false);
                        NotifyList.Notify.choiceItem(position,false);
                    } else{
                        checkList.set(position, true);

                        NotifyList.Notify.choiceItem(position,true);
                    }
                }
            });
        } else if (SEARCH_ITEM){
            holder.btn_choice.setChecked(false);
            holder.btn_choice.setVisibility(View.VISIBLE);
            holder.btn_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = NotifyList.Notify.getIndex(position);
                    if (checkList.get(index)) {
                        checkList.set(index, false);
                        radioButtons.get(index).setChecked(false);
                        holder.btn_choice.setChecked(false);
                        NotifyList.Notify.choiceItem(index,false);
                    } else {
                        checkList.set(index, true);
                        radioButtons.get(index).setChecked(true);
                        NotifyList.Notify.choiceItem(index,true);
                    }
                }
            });
        } else {
            final String text = holder.tv_phone_dial.getText().toString();
            holder.fl_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(text);
                    DataHandler.getDialList(mDialList, text);
                    notifyDataSetChanged();
                }
            });
        }
    }

    public void choiceAble() {
        CHOICE_ITEM = true;
        radioButtons = new ArrayList<>();
        checkList = new ArrayList<>();
    }

    public void searchAble() {
        SEARCH_ITEM = true;

    }

    public void setViewHolderEditText(EditText text) {
        editText = text;
    }

    @Override
    public int getItemCount() {
        return mDialList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_phone_dial, tv_name_dial, tv_surname, tv_title_surname;
        FrameLayout fl_click;
        RadioButton btn_choice;
        ViewHolder(View itemView) {
            super(itemView);
            tv_title_surname = itemView.findViewById(R.id.tv_title_surname);
            tv_name_dial = itemView.findViewById(R.id.tv_name_dial);
            tv_phone_dial = itemView.findViewById(R.id.tv_phone_dial);
            fl_click = itemView.findViewById(R.id.fl_click_dial);
            btn_choice = itemView.findViewById(R.id.btn_choice);
            tv_surname = itemView.findViewById(R.id.tv_surname);
        }
    }
}
