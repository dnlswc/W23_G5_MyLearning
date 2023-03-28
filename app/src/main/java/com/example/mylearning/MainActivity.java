package com.example.mylearning;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.mylearning.login.LoginActivity;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.notepad.NotePageActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button btnQuiz, btnNote, btnNews;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnQuiz = findViewById(R.id.btnQuiz);
        btnNote = findViewById(R.id.btnNote);
        btnNews = findViewById(R.id.btnNews);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);


        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) ->{

            switch (item.getItemId())
            {
                case R.id.home:
                    return true;

                case R.id.myQuiz:
                    startActivity(new Intent(getApplicationContext(), QuizCatalogueActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myNote:
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0,0);
                    return true;


                case R.id.myNews:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myAccount:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }

            return false;

        });


        btnQuiz.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, QuizCatalogueActivity.class));
        });

        btnNote.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, NotePageActivity.class));
        });

        btnNews.setOnClickListener((View v) -> {
            startActivity(new Intent(MainActivity.this, NewsActivity.class));
        });

    }
}