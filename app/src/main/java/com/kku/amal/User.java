package com.kku.amal;
public class User {

    public String username;
    public String email;
    String [] collections, favorites;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String [] collections, String[] favorites) {
        this.username = username;
        this.email = email;
        this.collections=collections;
        this.favorites=favorites;

    }

}
