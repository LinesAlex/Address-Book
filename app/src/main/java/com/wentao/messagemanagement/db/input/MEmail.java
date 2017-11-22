package com.wentao.messagemanagement.db.input;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MEmail extends DataSupport {
    private long id;
    private String mid;
    private String email;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
