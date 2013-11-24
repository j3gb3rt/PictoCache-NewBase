package edu.gatech.cs4261.wheresdabeef.domain;

import android.location.Location;
import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 11/2/13.
 */
public class Image implements Serializable{
    private final int id;

    private double latitude;
    private double longitude;
    private List<Keyword> keywords;

    private Uri thumbnail;
    private Uri image;

    public Image(final int id) {
        this.id = id;
    }

    public Image(Uri imageLocation, Location location) {
        this(-1);
        setImage(imageLocation);
        setThumbnail(imageLocation);
        setLatitude(location.getLatitude());
        setLongitude(location.getLongitude());
    }
    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Keyword> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<Keyword>();
        }
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public Uri getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Uri thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

}
