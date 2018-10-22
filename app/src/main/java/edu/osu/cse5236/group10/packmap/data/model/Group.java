package edu.osu.cse5236.group10.packmap.data.model;

import java.util.List;

public class Group extends BaseDocument {

    private String name;
    private List<String> activityIdList;

    public Group() { }

    public Group(String name, List<String> activityIdList) {
        this.name = name;
        this.activityIdList = activityIdList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActivityIdList() {
        return activityIdList;
    }

    public void setActivityIdList(List<String> activityIdList) {
        this.activityIdList = activityIdList;
    }
}
