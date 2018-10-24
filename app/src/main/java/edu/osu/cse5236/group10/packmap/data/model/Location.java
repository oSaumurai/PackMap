package edu.osu.cse5236.group10.packmap.data.model;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Location extends BaseDocument {

    private GeoPoint coordinates;
    private String name;
    private List<Vote> upvotes;
    private List<Vote> downvotes;

    public Location(GeoPoint coordinates, String name, List<Vote> upvotes, List<Vote> downvotes) {
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

    public List<Vote> getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(List<Vote> upvotes) {
        this.upvotes = upvotes;
    }

    public List<Vote> getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(List<Vote> downvotes) {
        this.downvotes = downvotes;
    }
}
