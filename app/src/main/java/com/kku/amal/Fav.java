package com.kku.amal;

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

    // Store the name of the movie
    private String sentence;
    // Store the release date of the movie
    private String collection;

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
    public Fav( String sentence) {
        this.sentence = sentence;

    }


}