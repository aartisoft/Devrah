package com.app.devrah.pojo;

/**
 * Created by Rizwan Butt on 17-Aug-17.
 */

public class boardNotificationsPojo {
    public String data;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String userName;
    public String date;

    boardNotificationsPojo(String Data){
        this.data=Data;

    }
    public boardNotificationsPojo(){

    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
