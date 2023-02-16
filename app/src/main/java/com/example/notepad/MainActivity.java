package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnNote, btnQuiz,btnNews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNote = findViewById(R.id.btnNote);
        btnQuiz = findViewById(R.id.btnQuiz);
        btnNews = findViewById(R.id.btnNews);


        btnNote.setOnClickListener((View v) ->{
        startActivity(new Intent(MainActivity.this, NotePage.class));


        });



    }
}