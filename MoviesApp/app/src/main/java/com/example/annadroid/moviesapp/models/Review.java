package com.example.annadroid.moviesapp.models;

import java.io.Serializable;

public class Review implements Serializable {
    public static final long serialVersionUID = 1L;

    private String author;

    private String text;


    public Review(String author, String text) {
        this.author = author;
        this.text = text;
    }


    public String getAuthor() {
        return author;
    }


    public String getText() {
        return text;
    }
}
