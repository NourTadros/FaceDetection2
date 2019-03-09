package com.example.katy.facedetention.Model;

public class PictureModel {
    private String photoURL, userID,PictureID;

    public PictureModel(){

    }

    public PictureModel(String PhotoURL, String userID,String PictureID) {
        this.photoURL = PhotoURL;
        this.userID = userID;
        this.PictureID=PictureID;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPictureID() {
        return PictureID;
    }

    public void setPictureID(String PictureID) {
        this.PictureID = PictureID;
    }



}
