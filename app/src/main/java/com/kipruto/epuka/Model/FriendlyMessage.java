package com.kipruto.epuka.Model;

public class FriendlyMessage {

    private String message;
    private String name;
    private String photoUrl;

    public FriendlyMessage(String message, String name, String photoUrl) {
        this.message = message;
        this.name = name;
        this.photoUrl = photoUrl;
    }
    public FriendlyMessage(){

    }


    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
