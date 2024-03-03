package com.example.thesismaster_v10.Model;

public class QuestionModel {
    private int id, status;
    private String question;
    private String thesis;
    private String theme;
    private String qnote;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getThesis() {
        return thesis;
    }

    public void setThesis(String thesis) {
        this.thesis = thesis;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getQnote() {
        return qnote;
    }

    public void setQnote(String qnote) {
        this.qnote = qnote;
    }
}
