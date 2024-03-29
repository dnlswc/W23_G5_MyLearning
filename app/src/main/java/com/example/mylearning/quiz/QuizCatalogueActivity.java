package com.example.mylearning.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.login.LoginActivity;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.notepad.NotePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class QuizCatalogueActivity extends AppCompatActivity {
    static final String TAG = "QuizData";

    public static final String EXTRA_TYPE = "extraType";
    public static final String EXTRA_TOPIC = "extraTopic";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    private Spinner spinnerType;
    private Spinner spinnerTopic;
    private Spinner spinnerDifficulty;
    private Button btnStartQuiz;

    private List<Question> questions;
    private QuizDbHelper quizDbHelper;

    private SharedPreferences sharedPrefQuiz;
    private SharedPreferences.Editor editor;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_catalogue);

        getSupportActionBar().setTitle("My Quiz");

        spinnerType = findViewById(R.id.spinnerType);
        spinnerTopic = findViewById(R.id.spinnerTopic);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.myQuiz);

        // use shared preferences to determine if it is the first run of the quiz feature
        sharedPrefQuiz = getSharedPreferences("Quiz", Context.MODE_PRIVATE);
        boolean isFirstRun = sharedPrefQuiz.getBoolean("is_first_run", true);
        if (isFirstRun) {
            // if it is the first run, import the quiz data from CSV file to database
            importCsvDataIntoDb();
        }

        loadTypes();
        loadTopics();
        loadDifficultyLevels();

        btnStartQuiz.setOnClickListener((View v) -> {
            String type = spinnerType.getSelectedItem().toString();
            String topic = spinnerTopic.getSelectedItem().toString();
            String difficulty = spinnerDifficulty.getSelectedItem().toString();

            if (type.equalsIgnoreCase("True or False")) {
                type = "TF";
            } else if (type.equalsIgnoreCase("Multiple Choice")) {
                type = "MC";
            } else if (type.equalsIgnoreCase("Fill in the Blank")) {
                type = "FITB";
            }

            Intent quizIntent = new Intent(QuizCatalogueActivity.this, QuizActivity.class);
            quizIntent.putExtra(EXTRA_TYPE, type);
            quizIntent.putExtra(EXTRA_TOPIC, topic);
            quizIntent.putExtra(EXTRA_DIFFICULTY, difficulty);
            startActivity(quizIntent);
        });

        bottomNavigationView.setOnItemSelectedListener((@NonNull MenuItem item) ->{

            switch (item.getItemId())
            {
                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;

                case R.id.myQuiz:
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
    }

    private void readQuizData() {
        InputStream inputStream = getResources().openRawResource(R.raw.quiz_data);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            if ((line = reader.readLine()) != null) {
                // process header
            }

            while ((line = reader.readLine()) != null) {
                // this regex split the line on commas only if the commas are not within a field enclosed in double quotes
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                for (int i = 0; i < tokens.length; i++) {
                    // if a field is enclosed in double quotes
                    if (tokens[i].startsWith("\"") && tokens[i].endsWith("\"")) {
                        // remove enclosing double quotes
                        tokens[i] = tokens[i].substring(1, tokens[i].length() - 1);
                        // replace two consecutive double quotes with one double quote
                        tokens[i] = tokens[i].replaceAll("\"\"", "\"");
                    }
                }

                Question question = new Question();
                question.setType(tokens[0]);
                question.setDifficulty(tokens[1]);
                question.setTopic(tokens[2]);
                question.setQuestion(tokens[3].replace("***", "\n"));
                question.setAnswer(tokens[4]);
                if (tokens[5].length() > 0) {
                    question.setOption1(tokens[5]);
                } else {
                    question.setOption1("");
                }
                if (tokens[6].length() > 0) {
                    question.setOption2(tokens[6]);
                } else {
                    question.setOption2("");
                }
                if (tokens[7].length() > 0) {
                    question.setOption3(tokens[7]);
                } else {
                    question.setOption3("");
                }
                if (tokens[8].length() > 0) {
                    question.setOption4(tokens[8]);
                } else {
                    question.setOption4("");
                }
                questions.add(question);
            }
        } catch (Exception ex) {
            Log.d(TAG, "Error in reading data file on line " + line);
            ex.printStackTrace();
        }
    }

    private void importCsvDataIntoDb() {
        quizDbHelper = QuizDbHelper.getInstance(this);
        questions = new ArrayList<>();
        readQuizData();
        for (Question question : questions) {
            quizDbHelper.addQuestion(question);
        }

        // set the boolean to false for "is_first_run" in Quiz's shared preferences
        setIsFirstRunToFalse();
    }

    private void setIsFirstRunToFalse() {
        editor = sharedPrefQuiz.edit();
        editor.putBoolean("is_first_run", false);
        editor.commit();
    }

    private void loadTypes() {
        String[] types = new String[Question.getAllTypes().length + 1];
        for (int i = 0; i <= Question.getAllTypes().length; i++) {
            if (i == 0) {
                types[i] = "All";
            } else {
                types[i] = Question.getAllTypes()[i - 1];
            }
        }

        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
    }

    // load distinct topics from the database
    private void loadTopics() {
        QuizDbHelper quizDbHelper = new QuizDbHelper(this);
        String[] topics = new String[quizDbHelper.getTopics().size() + 1];
        for (int i = 0; i <= quizDbHelper.getTopics().size(); i++) {
            if (i == 0) {
                topics[i] = "All";
            } else {
                topics[i] = quizDbHelper.getTopics().get(i - 1);
            }
        }

        ArrayAdapter<String> adapterTopic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        adapterTopic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(adapterTopic);
    }

    private void loadDifficultyLevels() {
        String[] difficultyLevels = new String[Question.getAllDifficultyLevels().length + 1];
        for (int i = 0; i <= Question.getAllDifficultyLevels().length; i++) {
            if (i == 0) {
                difficultyLevels[i] = "All";
            } else {
                difficultyLevels[i] = Question.getAllDifficultyLevels()[i - 1];
            }
        }

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }
}