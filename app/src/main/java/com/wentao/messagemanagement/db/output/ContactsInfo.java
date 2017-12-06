package com.wentao.messagemanagement.db.output;


import com.wentao.messagemanagement.db.Infos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/4.
 */

public class ContactsInfo extends Infos{
    private int count = 0;
    private String id = null;
    private String name = null;
    private String surname = null;

    private ArrayList<String> email = new ArrayList<>();
    private ArrayList<String> phoneNumber = new ArrayList<>();

    public String getSurname() {return surname;}
    public void setSurname(String pinyin) {this.surname = pinyin;}

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

    public void setEmail(String email) {this.email.add(email);}

    public ArrayList<String> getPhoneNumber() {return phoneNumber;}
    public String getPhoneNumber(int Index) {return phoneNumber.get(Index);}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber.add(phoneNumber);}

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
