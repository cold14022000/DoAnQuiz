package com.example.doanquiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class addNewQuiz extends AppCompatActivity {

    private EditText quizName, quizURL;
    private Button saveQuiz;
    private String Url;
    private Integer numQuestion;
    private ProgressBar progressBar;

    ArrayList<Question> questionArrayList = new ArrayList<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Quiz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numQuestion=0;
        setContentView(R.layout.activity_add_new_quiz);

        RequestQueue queue = Volley.newRequestQueue(addNewQuiz.this);

        quizName = findViewById(R.id.quiz_name_AddNewQuiz);
        quizURL = findViewById(R.id.quiz_URL_AddNewQuiz);
        saveQuiz = findViewById(R.id.save_quiz_Btn_AddNewQuiz);
        progressBar=findViewById(R.id.progressBar);

        saveQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check=saveQuiz.getText().toString();
                switch (check){
                    case "Check Quiz":
                        Async async=new Async();
                        async.execute();
                        break;
                    case"Save Quiz":
                        saveQuiztoFirebase();
                        break;
                }

            }
        });
    }

    private void saveQuiztoFirebase() {
        String name = quizName.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        Url = quizURL.getText().toString();
        if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(Url)) {
            String itemId=String.valueOf(Timestamp.now().getSeconds());

            Quiz quiz=new Quiz();
            quiz.setPoint(0);
            quiz.setPosition(0);
            quiz.setNumQuestion(numQuestion);
            quiz.setName(name);
            quiz.setURL(Url);
            quiz.setQuizId(itemId);

            collectionReference.add(quiz).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Intent intent=new Intent(addNewQuiz.this
                            , MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(addNewQuiz.this, "Please enter all the information", Toast.LENGTH_SHORT).show();
            Log.d("Quiz", "dataName: "+name );
            Log.d("Quiz", "dataUrl: "+Url );
        }
    }

    public List<Question> getQuestions(String urlLink,final AnswerListAsyncResponse callBack) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlLink,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Question question = new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));


                                //Add question objects to lists
                                questionArrayList.add(question);

                                //Log.d("Hello ", "onResponse: "+question);
                                //Log.d(TAG, "onResponse: " + response.getJSONArray(i).get(0));
                                //Log.d(TAG, "onResponse: " + response.getJSONArray(i).getBoolean(1));
                            } catch (JSONException e) {
                                Log.d("error", "onResponse: exception");
                            }
                        }
                        if(null!=callBack) callBack.processFinished(questionArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        Log.d("Quiz", "getQuestions: "+questionArrayList);
        return questionArrayList;
    }

    private class Async extends AsyncTask<Void,Integer,ArrayList<Question>>{

        @Override
        protected ArrayList<Question> doInBackground(Void... voids) {
            Url = quizURL.getText().toString();
            getQuestions(Url,new AnswerListAsyncResponse() {
                @Override
                public void processFinished(ArrayList<Question> questionArrayList) {
                    numQuestion=questionArrayList.size();
                    if(numQuestion!=0){
                        saveQuiz.setText("Save Quiz");
                        Toast.makeText(addNewQuiz.this,"Url is valid"
                                ,Toast.LENGTH_SHORT).show();
                        quizURL.setTextColor(Color.parseColor("#41E838"));
                    }
                    Log.d("Quiz", "getQuestions: "+numQuestion);
                }
            });
            return questionArrayList;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Question> questions) {
            super.onPostExecute(questions);
            questionArrayList=questions;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}