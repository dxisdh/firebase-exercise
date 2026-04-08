package com.example.firebase.models;

import java.util.Date;

public class Ticket {
    private String id;
    private String userId;
    private String showtimeId;
    private String movieTitle;
    private String theaterName;
    private Date showtimeTime;
    private String seatNumber;
    private double price;

    public Ticket() {}

    public Ticket(String id, String userId, String showtimeId, String movieTitle, String theaterName, Date showtimeTime, String seatNumber, double price) {
        this.id = id;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.showtimeTime = showtimeTime;
        this.seatNumber = seatNumber;
        this.price = price;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getShowtimeId() { return showtimeId; }
    public String getMovieTitle() { return movieTitle; }
    public String getTheaterName() { return theaterName; }
    public Date getShowtimeTime() { return showtimeTime; }
    public String getSeatNumber() { return seatNumber; }
    public double getPrice() { return price; }
}