package com.example.android.booklisting;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Book {
    private String author;
    private String title;
    private String description;

    public Book (String author, String title){
        this.author = author;
        this.title = title;
    }

    public Book (String author, String title, String description){
        this.author = author;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() { return author; }

    public String getDescription() {
        return description;
    }
}
