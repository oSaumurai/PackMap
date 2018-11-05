package edu.osu.cse5236.group10.packmap.data.model;

import java.util.ArrayList;
import java.util.List;

public class Group extends BaseDocument {

    private String name;
    private List<String> activityList;
    private List<String> userList;

    public Group() {
        userList = new ArrayList<>();
        activityList = new ArrayList<>();
    }

    public Group(String name, List<String> activityList, List<String> userList) {
        this.name = name;
        this.activityList = activityList;
        this.userList = userList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
}
