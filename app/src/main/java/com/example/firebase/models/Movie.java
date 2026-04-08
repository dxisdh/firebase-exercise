package com.example.firebase.models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String genre;
    private int duration;

    public Movie() {}

    public Movie(String id, String title, String description, String imageUrl, String genre, int duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.genre = genre;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
}