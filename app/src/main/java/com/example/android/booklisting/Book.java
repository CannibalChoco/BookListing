package com.example.android.booklisting;


public class Book {
    private String author;
    private String title;
    private String description;
    private String url;

    public Book (String author, String title, String description){
        this.author = author;
        this.title = title;
        this.description = description;
    }

    public Book (String author, String title, String description, String url){
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() { return author; }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }
}
