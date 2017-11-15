package com.wentao.messagemanagement.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/11.
 */

public class TimeTool {
    public static SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
    public static String getTime() {
        Date curDate = new  Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        return time;
    }
    public static String formatDuration(long duration) {
        StringBuilder sb = new StringBuilder();

        if (duration == 0) {
            sb.append("00:00");
        } else if (duration > 0 && duration < 60) {
            sb.append("00:");
            if (duration < 10) {
                sb.append("0");
            }
            sb.append(duration);

        } else if (duration > 60 && duration < 3600) {

            long min = duration / 60;
            long sec = duration % 60;
            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        } else if (duration > 3600) {
            long hour = duration / 3600;
            long min = duration % 3600 / 60;
            long sec = duration % 3600 % 60;
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");

            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        }

        return sb.toString();
    }
    public static String formatForDate(Date date) {
        return formatter.format(date);
    }
}
