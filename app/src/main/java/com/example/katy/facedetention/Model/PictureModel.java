package com.example.katy.facedetention.Model;

public class PictureModel {
    private String photoURL, userID, pictureID;

    public PictureModel(){

    }

    public PictureModel(String PhotoURL, String userID,String PictureID) {
        this.photoURL = PhotoURL;
        this.userID = userID;
        this.pictureID =PictureID;
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
        return pictureID;
    }

    public void setPictureID(String PictureID) {
        this.pictureID = PictureID;
    }



}
