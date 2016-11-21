package com.example.app;

/**
 * Created by ranjiang on 2016/11/20.
 */
import android.graphics.*;
public class Restaurant {
    private String name;
    private String address;
    private String type;
    private double lat;
    private double lng;
    private Bitmap thumbnail;
    private Bitmap rating;
    public Restaurant(String name, String address, String type, double lat, double lng, Bitmap thumbnail, Bitmap rating) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.thumbnail = thumbnail;
        this.rating = rating;

    }
    public String getName() { return this.name;}
    public String getAddress() { return this.address;}
    public String getType() { return this.type;}
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public Bitmap getThumbnail() { return thumbnail; }
    public Bitmap getRating() { return rating; }

}
