package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class NotePageActivity extends AppCompatActivity {

    RecyclerView RecyclerViewMainNote;
    Adapter adapter;
    List<Note> notes;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        RecyclerViewMainNote = findViewById(R.id.RecyclerViewMainNote);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNote);

        getSupportActionBar().setTitle("My Note");

        Database db = new Database(this);
        notes = db.getNotes();



        RecyclerViewMainNote.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notes );
        RecyclerViewMainNote.setAdapter(adapter);

        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) ->{

               switch (item.getItemId())
               {
                   case R.id.home:
                       startActivity(new Intent(getApplicationContext(), MainActivity.class));
                       overridePendingTransition(0,0);
                       return true;

                   case R.id.myQuiz:
                       startActivity(new Intent(getApplicationContext(), QuizCatalogueActivity.class));
                       overridePendingTransition(0,0);
                       return true;

                   case R.id.myNote:
                       return true;


                   case R.id.myNews:
                       startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                       overridePendingTransition(0,0);
                       return true;
               }

                return false;

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.add)
        {
            Intent addNote = new Intent(this, AddNoteActivity.class);
            Toast.makeText(this, "Add a note", Toast.LENGTH_SHORT).show();
            startActivity(addNote);
        }
        return super.onOptionsItemSelected(item);
    }



}