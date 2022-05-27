package com.example.doanquiz;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Quiz> questionArrayList);
}
