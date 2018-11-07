package edu.osu.cse5236.group10.packmap.data.model;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseDocument {

    private String phone;
    private String lastName;
    private String firstName;
    private String password;
    private List<String> groups;

    public User() {}

    public User(String phone) {
        this.phone = phone;
    }

    public User(String phone, String lastName, String firstName, String password) {
        this.phone = phone;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
        this.groups = new ArrayList<>();
    }

    public String getPhone() {
        return phone;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getGroups() { return groups; }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLastName(String lname) {
        this.lastName = lname;
    }

    public void setFirstName(String fname) {
        this.firstName = fname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGroups(List<String> groups) { this.groups = groups; }
    /*
     * Added the methods below to make this work better with AbstractStore methods better
     */
    @Override
    @Exclude
    public String getDocumentId() {
        return getPhone();
    }

    @Override
    public void setDocumentId(String documentId) {
        setPhone(documentId);
    }
}
