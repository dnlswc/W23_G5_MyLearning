package com.example.mylearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mylearning.notepad.NotePage;
import com.example.mylearning.quiz.QuizCatalogueActivity;

public class MainActivity extends AppCompatActivity {

    Button btnQuiz, btnNote, btnNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQuiz = findViewById(R.id.btnQuiz);
        btnNote = findViewById(R.id.btnNote);
        btnNews = findViewById(R.id.btnNews);

        btnQuiz.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, QuizCatalogueActivity.class));
        });

        btnNote.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, NotePage.class));
        });
    }
}