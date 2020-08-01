package com.kku.amal;

import java.util.ArrayList;
import java.util.List;

public class Collections {

     public String getCollection() {
        return collection;
    }


    private String collection;

    List<String> a;

    public void setCollection(String collection) {
        this.collection = collection;
    }


    // Constructor that is used to create an instance of the Movie object
    public Collections(  String sentence) {
        this.collection = sentence;

    }
    public Collections( String sentence, List<String> sentences) {
        this.collection = sentence;
      a = sentences;

    }
    public Object getItem(int position) {
        return a.get(position);
    }

}