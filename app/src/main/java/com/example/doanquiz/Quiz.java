package com.example.doanquiz;

public class Quiz {
    private Integer position;
    private String URL;
    private int numQuestion;
    private int point;

    public Quiz(Integer position, String URL, int numQuestion, int point) {
        this.position = position;
        this.URL = URL;
        this.numQuestion = numQuestion;
        this.point = point;
    }

    public Quiz() {
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getNumQuestion() {
        return numQuestion;
    }

    public void setNumQuestion(int numQuestion) {
        this.numQuestion = numQuestion;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
