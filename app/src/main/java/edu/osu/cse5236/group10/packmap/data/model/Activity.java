package edu.osu.cse5236.group10.packmap.data.model;

import java.util.List;

public class Activity extends BaseDocument {

    private String name;
    private String info;
    private boolean isActive;
    private List<String> selectedLocations;

    public Activity() {}

    public Activity(String name, String info, boolean isActive, List<String> selectedLocations) {
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
}
