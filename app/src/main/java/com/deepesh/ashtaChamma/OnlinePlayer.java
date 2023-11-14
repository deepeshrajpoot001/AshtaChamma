package com.deepesh.ashtaChamma;


import com.google.firebase.database.FirebaseDatabase;

public class OnlinePlayer {
    int id;
    String name;
    String photoURL;

        OnlinePlayer(){

    }
    OnlinePlayer(int id,String name,String photoURL){
            this.id = id;
        this.name = name;
        this.photoURL= photoURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }


}
