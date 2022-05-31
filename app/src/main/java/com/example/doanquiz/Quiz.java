package com.example.doanquiz;

public class Quiz {
    private String name;
    private String URL;
    private int numQuestion;
    private int point;
    private String quizId;

    public Quiz(String name, String URL, int numQuestion, int point, String quizId) {
        this.name = name;
        this.URL = URL;
        this.numQuestion = numQuestion;
        this.point = point;
        this.quizId = quizId;
    }

    public Quiz() {
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
        this.quizId = quizId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
