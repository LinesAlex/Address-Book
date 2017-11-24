package com.wentao.messagemanagement.tool;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class CallFilter {
    public List<String> phone;
    public List<String> type;
    public List<String> time;
    public CallFilter(List<String> p, List<String> t, List<String> m) {
        phone = p;
        type = t;
        time = m;
    }
    public void add(String p, String t, String m) {
        phone.add(p);
        type.add(t);
        time.add(m);
    }
}
