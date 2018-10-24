package edu.osu.cse5236.group10.packmap.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class Location extends BaseDocument {

    private GeoPoint coordinates;
    private String name;
    // Map with key of user id (phone number) and value is any comment
    private Map<String, String> upvotes;
    private Map<String, String> downvotes;

    public Location(GeoPoint coordinates, String name, Map<String, String> upvotes, Map<String, String> downvotes) {
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

    public Map<String, String> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Map<String, String> upvotes) {
        this.upvotes = upvotes;
    }

    public Map<String, String> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(Map<String, String> downvotes) {
        this.downvotes = downvotes;
    }
}
