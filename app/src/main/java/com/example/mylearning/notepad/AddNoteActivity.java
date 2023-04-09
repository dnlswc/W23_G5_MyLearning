package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.login.LoginActivity;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextContent;
    Calendar calendar;
    String today;
    String time;
    BottomNavigationView bottomNavigationView;
    Database db;
    Note note;
    public static int addCounterForGuest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);


        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            int temAddCounterForGuest = sharedPreferences.getInt("ADD_COUNTER", -99);


            if (temAddCounterForGuest != -99) {
                addCounterForGuest = temAddCounterForGuest;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        getSupportActionBar().setTitle("Add a new note");

        editTextContent = findViewById(R.id.editTextContent);
        editTextTitle = findViewById(R.id.editTextTitle);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNote);

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

        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) -> {

            switch (item.getItemId()) {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myQuiz:
                    startActivity(new Intent(getApplicationContext(), QuizCatalogueActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myNote:
                    startActivity(new Intent(getApplicationContext(), NotePageActivity.class));
                    overridePendingTransition(0, 0);
                    return true;


                case R.id.myNews:
                    startActivity(new Intent(getApplicationContext(), NewsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;

                case R.id.myAccount:
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    overridePendingTransition(0, 0);
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
            Toast.makeText(this, "Cancel the note adding", Toast.LENGTH_SHORT).show();
            onBackPressed();
        } else if (item.getItemId() == R.id.save) {
            if (editTextTitle.getText().toString().isEmpty() == false) {
                calendar = Calendar.getInstance();
                today = calendar.get(Calendar.YEAR) + "/" + pad((calendar.get(Calendar.MONTH) + 1))
                        + "/" + pad(calendar.get(Calendar.DAY_OF_MONTH));
                time = pad(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + pad(calendar.get(Calendar.MINUTE)) +
                        ":" + pad(calendar.get(Calendar.SECOND));

                String tempAuthorEmail = NotePageActivity.author_email;

                if (tempAuthorEmail.equals("Empty") == true) {
                    tempAuthorEmail = "Guest3175@gmail.com";
                }

                note = new Note(editTextTitle.getText().toString(), editTextContent.getText().toString(), today, time, tempAuthorEmail);

                db = new Database(this);
                long idFromDb = db.addNote(note);
                note.setId(idFromDb);
                Toast.makeText(this, "Saved the note", Toast.LENGTH_SHORT).show();

                if (NotePageActivity.author_email.equals("Guest3175@gmail.com") == true ||
                        NotePageActivity.author_email.equals("Empty") == true) {
                    addCounterForGuest++;
                }

                directToNotePage();
            } else {
                Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void directToNotePage() {
        Intent intent = new Intent(this, NotePageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("ADD_COUNTER", addCounterForGuest);

        editor.commit();

    }


}