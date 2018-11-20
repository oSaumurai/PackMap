package edu.osu.cse5236.group10.packmap.data.model;

import android.location.Location;
import android.util.Log;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationInfo extends BaseDocument {

    private GeoPoint coordinates;
    private String name;
    private List<String> upvotes;
    private List<String> downvotes;
    private String Uid;

    public LocationInfo (){
        this.coordinates = new GeoPoint(40,-82);
        this.name = "";
        this.Uid="";
        this.upvotes = new ArrayList<>();
        this.downvotes = new ArrayList<>();
    }
    public LocationInfo(GeoPoint coordinates,String name, List<String> upvotes, List<String> downvotes) {
        this.coordinates = coordinates;
        this.name = name;
        this.Uid="";
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
    public String getUid(){
        return Uid;
    }

    public void setUid(String Uid){
        this.Uid=Uid;
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

    public int getIntScore() {
        return getUpvotes().size()-getDownvotes().size();
    }

    public String getUpVote(){
        return Integer.toString(getUpvotes().size());
    }
    
    public String getDownVote(){
        return Integer.toString(getDownvotes().size());
    }

    public void setDownvotes(List<String> downvotes) {
        this.downvotes = downvotes;
    }
}
