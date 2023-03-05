package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.notepad.Database;
import com.example.mylearning.notepad.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteContent extends AppCompatActivity {
    TextView textViewNoteContent;
    FloatingActionButton floatingActionButtonDelete;
    Database database;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_content);

        textViewNoteContent = findViewById(R.id.textViewNoteContent);
        floatingActionButtonDelete = findViewById(R.id.floatingActionButtonDelete);

        ActionBar actionBar = getSupportActionBar();


        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID", 0);

        database = new Database(this);
        note = database.getNote(id);

        actionBar.setTitle(note.getTitle());
        textViewNoteContent.setText(note.getContent());
        textViewNoteContent.setMovementMethod(new ScrollingMovementMethod());

        floatingActionButtonDelete.setOnClickListener((View v) -> {
            Toast.makeText(this, "Deleted note", Toast.LENGTH_SHORT).show();
            database.deleteNote(note.getId());
            startActivity(new Intent(getApplicationContext(), NotePage.class));
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // menu object in edit_menu.xml
        if (item.getItemId() == R.id.edit) {
            Toast.makeText(this, "Edit the note", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, EditNote.class);
            intent.putExtra("ID",note.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}