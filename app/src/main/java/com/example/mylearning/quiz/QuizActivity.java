package com.example.mylearning.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylearning.MainActivity;
import com.example.mylearning.R;
import com.example.mylearning.news.NewsActivity;
import com.example.mylearning.notepad.NotePageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private static final long TIMER_IN_MS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNTER = "keyQuestionCounter";
    private static final String KEY_TIME_LEFT = "keyTimeLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    private static final String KEY_CORRECT_ANSWER = "keyCorrectAnswer";
    private static final String KEY_TFQ_FITBQ_ANSWER = "keyTfqFitbqAnswer";

    private TextView txtViewScore;
    private TextView txtViewQuestionCount;
    private TextView txtViewTimer;
    private TextView txtViewTopicDifficulty;
    private TextView txtViewQuestion;

    private RadioGroup rdGrpTfOptions;
    private RadioButton rdBtnTrue;
    private RadioButton rdBtnFalse;

    private RadioGroup rdGrpMcOptions;
    private RadioButton rdBtnOption1;
    private RadioButton rdBtnOption2;
    private RadioButton rdBtnOption3;
    private RadioButton rdBtnOption4;

    private EditText editTxtFitbAnswer;

    private TextView txtViewCorrectAnswer;
    private Button btnConfirmNext;

    private ArrayList<Question> questionList;

    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private String correctAnswer;

    private int score;
    private boolean answered;
    private int mcqAnswer;
    private String tfqFitbqAnswer;

    private ColorStateList txtColourDefaultRdBtn;
    private ColorStateList lineColourDefaultEditTxt;
    private ColorStateList lineColourGreenEditTxt;
    private ColorStateList lineColourRedEditTxt;
    private ColorStateList txtColourDefaultTimer;

    private CountDownTimer timer;
    private long timeLeftInMs;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        txtViewScore = findViewById(R.id.txtViewScore);
        txtViewQuestionCount = findViewById(R.id.txtViewQuestionCount);
        txtViewTimer = findViewById(R.id.txtViewTimer);
        txtViewTopicDifficulty = findViewById(R.id.txtViewTopicDifficulty);
        txtViewQuestion = findViewById(R.id.txtViewQuestion);

        rdGrpTfOptions = findViewById(R.id.rdGrpTfOptions);
        rdBtnTrue = findViewById(R.id.rdBtnTrue);
        rdBtnFalse = findViewById(R.id.rdBtnFalse);

        rdGrpMcOptions = findViewById(R.id.rdGrpMcOptions);
        rdBtnOption1 = findViewById(R.id.rdBtnOption1);
        rdBtnOption2 = findViewById(R.id.rdBtnOption2);
        rdBtnOption3 = findViewById(R.id.rdBtnOption3);
        rdBtnOption4 = findViewById(R.id.rdBtnOption4);
        editTxtFitbAnswer = findViewById(R.id.editTxtFitbAnswer);

        txtViewCorrectAnswer = findViewById(R.id.txtViewCorrectAnswer);
        btnConfirmNext = findViewById(R.id.btnConfirmNext);
        bottomNavigationView = findViewById(R.id.bottom_navigator);

        txtColourDefaultRdBtn = rdBtnTrue.getTextColors();
        lineColourDefaultEditTxt = editTxtFitbAnswer.getBackgroundTintList();
        lineColourGreenEditTxt = ColorStateList.valueOf(getResources().getColor(R.color.green));
        lineColourRedEditTxt = ColorStateList.valueOf(getResources().getColor(R.color.red));
        txtColourDefaultTimer = txtViewTimer.getTextColors();

        Intent quizIntent = getIntent();
        String topic = quizIntent.getStringExtra(QuizCatalogueActivity.EXTRA_TOPIC);
        String difficulty = quizIntent.getStringExtra(QuizCatalogueActivity.EXTRA_DIFFICULTY);

        txtViewTopicDifficulty.setText(topic + " (" + difficulty + ")");

        if (savedInstanceState == null) {
            QuizDbHelper quizDbHelper = QuizDbHelper.getInstance(this);
            questionList = quizDbHelper.getQuestions(topic, difficulty);

            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            if (questionList == null) {
                finish();
            }
            score = savedInstanceState.getInt(KEY_SCORE);
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNTER);
            timeLeftInMs = savedInstanceState.getLong(KEY_TIME_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            correctAnswer = savedInstanceState.getString(KEY_CORRECT_ANSWER);
            tfqFitbqAnswer = savedInstanceState.getString(KEY_TFQ_FITBQ_ANSWER);

            questionCountTotal = questionList.size();
            currentQuestion = questionList.get(questionCounter - 1);

            if (currentQuestion.getCategory().equalsIgnoreCase("TF")) {
                showTfOptions();
                hideMcOptions();
                hideFitbTxtField();
            } else if (currentQuestion.getCategory().equalsIgnoreCase("MC")) {
                hideTfOptions();
                showMcOptions();
                hideFitbTxtField();
            } else {
                hideTfOptions();
                hideMcOptions();
                showFitbTxtField();
            }

            if (!answered) {
                startTimer();
            } else {
                updateTimerText();
                if (currentQuestion.getCategory().equalsIgnoreCase("TF")
                        || currentQuestion.getCategory().equalsIgnoreCase("FITB")) {
                    showCorrectTfqFitbqAnswer(tfqFitbqAnswer);
                } else {
                    showCorrectMcqAnswer(mcqAnswer);
                }
            }
        }

        btnConfirmNext.setOnClickListener((View v) -> {
            if (!answered) {
                if (currentQuestion.getCategory().equalsIgnoreCase("TF")) {
                    if (rdBtnTrue.isChecked() || rdBtnFalse.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an option.", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentQuestion.getCategory().equalsIgnoreCase("MC")) {
                    if (rdBtnOption1.isChecked() || rdBtnOption2.isChecked() ||
                            rdBtnOption3.isChecked() || rdBtnOption4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an option.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!(editTxtFitbAnswer.getText().toString().isEmpty())) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please enter an answer.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                showNextQuestion();
            }
        });

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

    public void showTfOptions() {
        rdGrpTfOptions.setVisibility(View.VISIBLE);
    }

    public void hideTfOptions() {
        rdGrpTfOptions.setVisibility(View.GONE);
    }

    public void showMcOptions() {
        rdGrpMcOptions.setVisibility(View.VISIBLE);
    }

    public void hideMcOptions() {
        rdGrpMcOptions.setVisibility(View.GONE);
    }

    public void showFitbTxtField() {
        editTxtFitbAnswer.setVisibility(View.VISIBLE);
    }

    public void hideFitbTxtField() {
        editTxtFitbAnswer.setVisibility(View.GONE);
    }

    private void showNextQuestion() {
        rdBtnTrue.setTextColor(txtColourDefaultRdBtn);
        rdBtnFalse.setTextColor(txtColourDefaultRdBtn);
        rdGrpTfOptions.clearCheck();

        rdBtnOption1.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption2.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption3.setTextColor(txtColourDefaultRdBtn);
        rdBtnOption4.setTextColor(txtColourDefaultRdBtn);
        rdGrpMcOptions.clearCheck();

        editTxtFitbAnswer.setBackgroundTintList(lineColourDefaultEditTxt);
        editTxtFitbAnswer.setText("");

        txtViewCorrectAnswer.setText("");

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            txtViewQuestion.setText(currentQuestion.getQuestion());

            if (currentQuestion.getCategory().equalsIgnoreCase("TF")) {
                showTfOptions();
                hideMcOptions();
                hideFitbTxtField();
                rdBtnTrue.setText("True");
                rdBtnFalse.setText("False");
            } else if (currentQuestion.getCategory().equalsIgnoreCase("MC")) {
                hideTfOptions();
                showMcOptions();
                hideFitbTxtField();
                rdBtnOption1.setText(currentQuestion.getOption1());
                rdBtnOption2.setText(currentQuestion.getOption2());
                rdBtnOption3.setText(currentQuestion.getOption3());
                rdBtnOption4.setText(currentQuestion.getOption4());
            } else {
                hideTfOptions();
                hideMcOptions();
                showFitbTxtField();
            }

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

        if (currentQuestion.getCategory().equalsIgnoreCase("MC")) {
            RadioButton rdBtnSelected = findViewById(rdGrpMcOptions.getCheckedRadioButtonId());
            mcqAnswer = rdGrpMcOptions.indexOfChild(rdBtnSelected) + 1;

            if (mcqAnswer == Integer.parseInt(currentQuestion.getAnswer())) {
                score++;
                txtViewScore.setText("Score: " + score);
            }

            showCorrectMcqAnswer(mcqAnswer);
            return;
        }

        if (currentQuestion.getCategory().equalsIgnoreCase("TF")) {
            RadioButton rdBtnSelected = findViewById(rdGrpTfOptions.getCheckedRadioButtonId());

            if (rdGrpTfOptions.indexOfChild(rdBtnSelected) == 0) {
                tfqFitbqAnswer = "True";
            } else if (rdGrpTfOptions.indexOfChild(rdBtnSelected) == 1) {
                tfqFitbqAnswer = "False";
            } else {
                tfqFitbqAnswer = "";
            }

            if (tfqFitbqAnswer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                score++;
                txtViewScore.setText("Score: " + score);
            }
        } else if (currentQuestion.getCategory().equalsIgnoreCase("FITB")) {
            tfqFitbqAnswer = editTxtFitbAnswer.getText().toString();

            if (tfqFitbqAnswer.equalsIgnoreCase(currentQuestion.getAnswer())) {
                score++;
                txtViewScore.setText("Score: " + score);
            }
        }

        showCorrectTfqFitbqAnswer(tfqFitbqAnswer);
    }

    private void showCorrectMcqAnswer(int mcqAnswer) {
        rdBtnOption1.setTextColor(Color.RED);
        rdBtnOption2.setTextColor(Color.RED);
        rdBtnOption3.setTextColor(Color.RED);
        rdBtnOption4.setTextColor(Color.RED);

        switch (Integer.parseInt(currentQuestion.getAnswer())) {
            case 1:
                rdBtnOption1.setTextColor(Color.GREEN);
                if (mcqAnswer != 1) {
                    correctAnswer = "Correct Answer: A";
                } else {
                    correctAnswer = "";
                }
                break;
            case 2:
                rdBtnOption2.setTextColor(Color.GREEN);
                if (mcqAnswer != 2) {
                    correctAnswer = "Correct Answer: B";
                } else {
                    correctAnswer = "";
                }
                break;
            case 3:
                rdBtnOption3.setTextColor(Color.GREEN);
                if (mcqAnswer != 3) {
                    correctAnswer = "Correct Answer: C";
                } else {
                    correctAnswer = "";
                }
                break;
            case 4:
                rdBtnOption4.setTextColor(Color.GREEN);
                if (mcqAnswer != 4) {
                    correctAnswer = "Correct Answer: D";
                } else {
                    correctAnswer = "";
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

    private void showCorrectTfqFitbqAnswer(String tfqFitbqAnswer) {
        if (currentQuestion.getCategory().equalsIgnoreCase("TF")) {
            rdBtnTrue.setTextColor(Color.RED);
            rdBtnFalse.setTextColor(Color.RED);

            if (currentQuestion.getAnswer().equalsIgnoreCase("True")) {
                rdBtnTrue.setTextColor(Color.GREEN);
                if (!(tfqFitbqAnswer.equalsIgnoreCase("True"))) {
                    correctAnswer = "Correct Answer: True";
                } else {
                    correctAnswer = "";
                }
            } else if (currentQuestion.getAnswer().equalsIgnoreCase("False")) {
                rdBtnFalse.setTextColor(Color.GREEN);
                if (!(tfqFitbqAnswer.equalsIgnoreCase("False"))) {
                    correctAnswer = "Correct Answer: False";
                } else {
                    correctAnswer = "";
                }
            }
        } else if (currentQuestion.getCategory().equalsIgnoreCase("FITB")) {
            editTxtFitbAnswer.setBackgroundTintList(lineColourRedEditTxt);
            if (!(tfqFitbqAnswer.equalsIgnoreCase(currentQuestion.getAnswer()))) {
                correctAnswer = "Correct Answer: " + currentQuestion.getAnswer();
            } else {
                editTxtFitbAnswer.setBackgroundTintList(lineColourGreenEditTxt);
                correctAnswer = "";
            }
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNTER, questionCounter);
        outState.putLong(KEY_TIME_LEFT, timeLeftInMs);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
        outState.putString(KEY_TFQ_FITBQ_ANSWER, tfqFitbqAnswer);
    }
}