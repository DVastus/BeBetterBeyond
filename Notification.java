package com.firstapp.bbb;

public class Notification {

    private String senderId;
    private String recipientId;
    private String title;
    private String message;
    private long timestamp;


    // for Firestore
    public Notification() {}

    public Notification(String senderId, String recipientId, String title, String message, long timestamp){
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSenderId(){
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getTitle(){
        return title;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
/**
    public boolean isRead() {
        return isRead;
    }
**/
    // Setters
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setRecipientId(String recipientId){
        this.recipientId = recipientId;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public void setTitle(String title){
        this.title = title;
    }
 /**
    public void setRead(boolean read) {
        isRead = read;
    }
  **/
}
