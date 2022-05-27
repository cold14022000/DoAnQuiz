package com.example.doanquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class addNewQuiz extends AppCompatActivity {

    private EditText quizName, quizURL;
    private Button saveQuiz;
    private String Url;
    private Integer numQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_quiz);

        quizName = findViewById(R.id.quiz_name_AddNewQuiz);
        quizURL = findViewById(R.id.quiz_URL_AddNewQuiz);
        saveQuiz = findViewById(R.id.save_quiz_Btn_AddNewQuiz);

        String name = quizName.getText().toString().trim();
        Url = quizURL.getText().toString().trim();

        saveQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(Url)) {
                        Quiz quiz=new Quiz();
                        quiz.setPoint(0);
                        quiz.setNumQuestion(getNumQuestions());
                        quiz.setPosition(0);
                        quiz.setName(name);
                        quiz.setURL(Url);
                } else {
                    Toast.makeText(addNewQuiz.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public int getNumQuestions(){
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET, Url, null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++){
                    numQuestion=i;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        return numQuestion;
    }
}