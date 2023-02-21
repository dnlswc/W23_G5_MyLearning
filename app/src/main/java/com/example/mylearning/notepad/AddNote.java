package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
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

import com.example.mylearning.R;

import java.util.Calendar;

public class AddNote extends AppCompatActivity {

    EditText editTextTitle, editTextContent;
    Calendar calendar;
    String today;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextContent = findViewById(R.id.editTextContent);
        editTextTitle = findViewById(R.id.editTextTitle);
        getSupportActionBar().setTitle("Add new note");
        //  getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0) {
                    getSupportActionBar().setTitle("Note Title: " + s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        calendar = Calendar.getInstance();
        today = calendar.get(Calendar.YEAR) + "/"+ (calendar.get(Calendar.MONTH)+1)
                + "/"+ calendar.get(Calendar.DAY_OF_MONTH);

        time = pad(calendar.get(Calendar.HOUR)) + ":" + pad(calendar.get(Calendar.MINUTE));

        Toast.makeText(this, "Date and Time: " + today + " and " + time, Toast.LENGTH_SHORT).show();

        //  Log.d("Calender", "Date and Time: " + today + "and " + time );
    }

    private String pad(int i) {
        if (i<10) {
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
        if (item.getItemId()==R.id.delete) {
            Toast.makeText(this, "Cancel the note.", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        if (item.getItemId()==R.id.save) {
            Note note = new Note(editTextTitle.getText().toString(), editTextContent.getText().toString(),today,time );
            Database db = new Database(this);
            db.addNote(note);

            Toast.makeText(this, "Save the note.", Toast.LENGTH_SHORT).show();
            //  onBackPressed();
            goToMain();
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent intent = new Intent(this, NotePage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}