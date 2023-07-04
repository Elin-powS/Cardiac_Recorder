package com.example.cardiacrecorder;

public class health_info_model {
    String heart_rate,systolic_pressure,diastolic_pressure,status,date,time,key,UserID;

    public String getheart_rate(){ return heart_rate;}
    public String getsystolic_pressure(){ return systolic_pressure;}
    public String getdiastolic_pressure(){ return diastolic_pressure;}
    public String getstatus(){ return status;}
    public String gettime(){ return time;}
    public String getdate(){ return date;}


    public String getUserID() {
        return UserID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
