package com.wentao.messagemanagement;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsInfo {
    private Long id = null;
    private String name = null;
    private String email = null;
    private ArrayList<String> phoneNumber = new ArrayList<String>();


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public ArrayList<String> getPhoneNumber() {return phoneNumber;}
    public String getPhoneNumber(int Index) {
        return phoneNumber.get(Index);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.add(phoneNumber);
    }
    public void setPhoneNumber(int index, String phoneNumber) {this.phoneNumber.add(index, phoneNumber);}
}
