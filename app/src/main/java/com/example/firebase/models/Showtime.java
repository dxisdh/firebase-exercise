package com.example.firebase.models;

import java.io.Serializable;
import java.util.Date;

public class Showtime implements Serializable {
    private String id;
    private String movieId;
    private String theaterId;
    private Date startTime;
    private double price;

    public Showtime() {}

    public Showtime(String id, String movieId, String theaterId, Date startTime, double price) {
        this.id = id;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.price = price;
    }

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getTheaterId() { return theaterId; }
    public Date getStartTime() { return startTime; }
    public double getPrice() { return price; }
}