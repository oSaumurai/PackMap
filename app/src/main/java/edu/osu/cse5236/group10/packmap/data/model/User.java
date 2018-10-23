package edu.osu.cse5236.group10.packmap.data.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String phone;
    private String lastName;
    private String firstName;
    private String password;

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

    public User(String phone, String lastName, String firstName, String password) {
        this.phone = phone;
        this.lastName = lastName;
        this.firstName = firstName;
        this.password = password;
    }

//    public Map<String, Object> getUser() {
//        Map<String, Object> m = new HashMap<>();
//        m.put("phone", phone);
//        m.put("lname", lastName);
//        m.put("fname", firstName);
//        m.put("password", password);
//        return m;
//    }
}
