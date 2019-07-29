package com.example.pictureuploader;

public class ImageObject {
    String id;
    String url;
    String imageurl;
    String PhotographerName;


    public ImageObject(String id, String url, String imageurl, String photographerName){
        this.id = id;
        this.url = url;
        this.imageurl = imageurl;
        this.PhotographerName = photographerName;

    }

//    @androidx.annotation.NonNull
    @Override
    public String toString() {
        return "\n\nid: " + id + "\nurl: " + url + "\nimage url: " + imageurl + "\nPhotographer Name: " + PhotographerName;

    }
}
