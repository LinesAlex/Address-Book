package com.wentao.messagemanagement.db.output;

/**
 * Created by Administrator on 2017/11/8.
 */

public class MessageInfo {
    private String id;
    private String phone;
    private String smsbody;
    private String date;
    private String type;
    private String name;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getPhone() {return phone;}
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsbody() {
        return smsbody;
    }
    public void setSmsbody(String smsbody) {
        this.smsbody = smsbody;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public MessageInfo(String phone, String smsbody, String date, String type) {
        this.phone = phone;
        this.smsbody = smsbody;
        this.date = date;
        this.type = type;
    }
    public MessageInfo(){}
}
