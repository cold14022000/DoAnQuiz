package com.example.doanquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_NoItemInApp;
    private ImageView add_item_noItemInApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_NoItemInApp = findViewById(R.id.no_item_in_app_tv);
        add_item_noItemInApp = findViewById(R.id.add_button_noItem);

        add_item_noItemInApp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button_noItem:
                Intent intent = new Intent(MainActivity.this, addNewQuiz.class);
                startActivity(intent);
                break;
        }
    }
}