package edu.osu.cse5236.group10.packmap.data.model;

public class User {
    private String phone;
    private String lastName;
    private String firstName;
    private String password;

    public String getPhone() {
        return phone;
    }

    public String getLastName() {
        return lname;
    }

    public String getFirstName() {
        return fname;
    }

    public String getPassword() {
        return password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLastName(String lname) {
        this.lname = lname;
    }

    public void setFirstName(String fname) {
        this.fname = fname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String phone, String lastName, String firstName, String password) {
        this.phone = phone;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
    }
}
