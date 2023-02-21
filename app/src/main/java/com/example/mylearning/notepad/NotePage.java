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

import com.example.mylearning.R;

import java.util.List;

public class NotePage extends AppCompatActivity {

    RecyclerView RecyclerViewMainNote;
    Adapter adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        Database db = new Database(this);
        notes = db.getNotes();
        RecyclerViewMainNote = findViewById(R.id.RecyclerViewMainNote);
        RecyclerViewMainNote.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notes );
        RecyclerViewMainNote.setAdapter(adapter);
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

            Intent addNote = new Intent(this, AddNote.class);
            Toast.makeText(this, "Add a note.", Toast.LENGTH_SHORT).show();
            startActivity(addNote);
        }
        return super.onOptionsItemSelected(item);
    }

}