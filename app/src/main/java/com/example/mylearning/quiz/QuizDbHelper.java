package com.example.mylearning.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "quizdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "quiz_questions";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_TOPIC = "topic";
    private static final String COLUMN_CHAPTER = "chapter";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_OPTION1 = "option1";
    private static final String COLUMN_OPTION2 = "option2";
    private static final String COLUMN_OPTION3 = "option3";
    private static final String COLUMN_OPTION4 = "option4";

    private static QuizDbHelper instance;
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE =
            "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_DIFFICULTY + " TEXT, "
                + COLUMN_TOPIC + " TEXT, "
                + COLUMN_CHAPTER + " TEXT, "
                + COLUMN_QUESTION + " TEXT, "
                + COLUMN_ANSWER + " TEXT, "
                + COLUMN_OPTION1 + " TEXT, "
                + COLUMN_OPTION2 + " TEXT, "
                + COLUMN_OPTION3 + " TEXT, "
                + COLUMN_OPTION4 + " TEXT)";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addQuestion(Question question) {
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TYPE, question.getType());
        cv.put(COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(COLUMN_TOPIC, question.getTopic());
        cv.put(COLUMN_CHAPTER, question.getChapter());
        cv.put(COLUMN_QUESTION, question.getQuestion());
        cv.put(COLUMN_ANSWER, question.getAnswer());
        cv.put(COLUMN_OPTION1, question.getOption1());
        cv.put(COLUMN_OPTION2, question.getOption2());
        cv.put(COLUMN_OPTION3, question.getOption3());
        cv.put(COLUMN_OPTION4, question.getOption4());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public ArrayList<String> getTopics() {
        db = this.getReadableDatabase();
        ArrayList<String> topicList = new ArrayList<>();
        String[] columns = {COLUMN_TOPIC};
        Cursor cursor = null;

        cursor = db.query(true, TABLE_NAME, columns, null, null, null, null, COLUMN_TOPIC, null);

        if (cursor.moveToFirst()) {
            do {
                topicList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC)));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return topicList;
    }

    public ArrayList<Question> getQuestions(String type, String topic, String difficulty) {
        db = this.getReadableDatabase();
        ArrayList<Question> questionList = new ArrayList<>();
        String selection = "";
        String[] selectionArgs = null;
        Cursor cursor = null;

        if (type.equalsIgnoreCase("All")
                && topic.equalsIgnoreCase("All")
                && difficulty.equalsIgnoreCase("All")) {
            cursor = db.query(TABLE_NAME, null,
                    null, null,
                    null, null,
                    null);
        } else {
            if (!(type.equalsIgnoreCase("All"))
                    && !(topic.equalsIgnoreCase("All"))
                    && !(difficulty.equalsIgnoreCase("All"))) {
                selection = COLUMN_TYPE + " = ? "
                        + " AND " + COLUMN_TOPIC + " = ? "
                        + " AND " + COLUMN_DIFFICULTY + " = ? ";
                selectionArgs = new String[] {type, topic, difficulty};

            } else if (type.equalsIgnoreCase("All")
                    && !(topic.equalsIgnoreCase("All"))
                    && !(difficulty.equalsIgnoreCase("All"))) {
                selection = COLUMN_TOPIC + " = ? "
                        + " AND " + COLUMN_DIFFICULTY + " = ? ";
                selectionArgs = new String[] {topic, difficulty};

            } else if (!(type.equalsIgnoreCase("All"))
                    && topic.equalsIgnoreCase("All")
                    && !(difficulty.equalsIgnoreCase("All"))) {
                selection = COLUMN_TYPE + " = ? "
                        + " AND " + COLUMN_DIFFICULTY + " = ? ";
                selectionArgs = new String[] {type, difficulty};

            } else if (!(type.equalsIgnoreCase("All"))
                    && !(topic.equalsIgnoreCase("All"))
                    && difficulty.equalsIgnoreCase("All")) {
                selection = COLUMN_TYPE + " = ? "
                        + " AND " + COLUMN_TOPIC + " = ? ";
                selectionArgs = new String[] {type, topic};

            } else if (type.equalsIgnoreCase("All")
                    && topic.equalsIgnoreCase("All")
                    && !(difficulty.equalsIgnoreCase("All"))) {
                selection = COLUMN_DIFFICULTY + " = ? ";
                selectionArgs = new String[] {difficulty};

            } else if (type.equalsIgnoreCase("All")
                    && !(topic.equalsIgnoreCase("All"))
                    && difficulty.equalsIgnoreCase("All")) {
                selection = COLUMN_TOPIC + " = ? ";
                selectionArgs = new String[] {topic};

            } else if (!(type.equalsIgnoreCase("All"))
                    && topic.equalsIgnoreCase("All")
                    && difficulty.equalsIgnoreCase("All")) {
                selection = COLUMN_TYPE + " = ? ";
                selectionArgs = new String[] {type};

            }

            cursor = db.query(TABLE_NAME, null,
                    selection, selectionArgs,
                    null, null,
                    null);
        }

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setType(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIFFICULTY)));
                question.setTopic(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOPIC)));
                question.setChapter(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CHAPTER)));
                question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER)));
                question.setOption1(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION3)));
                question.setOption4(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OPTION4)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return questionList;
    }

    public void resetTable() {
        db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
