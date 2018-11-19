package edu.osu.cse5236.group10.packmap.data.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfo extends BaseDocument {

    private String name;
    private String info;
    private boolean isActive;
    private List<String> selectedLocations;
    private String uid;

    public ActivityInfo() {
        name="";
        info="";
        isActive=false;
        selectedLocations=new ArrayList<>();
    }

    public ActivityInfo(String name, String info, boolean isActive, List<String> selectedLocations) {
        this.name = name;
        this.info = info;
        this.isActive = isActive;
        this.selectedLocations = selectedLocations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<String> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<String> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    @Exclude
    public String getDocumentId() {
        return this.uid;
    }

    @Override
    public void setDocumentId(String documentId) {
        setName(documentId);
    }
}
