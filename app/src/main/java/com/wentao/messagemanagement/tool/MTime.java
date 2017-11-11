package com.wentao.messagemanagement.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */

public class MTime {
    public static SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
    public static String getTime() {
        Date curDate = new  Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    public static String formatForDate(Date date) {
        return formatter.format(date);
    }
}
