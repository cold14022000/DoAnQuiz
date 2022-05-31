package com.example.doanquiz;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class quiz_game extends AppCompatActivity implements View.OnClickListener {

    private static final String SCORE_ID = "";

    private String url;
    private int point;
    private String title;
    private String quizId;

    private TextView questionTextview;
    private TextView counterTextview;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private int currentQuestionIndex = 0;
    private int myScore=0;
    private int highscore=0;
    private int storeQuestionIndex=99999;
    private List<Question> questionList;

    private TextView title_TV;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Quiz");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        questionTextview = findViewById(R.id.question);
        counterTextview = findViewById(R.id.counter);
        trueButton = findViewById(R.id.true_Button);
        falseButton = findViewById(R.id.false_Button);
        nextButton = findViewById(R.id.next_Button);
        prevButton = findViewById(R.id.previous_Button);
        title_TV=findViewById(R.id.title);

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            currentQuestionIndex=extras.getInt("quizPoint");
            url=extras.getString("quizUrl");
            title=extras.getString("quizTitle");
            quizId=extras.getString("quizId");
            title_TV.setText(title);
        }
        myScore=point;
        currentQuestionIndex++;
        questionList = new addNewQuiz().getQuestions(url,new AnswerListAsyncResponse() {
            @SuppressLint("SetTextI18n")
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                counterTextview.setText(currentQuestionIndex + " / " + questionArrayList.size());
                Log.d("Inside", "processFinished: " + questionArrayList);
            }
        });

        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);

        //Log.d("Main", "onCreate: "+ questionList);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous_Button:
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
                    updateQuestion();
                }
                break;
//            case R.id.next_Button:
//                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
//                updateQuestion();
//                break;
            case R.id.true_Button:
                checkAnswer(true);
                updateQuestion();
                break;
            case R.id.false_Button:
                checkAnswer(false);
                updateQuestion();
                break;
        }
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answerIsTrue=questionList.get(currentQuestionIndex).isAnswerTrue();
        int toastMessageID=0;
        if(userChooseCorrect==answerIsTrue)
        {
            if(storeQuestionIndex!=currentQuestionIndex) {
                myScore = myScore + 100;
                storeQuestionIndex=currentQuestionIndex;
            }
//            toastMessageID=R.string.correct_answer;
            fadeView();
            updateScoreFireBase();
        }else {
            if(storeQuestionIndex!=currentQuestionIndex) {
                if(myScore>0) {
                    myScore = myScore - 100;
                }
                storeQuestionIndex=currentQuestionIndex;
            }
//            toastMessageID=R.string.wrong_answer;
            shakeAnimtaion();
        }
//        Toast.makeText(quiz_game.this,toastMessageID,Toast.LENGTH_SHORT).show();
    }

    private void updateScoreFireBase() {
        collectionReference.whereEqualTo("quizId",quizId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot user : task.getResult()){
                        Map<String,Integer> updateScore=new HashMap<>();
                        updateScore.put("point",currentQuestionIndex);
                        collectionReference.document(user.getId()).set(updateScore, SetOptions.merge());
                    }
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        questionTextview.setText(question);
        counterTextview.setText(currentQuestionIndex + " / " + questionList.size());
    }



    private void shakeAnimtaion(){
        Animation shake= AnimationUtils.loadAnimation(quiz_game.this , R.anim.shake_animation);
        shake.setRepeatCount(1);
        shake.setDuration(250);
        shake.setRepeatMode(Animation.REVERSE);
        questionTextview.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                questionTextview.setTextColor(Color.parseColor("#FF4636"));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                questionTextview.setTextColor(Color.parseColor("#FFAF00"));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView(){
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        questionTextview.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                questionTextview.setTextColor(Color.parseColor("#1CFF81"));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                questionTextview.setTextColor(Color.parseColor("#FFAF00"));
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}