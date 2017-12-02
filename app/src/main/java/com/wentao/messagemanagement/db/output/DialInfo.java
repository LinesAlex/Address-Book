package com.wentao.messagemanagement.db.output;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DialInfo {
    private String name;
    private String phone;
    private boolean isContacts = true;

    public boolean isContacts() {
        return isContacts;
    }

    public void setContacts(boolean contacts) {
        isContacts = contacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
