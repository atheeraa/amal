package com.kku.amal;

import java.util.ArrayList;

public class Fav {


    public String getSentence() {
        return sentence;
    }

    public String getCollection() {
        return collection;
    }

    public boolean isLike() {
        return like;
    }

    private String sentence;
    private String collection;
    ArrayList<Fav> a;

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    private boolean like;

    // Constructor that is used to create an instance of the Movie object
    public Fav(  String sentence) {
        this.sentence = sentence;

    }
    public Object getItem(int position) {
        return a.get(position);
    }

}