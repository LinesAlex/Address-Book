package com.wentao.messagemanagement.db;

/**
 * Created by Administrator on 2017/11/8.
 */

public class CallInfo {
    private String id;
    private String type;
    private String time;
    private String duration;
    private String phoneNumber;

    public String getPhoneNumber() {return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getDuration() {return duration;}
    public void setDuration(String duration) {this.duration = duration;}
}