package com.example.mylearning.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylearning.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class McqActivity extends AppCompatActivity {
    private static final long TIMER_IN_MS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNTER = "keyQuestionCounter";
    private static final String KEY_TIME_LEFT = "keyTimeLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private static final String KEY_CORRECT_ANSWER = "keyCorrectAnswer";

    private TextView txtViewScore;
    private TextView txtViewQuestionCount;
    private TextView txtViewTimer;
    private TextView txtViewTopicDifficulty;
    private TextView txtViewQuestion;
    private RadioGroup rdGrpMcOptions;
    private RadioButton rdBtnOption1;
    private RadioButton rdBtnOption2;
    private RadioButton rdBtnOption3;
    private RadioButton rdBtnOption4;
    private TextView txtViewCorrectAnswer;
    private Button btnConfirmNext;

    private ArrayList<Question> questionList;
    private ArrayList<Question> mcQuestionList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private String correctAnswer;

    private int score;
    private boolean answered;
    private int answerNo;

    private ColorStateList txtColourDefaultRdBtn;
    private ColorStateList txtColourDefaultTimer;

    private CountDownTimer timer;
    private long timeLeftInMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq);

        txtViewScore = findViewById(R.id.txtViewScore);
        txtViewQuestionCount = findViewById(R.id.txtViewQuestionCount);
        txtViewTimer = findViewById(R.id.txtViewTimer);
        txtViewTopicDifficulty = findViewById(R.id.txtViewTopicDifficulty);
        txtViewQuestion = findViewById(R.id.txtViewQuestion);
        rdGrpMcOptions = findViewById(R.id.rdGrpMcOptions);
        rdBtnOption1 = findViewById(R.id.rdBtnOption1);
        rdBtnOption2 = findViewById(R.id.rdBtnOption2);
        rdBtnOption3 = findViewById(R.id.rdBtnOption3);
        rdBtnOption4 = findViewById(R.id.rdBtnOption4);
        txtViewCorrectAnswer = findViewById(R.id.txtViewCorrectAnswer);
        btnConfirmNext = findViewById(R.id.btnConfirmNext);

        txtColourDefaultRdBtn = rdBtnOption1.getTextColors();
        txtColourDefaultTimer = txtViewTimer.getTextColors();

        Intent mcqIntent = getIntent();
        int topicId = mcqIntent.getIntExtra(QuizCatalogueActivity.EXTRA_TOPIC_ID, 0);
        String topicName = mcqIntent.getStringExtra(QuizCatalogueActivity.EXTRA_TOPIC_NAME);
        String difficulty = mcqIntent.getStringExtra(QuizCatalogueActivity.EXTRA_DIFFICULTY);

        txtViewTopicDifficulty.setText(topicName + " (" + difficulty + ")");

        if (savedInstanceState == null) {
            QuizDbHelper quizDbHelper = QuizDbHelper.getInstance(this);
            questionList = quizDbHelper.getQuestions(topicId, difficulty);
            getMcQuestions();

            questionCountTotal = mcQuestionList.size();
            Collections.shuffle(mcQuestionList);

            showNextQuestion();
        } else {
            mcQuestionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if (mcQuestionList == null) {
                finish();
            }
            score = savedInstanceState.getInt(KEY_SCORE);
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNTER);
            timeLeftInMs = savedInstanceState.getLong(KEY_TIME_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            correctAnswer = savedInstanceState.getString(KEY_CORRECT_ANSWER);
            questionCountTotal = mcQuestionList.size();
            currentQuestion = mcQuestionList.get(questionCounter - 1);

            if (!answered) {
                startTimer();
            } else {
                updateTimerText();
                showCorrectAnswer(answerNo);
            }
        }

        btnConfirmNext.setOnClickListener((View v) -> {
            if (!answered) {
                if (rdBtnOption1.isChecked() || rdBtnOption2.isChecked() ||
                        rdBtnOption3.isChecked() || rdBtnOption4.isChecked()) {
                    checkAnswer();
                } else {
                    Toast.makeText(McqActivity.this, "Please select an option.", Toast.LENGTH_SHORT).show();
                }
            } else {
                showNextQuestion();
            }
        });
    }

    private void getMcQuestions() {
        mcQuestionList = new ArrayList<>();

        for (Question question : questionList) {
            if (question.getCategory().equalsIgnoreCase("MC")) {
                mcQuestionList.add(question);
            }
        }
    }

    private void showNextQuestion() {
        rdBtnOption1.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption2.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption3.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption4.setTextColor(txtColourDefaultRdBtn);
        rdGrpMcOptions.clearCheck();
        txtViewCorrectAnswer.setText("");

        if (questionCounter < questionCountTotal) {
            currentQuestion = mcQuestionList.get(questionCounter);

            txtViewQuestion.setText(currentQuestion.getQuestion());
            rdBtnOption1.setText(currentQuestion.getOption1());
            rdBtnOption2.setText(currentQuestion.getOption2());
            rdBtnOption3.setText(currentQuestion.getOption3());
            rdBtnOption4.setText(currentQuestion.getOption4());

            questionCounter++;
            txtViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            btnConfirmNext.setText("Confirm");

            timeLeftInMs = TIMER_IN_MS;
            startTimer();
        } else {
            finishQuiz();
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMs = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeLeftInMs = 0;
                updateTimerText();
                checkAnswer();
            }
        }.start();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMs / 1000) / 60;
        int seconds = (int) (timeLeftInMs / 1000) % 60;

        String timerFormat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        txtViewTimer.setText(timerFormat);

        if (timeLeftInMs < 10000) {
            txtViewTimer.setTextColor(Color.RED);
        } else {
            txtViewTimer.setTextColor(txtColourDefaultTimer);
        }
    }

    private void checkAnswer() {
        answered = true;

        timer.cancel();

        RadioButton rdBtnSelected = findViewById(rdGrpMcOptions.getCheckedRadioButtonId());
        answerNo = rdGrpMcOptions.indexOfChild(rdBtnSelected) + 1;

        if (answerNo == Integer.parseInt(currentQuestion.getAnswer())) {
            score++;
            txtViewScore.setText("Score: " + score);
        }

        showCorrectAnswer(answerNo);
    }

    private void showCorrectAnswer(int answerNo) {
        rdBtnOption1.setTextColor(Color.RED);
        rdBtnOption2.setTextColor(Color.RED);
        rdBtnOption3.setTextColor(Color.RED);
        rdBtnOption4.setTextColor(Color.RED);

        switch (Integer.parseInt(currentQuestion.getAnswer())) {
            case 1:
                rdBtnOption1.setTextColor(Color.GREEN);
                if (answerNo != 1) {
                    correctAnswer = "The correct answer is A.";
                }
                break;
            case 2:
                rdBtnOption2.setTextColor(Color.GREEN);
                if (answerNo != 2) {
                    correctAnswer = "The correct answer is B.";
                }
                break;
            case 3:
                rdBtnOption3.setTextColor(Color.GREEN);
                if (answerNo != 3) {
                    correctAnswer = "The correct answer is C.";
                }
                break;
            case 4:
                rdBtnOption4.setTextColor(Color.GREEN);
                if (answerNo != 4) {
                    correctAnswer = "The correct answer is D.";
                }
                break;
        }

        txtViewCorrectAnswer.setText(correctAnswer);

        if (questionCounter < questionCountTotal) {
            btnConfirmNext.setText("Next");
        } else {
            btnConfirmNext.setText("Finish");
        }
    }

    private void finishQuiz() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNTER, questionCounter);
        outState.putLong(KEY_TIME_LEFT, timeLeftInMs);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, mcQuestionList);
    }
}