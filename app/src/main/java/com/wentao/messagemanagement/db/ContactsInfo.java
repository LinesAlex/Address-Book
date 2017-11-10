package com.wentao.messagemanagement.db;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsInfo {

    private int count = 0;
    private String id = null;
    private String name = null;
    private String pinyin = null;
    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> phoneNumber = new ArrayList<>();

    public String getPinyin() {return pinyin;}
    public void setPinyin(String pinyin) {this.pinyin = pinyin;}

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {return count;}
    public void setCount(int count) {this.count = count;}

    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getEmail() {return email;}
    public String getEmail(int Index) {
        return email.get(Index);
    }

    public void setEmail(ArrayList<String> email) {this.email = email;}
    public void setEmail(int index, String email) {this.email.add(index, email);}

    public ArrayList<String> getPhoneNumber() {return phoneNumber;}
    public String getPhoneNumber(int Index) {return phoneNumber.get(Index);}

    public void setPhoneNumber(ArrayList<String> phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setPhoneNumber(int index, String phoneNumber) {this.phoneNumber.add(index, phoneNumber);}

    @Override
    public String toString() {
        StringBuilder sbContactInfo = new StringBuilder();
                sbContactInfo.append("Count : ").append(getCount()).append("\nName : ").append(getName());
        for (int i = 0; i < getPhoneNumber().size(); i++)
        {
            sbContactInfo.append("\nPhoneNumber : ").append(getPhoneNumber(i));
        }
        for (int i = 0; i < getEmail().size(); i++)
        {
            sbContactInfo.append("\nEmail : ").append(getEmail(i));
        }
        return sbContactInfo.toString();
    }
}
