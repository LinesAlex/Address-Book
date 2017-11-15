package com.wentao.messagemanagement.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.wentao.messagemanagement.Adapter.MessageInfoAdapter;
import com.wentao.messagemanagement.db.MessageInfo;
import com.wentao.messagemanagement.tool.GetContactsInfo;
import com.wentao.messagemanagement.tool.TimeTool;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */

public class MessageReceiver extends BroadcastReceiver{
    private static MessageInfoAdapter Adapter;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 短信时间
        String strTime = "";
        // 短信内容
        StringBuilder strBody = new StringBuilder();
        // 短信发件人
        StringBuilder number = new StringBuilder();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] _pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] message = new SmsMessage[_pdus.length];

            for (int i = 0; i < _pdus.length; i++) {

                message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
            }
            for (SmsMessage currentMessage : message) {
                strBody.append(currentMessage.getDisplayMessageBody());
                number.append(currentMessage.getDisplayOriginatingAddress());
                strTime = TimeTool.formatForDate(new Date(currentMessage.getTimestampMillis()));
            }
            String smsBody = strBody.toString();
            String smsNumber = number.toString();

            Log.v("NUMBER=", smsNumber);
            Log.v("TIME=", strTime);
            Log.v("BODY=", smsBody);

            if (!Adapter.isEmpty()){
                MessageInfo messageInfo = new MessageInfo(smsNumber, smsBody, strTime, "接收");
                GetContactsInfo.MessageInfos.add(messageInfo);
                Adapter.notifyDataSetChanged();
            }

            // 取消消息
            this.abortBroadcast();
        }
    }
    public static void setAdapter(MessageInfoAdapter adapter) {
        Adapter = adapter;
    }

}
