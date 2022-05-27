package com.example.doanquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addNewQuiz extends AppCompatActivity {

    private EditText quizName, quizURL;
    private Button saveQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_quiz);

        quizName = findViewById(R.id.quiz_name_AddNewQuiz);
        quizURL = findViewById(R.id.quiz_URL_AddNewQuiz);
        saveQuiz = findViewById(R.id.save_quiz_Btn_AddNewQuiz);

        saveQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = quizName.getText().toString().trim();
                String Url = quizURL.getText().toString().trim();
                if (!TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(Url)) {

                } else {
                    Toast.makeText(addNewQuiz.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}