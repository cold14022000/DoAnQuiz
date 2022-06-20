package com.example.doanquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity", "static initializer: OpenCV is loaded");
        }else{
            Log.d("MainActivity", "static initializer: OpenCV is not loaded");
        }
    }

    private TextView tv_NoItemInApp;
    private ImageView add_item_noItemInApp,add_item_corner;

    private RecyclerView recyclerView;
    private List<Quiz> quizList;
    private quizRecyclerAdapter quizRecyclerAdapter;
    private quizRecyclerAdapter.RecyclerViewClickListener listener;

    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=db.collection("Quiz");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_NoItemInApp = findViewById(R.id.no_item_in_app_tv);
        add_item_noItemInApp = findViewById(R.id.add_button_noItem);
        add_item_corner=findViewById(R.id.corner_add_button);

        quizList=new ArrayList<>();

        recyclerView=findViewById(R.id.quizMenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        getQuizItem();

        add_item_noItemInApp.setOnClickListener(this);
        add_item_corner.setOnClickListener(this);
    }

    private void getQuizItem() {
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot quizs : queryDocumentSnapshots){
                                Quiz quiz=quizs.toObject(Quiz.class);
                                quizList.add(quiz);
                            }
                            setOnClickListener();
                            quizRecyclerAdapter=new quizRecyclerAdapter(MainActivity.this
                            ,quizList,listener);
                            recyclerView.setAdapter(quizRecyclerAdapter);
                            quizRecyclerAdapter.notifyDataSetChanged();
                            add_item_corner.setVisibility(View.VISIBLE);

                        }else {
                            tv_NoItemInApp.setVisibility(View.VISIBLE);
                            add_item_noItemInApp.setVisibility(View.VISIBLE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, addNewQuiz.class);
        switch (view.getId()) {
            case R.id.add_button_noItem:
            case R.id.corner_add_button:
                startActivity(intent);
                break;
        }
    }

    private void setOnClickListener() {
        listener=new quizRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(MainActivity.this, quiz_game.class);
                intent.putExtra("quizTitle", quizList.get(position).getName());
                intent.putExtra("quizUrl", quizList.get(position).getURL());
                intent.putExtra("quizPoint", quizList.get(position).getPoint());
                intent.putExtra("quizId", quizList.get(position).getQuizId());
                intent.putExtra("quizPosition",quizList.get(position).getPosition());
                startActivity(intent);
            }
        };
    }
}