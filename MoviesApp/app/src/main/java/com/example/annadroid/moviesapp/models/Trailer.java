package com.example.annadroid.moviesapp.models;


public class Trailer {
    private String name;

    private String link;


    public Trailer(String name, String link) {
        this.name = name;
        this.link = "http://www.youtube.com/watch?v=" + link;
    }


    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
