package edu.osu.cse5236.group10.packmap.data.model;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationInfo extends BaseDocument {

    private GeoPoint coordinates;
    private String name;
    // Map with key of user id (phone number) and value is any comment
    private List<String> upvotes;
    private List<String> downvotes;
    private int score;

    public LocationInfo (){
        this.coordinates = new GeoPoint(40,-82);
        this.name = "";
        this.upvotes = new ArrayList<>();
        this.downvotes = new ArrayList<>();
        score = 0;
    }
    public LocationInfo(GeoPoint coordinates, String name, List<String> upvotes, List<String> downvotes) {
        this.coordinates = coordinates;
        this.name = name;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<String> upvotes) {
        this.upvotes = upvotes;
    }

    public List<String> getDownvotes() {
        return downvotes;
    }

    public void updateScore() {
        score = upvotes.size() - downvotes.size();
    }

    public int getIntScore() {
        return score;
    }

    public String getScore() {
        return "" + score;
    }

    public void setDownvotes(List<String> downvotes) {
        this.downvotes = downvotes;
    }
}
