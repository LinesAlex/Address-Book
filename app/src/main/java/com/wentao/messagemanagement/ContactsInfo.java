package com.wentao.messagemanagement;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsInfo {
    public static int FIRST_ID;
    private int id = 0;
    private String name = null;
    private ArrayList<String> email = new ArrayList<String>();
    private ArrayList<String> phoneNumber = new ArrayList<String>();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getEmail() {return email;}
    public String getEmail(int Index) {
        return email.get(Index);
    }

    public void setEmail(String email) {
        this.email.add(email);
    }
    public void setEmail(int index, String email) {this.email.add(index, email);}

    public ArrayList<String> getPhoneNumber() {return phoneNumber;}
    public String getPhoneNumber(int Index) {return phoneNumber.get(Index);}

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.add(phoneNumber);
    }
    public void setPhoneNumber(int index, String phoneNumber) {this.phoneNumber.add(index, phoneNumber);}
}
