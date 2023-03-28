package com.example.mylearning.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.login.LoginActivity;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.quiz.QuizCatalogueActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotePageActivity extends AppCompatActivity {

    RecyclerView RecyclerViewMainNote;
    Adapter adapter;
    List<Note> notes;

    BottomNavigationView bottomNavigationView;
    Database db;
    SearchView searchViewNote;
    static int sortingIndex = 0;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    FloatingActionButton floatingActionButtonSort;
    Button buttonConfirm;
    Button buttonCancel;
    RadioGroup radioGroupSortOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);


        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            int temSortingIndex = sharedPreferences.getInt("SORT_INDEX_PREFERENCE", -99);

            if (temSortingIndex != -99) {
                sortingIndex = temSortingIndex;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }


        try {
            int temSortingIndex = getIntent().getIntExtra("SORTING_INDEX", -99);

            if (temSortingIndex != -99) {
                sortingIndex = temSortingIndex;
            }


            //Toast.makeText(this, "Successful: "+ sortingIndex, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        RecyclerViewMainNote = findViewById(R.id.RecyclerViewMainNote);
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myNote);
        searchViewNote = findViewById(R.id.searchViewNote);
        floatingActionButtonSort = findViewById(R.id.floatingActionButtonSort);

        getSupportActionBar().setTitle("My Note");

        db = new Database(this);
        notes = db.getNotes();


        RecyclerViewMainNote.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notes);
        RecyclerViewMainNote.setAdapter(adapter);

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

        searchViewNote.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return true;
            }
        });


        floatingActionButtonSort.setOnClickListener((View v) -> {

            createSortPopUpList();
        });


    }

    private void filter(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for (Note item : notes) {
            // if (item.getTitle().toLowerCase().contains(newText.toLowerCase()))
            if (item.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                    item.getContent().toLowerCase().contains(newText.toLowerCase()) ||
                    item.getDate().toLowerCase().contains(newText.toLowerCase()) ||
                    item.getTime().toLowerCase().contains(newText.toLowerCase())
            ) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            Intent addNote = new Intent(this, AddNoteActivity.class);
            Toast.makeText(this, "Add a note", Toast.LENGTH_SHORT).show();
            startActivity(addNote);
        }
        return super.onOptionsItemSelected(item);
    }


    public void createSortPopUpList() {
        dialogBuilder = new AlertDialog.Builder(this);

        View sortPopUpView = getLayoutInflater().inflate(R.layout.pop_up_list, null);

        buttonConfirm = (Button) sortPopUpView.findViewById(R.id.buttonConfirm);
        buttonCancel = (Button) sortPopUpView.findViewById(R.id.buttonCancel);
        radioGroupSortOption = (RadioGroup) sortPopUpView.findViewById(R.id.radioGroupSortOption);


        if (sortingIndex == 0) {
            radioGroupSortOption.check((R.id.radioButtonModifiedTimeNewestToOldest));

        } else if (sortingIndex == 1) {
            radioGroupSortOption.check((R.id.radioButtonModifiedTimeoOldestToNewes));

        } else if (sortingIndex == 2) {
            radioGroupSortOption.check((R.id.radioButtonAtoZ));

        } else if (sortingIndex == 3) {
            radioGroupSortOption.check((R.id.radioButtonZtoA));

        }

        //radioGroupSortOption.check((R.id.radioButtonModifiedTimeoOldestToNewes));

        dialogBuilder.setView(sortPopUpView);
        dialog = dialogBuilder.create();

        buttonConfirm.setOnClickListener((View v) -> {

            // radioGroupSortOption.check(R.id.radioButtonModifiedTimeoOldestToNewes);

            if (radioGroupSortOption.getCheckedRadioButtonId() == R.id.radioButtonModifiedTimeNewestToOldest) {
                sortingIndex = 0;
            } else if (radioGroupSortOption.getCheckedRadioButtonId() == R.id.radioButtonModifiedTimeoOldestToNewes) {
                sortingIndex = 1;
            } else if (radioGroupSortOption.getCheckedRadioButtonId() == R.id.radioButtonAtoZ) {
                sortingIndex = 2;
            } else if (radioGroupSortOption.getCheckedRadioButtonId() == R.id.radioButtonZtoA) {
                sortingIndex = 3;
            }
            //  Toast.makeText(this, "sortingIndex: " + sortingIndex, Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(this, NotePageActivity.class);


            intent.putExtra("SORTING_INDEX", sortingIndex);


/*
             Bundle bundle = new Bundle();
             bundle.putInt("SORTING_INDEX",sortingIndex);
             intent.putExtras(bundle);
*/

            startActivity(intent);

            //   startActivity(new Intent(this, NotePageActivity.class));

        });

        buttonCancel.setOnClickListener((View v) -> {
            dialog.dismiss();

        });


        dialog.show();


    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("SORT_INDEX_PREFERENCE", sortingIndex);
        editor.commit();

    }
}