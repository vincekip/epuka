package com.kipruto.epuka.Model;

public class Forum {
    private String sender;
    private String receiver;
    private String message;

    public Forum(String sender, String receiver, String message){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public Forum(){ }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
