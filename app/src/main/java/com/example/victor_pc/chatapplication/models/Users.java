package com.example.victor_pc.chatapplication.models;

/**
 * Created by Victor-pc on 15-06-2017.
 */

public class Users {

    private String emailId;
    private String lastMessage;
    private int notifCount;

    public String getEmailId(){ return emailId; }

    public void setEmailId(){ this.emailId = emailId; }

    public String getLastMessage(){ return lastMessage; }

    public void setLastMessage(){ this.lastMessage = lastMessage; }

    public int getNotifCount(){ return notifCount; }

    public void setNotifCount(int notifCount){ this.notifCount = notifCount; }
}
