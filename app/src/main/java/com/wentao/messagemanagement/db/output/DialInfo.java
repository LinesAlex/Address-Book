package com.wentao.messagemanagement.db.output;

import com.wentao.messagemanagement.db.Infos;

/**
 * Created by Administrator on 2017/11/28.
 */

public class DialInfo extends Infos{
    private String name;
    private String phone;
    private String surname;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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
