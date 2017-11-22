package com.wentao.messagemanagement.db.input;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MPhone extends DataSupport {
    private long id;
    private String mid;
    private String phone;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
