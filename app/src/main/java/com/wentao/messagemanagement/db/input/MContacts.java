package com.wentao.messagemanagement.db.input;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MContacts extends DataSupport {
    private long id;
    private String mid;
    private String name;
    private String surname;

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
