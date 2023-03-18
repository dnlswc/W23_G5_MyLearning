package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class EditNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextContent;
    Calendar calendar;
    String today;
    String time;
    Database database;
    Note note;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID", -1);
        database = new Database(this);
        note = database.getNote(id);

        editTextContent = findViewById(R.id.editTextContent);
        editTextTitle = findViewById(R.id.editTextTitle);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNote);

        ActionBar actionBar = getSupportActionBar();
        //   actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(note.getTitle());
        //  actionBar.setDisplayHomeAsUpEnabled(true);

        /*
        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/


        editTextTitle.setText(note.getTitle());
        editTextContent.setText(note.getContent());


        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    getSupportActionBar().setTitle("Note Title: " + s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*
        calendar = Calendar.getInstance();
        today = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1)
                + "/" + calendar.get(Calendar.DAY_OF_MONTH);

        time = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));

        Toast.makeText(this, "Date and Time: " + today + " and " + time, Toast.LENGTH_SHORT).show();
*/


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
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0,0);
                    return true;


                case R.id.myNews:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }

            return false;

        });

    }

    private String pad(int i) {
        if (i < 10) {
            return "0" + i;
        }

        return String.valueOf(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Cancel the note.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else if (item.getItemId() == R.id.save) {
            // new line
            if (editTextTitle.getText().toString().isEmpty() == false) {
                note.setTitle(editTextTitle.getText().toString());
                note.setContent(editTextContent.getText().toString());

                calendar = Calendar.getInstance();
                today = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1)
                        + "/" + calendar.get(Calendar.DAY_OF_MONTH);

                time = pad(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad(calendar.get(Calendar.MINUTE));

                note.setDate(today);
                note.setTime(time);

                int numberOfRowAffected = database.editNote(note);
                //  int id = database.editNote(note);
                //  if (id==note.getId()){
                if (numberOfRowAffected == 1) {
                    Toast.makeText(this, "Note was updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unsuccessful update", Toast.LENGTH_SHORT).show();
                }

                //  Toast.makeText(this, "Updated the note.", Toast.LENGTH_SHORT).show();
                // goToMain();
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                return super.onOptionsItemSelected(item);
            }


            Intent intent = new Intent(getApplicationContext(), NotePageActivity.class);
            /*intent.putExtra("ID", note.getId());
            intent.putExtra("DATE", note.getDate());
            intent.putExtra("TIME", note.getTime());*/
            startActivity(intent);

/*
            Toast.makeText(this, "Save the note.", Toast.LENGTH_SHORT).show();
            //  onBackPressed();
            goToMain();*/
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void goToMain() {
        Intent intent = new Intent(this, NotePage.class);
        startActivity(intent);
    }
*/

}