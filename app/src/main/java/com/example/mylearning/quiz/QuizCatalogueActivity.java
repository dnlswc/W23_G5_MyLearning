package com.example.mylearning.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mylearning.R;

import java.util.List;

public class QuizCatalogueActivity extends AppCompatActivity {

    public static final String EXTRA_TOPIC_ID = "extraTopicId";
    public static final String EXTRA_TOPIC_NAME = "extraTopicName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    private Spinner spinnerTopic;
    private Spinner spinnerDifficulty;
    private Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_catalogue);

        spinnerTopic = findViewById(R.id.spinnerTopic);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);

        loadTopics();
        loadDifficultyLevels();

        btnStartQuiz.setOnClickListener((View v) -> {
            Topic selectedTopic = (Topic) spinnerTopic.getSelectedItem();
            int topicId = selectedTopic.getId();
            String topicName = selectedTopic.getName();
            String difficulty = spinnerDifficulty.getSelectedItem().toString();

            Intent quizIntent = new Intent(QuizCatalogueActivity.this, QuizActivity.class);
            quizIntent.putExtra(EXTRA_TOPIC_ID, topicId);
            quizIntent.putExtra(EXTRA_TOPIC_NAME, topicName);
            quizIntent.putExtra(EXTRA_DIFFICULTY, difficulty);
            startActivity(quizIntent);
        });
    }

    private void loadTopics() {
        QuizDbHelper dbHelper = QuizDbHelper.getInstance(this);
        List<Topic> topics = dbHelper.getAllTopics();

        ArrayAdapter<Topic> adapterTopic = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, topics);
        adapterTopic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTopic.setAdapter(adapterTopic);
    }

    private void loadDifficultyLevels() {
        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

}