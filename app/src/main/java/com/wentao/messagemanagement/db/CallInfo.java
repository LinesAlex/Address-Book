package com.wentao.messagemanagement.db;

/**
 * Created by Administrator on 2017/11/8.
 */

public class CallInfo {
    private String Id;
    private String type;
    private String time;
    private String duration;

    public String getId() {return Id;}
    public void setId(String id) {Id = id;}

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getDuration() {return duration;}
    public void setDuration(String duration) {this.duration = duration;}
}
