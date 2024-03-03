package com.example.thesismaster_v10.Model;

public class ThesisModel {
    private int id;
    private String user;
    private String thesis;
    private String duedate;
    private int stars;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getThesis() {
        return thesis;
    }
    public void setThesis(String thesis) {
        this.thesis = thesis;
    }
    public String getDuedate() {
        return duedate;
    }
    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

}