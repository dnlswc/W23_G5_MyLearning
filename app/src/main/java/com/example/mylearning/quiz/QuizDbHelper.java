package com.example.mylearning.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mylearning.quiz.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    private static QuizDbHelper instance;

    private SQLiteDatabase db;

    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

        final String SQL_CREATE_TOPICS_TABLE =
                "CREATE TABLE " + TopicsTable.TABLE_NAME + " ("
                        + TopicsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + TopicsTable.COLUMN_NAME + " TEXT"
                        + ")";

        final String SQL_CREATE_QUESTIONS_TABLE =
                "CREATE TABLE " + QuestionsTable.TABLE_NAME + " ("
                        + QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + QuestionsTable.COLUMN_CATEGORY + " TEXT, "
                        + QuestionsTable.COLUMN_CHAPTER + " INTEGER, "
                        + QuestionsTable.COLUMN_DIFFICULTY + " TEXT, "
                        + QuestionsTable.COLUMN_QUESTION + " TEXT, "
                        + QuestionsTable.COLUMN_ANSWER + " TEXT, "
                        + QuestionsTable.COLUMN_OPTION1 + " TEXT, "
                        + QuestionsTable.COLUMN_OPTION2 + " TEXT, "
                        + QuestionsTable.COLUMN_OPTION3 + " TEXT, "
                        + QuestionsTable.COLUMN_OPTION4 + " TEXT, "
                        + QuestionsTable.COLUMN_TOPIC_ID + " INTEGER, "
                        + "FOREIGN KEY(" + QuestionsTable.COLUMN_TOPIC_ID + ") REFERENCES "
                        + TopicsTable.TABLE_NAME + "(" + TopicsTable._ID + ")" + " ON DELETE CASCADE"
                        + ")";

        db.execSQL(SQL_CREATE_TOPICS_TABLE);
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillTopicsTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TopicsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillTopicsTable() {
        Topic t1 = new Topic("C#");
        addTopic(t1);

        Topic t2 = new Topic("Java");
        addTopic(t2);

        Topic t3 = new Topic("SQL");
        addTopic(t3);

        Topic t4 = new Topic("Systems Design");
        addTopic(t4);

        Topic t5 = new Topic("Operating Systems");
        addTopic(t5);

        Topic t6 = new Topic("Computer Networking");
        addTopic(t6);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question(
                "TF", 1, Question.DIFFICULTY_EASY,
                "Computer literacy is understanding the role of information in generating and using business intelligence.",
                Question.TFQ_FALSE, Question.TFQ_TRUE, Question.TFQ_FALSE, "", "", Topic.SYSTEMS_DESIGN
        );
        addQuestion(q1);

        Question q2 = new Question(
                "TF", 2, Question.DIFFICULTY_MEDIUM,
                "The arithmetic logic unit (ALU) performs only arithmetic operations.",
                Question.TFQ_FALSE, Question.TFQ_TRUE, Question.TFQ_FALSE, "", "", Topic.SYSTEMS_DESIGN
        );
        addQuestion(q2);

        Question q3 = new Question(
                "MC", 3, Question.DIFFICULTY_HARD,
                "Which of the following is not among the 5Vs of big data?", "1",
                "Variance",
                "Value",
                "Variety",
                "Veracity",
                Topic.SYSTEMS_DESIGN
        );
        addQuestion(q3);

        Question q4 = new Question(
                "MC", 1, Question.DIFFICULTY_EASY,
                "The fully qualified call to the method that allows the user to input a single character is:", "2",
                "Console.System.Read()",
                "System.Console.Read()",
                "Console.System.Write()",
                "System.Console.Write()",
                Topic.C_SHARP
        );
        addQuestion(q4);

        Question q5 = new Question(
                "FITB", 1, Question.DIFFICULTY_EASY,
                "Which escape sequence can be used to indicate that the cursor should advance to the next line?",
                "\\n", "", "", "", "", Topic.C_SHARP
        );
        addQuestion(q5);

        Question q6 = new Question(
                "MC", 2, Question.DIFFICULTY_EASY,
                "One of primary differences between float, double, and decimal is:", "4",
                "Float is used to represent more significant digits.",
                "Double is normally used with large monetary values.",
                "Decimal is not used to display negative values.",
                "Double does not require suffixing a numeric literal with a value such as m or f.",
                Topic.C_SHARP
        );
        addQuestion(q6);

        Question q7 = new Question(
                "FITB", 2, Question.DIFFICULTY_MEDIUM,
                "What is stored in ans as a result of the arithmetic expression, given the following declarations?\nint ans = 10, v1 = 5, v2 = 7, v3 = 18;\nans += v1 + 10 (v2 / 5) + v3 / v2;",
                "27", "", "", "", "", Topic.C_SHARP
        );
        addQuestion(q7);

        Question q8 = new Question(
                "MC", 2, Question.DIFFICULTY_HARD,
                "Given the following method definition, what would be a valid call? The variable someIntValue is defined as an int.\nstatic int GetData(out int aValue, ref int bValue)", "2",
                "someIntValue = GetData(aValue, bValue);",
                "someIntValue = GetData(out aValue, ref bValue);",
                "someIntValue = GetData(out, ref);",
                "someIntValue = GetData(int out aValue, int ref bValue);",
                Topic.C_SHARP
        );
        addQuestion(q8);

        Question q9 = new Question(
                "MC", 1, Question.DIFFICULTY_EASY,
                "All Java applications must have a method named _____.", "2",
                "method()",
                "main()",
                "java()",
                "hello()",
                Topic.JAVA
        );
        addQuestion(q9);

        Question q10 = new Question(
                "FITB", 2, Question.DIFFICULTY_EASY,
                "Which Java statement produces w on one line and xyz on the next line?\n_____(\"w\\nxyz\");",
                "System.out.println", "", "", "", "", Topic.JAVA
        );
        addQuestion(q10);

        Question q11 = new Question(
                "FITB", 3, Question.DIFFICULTY_MEDIUM,
                "A public static method named computeSum() is located in ClassA. To call the method from within ClassB, use the statement _____.",
                "ClassA.computeSum();", "", "", "", "", Topic.JAVA
        );
        addQuestion(q11);

        Question q12 = new Question(
                "MC", 3, Question.DIFFICULTY_HARD,
                "The non-static data components of a class often are referred to as the _____ of that class.", "2",
                "access types",
                "instance variables",
                "methods",
                "objects",
                Topic.JAVA
        );
        addQuestion(q12);

        Question q13 = new Question(
                "MC", 3, Question.DIFFICULTY_MEDIUM,
                "The concept of allowing a class’s private data to be changed only by a class’s own methods is known as _____.", "3",
                "structured logic",
                "object orientation",
                "information hiding",
                "data masking",
                Topic.JAVA
        );
        addQuestion(q13);

        Question q14 = new Question(
                "FITB", 3, Question.DIFFICULTY_MEDIUM,
                "If a class is named Student, the class constructor name is _____.",
                "Student()", "", "", "", "", Topic.JAVA
        );
        addQuestion(q14);

        Question q15 = new Question(
                "TF", 1, Question.DIFFICULTY_EASY,
                "Protocol means the architecture of a network.",
                Question.TFQ_FALSE, Question.TFQ_TRUE, Question.TFQ_FALSE, "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q15);

        Question q16 = new Question(
                "TF", 1, Question.DIFFICULTY_EASY,
                "Topology means a set of rules established for users to gain control of the network to exchange information.",
                Question.TFQ_FALSE, Question.TFQ_TRUE, Question.TFQ_FALSE, "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q16);

        Question q17 = new Question(
                "FITB", 1, Question.DIFFICULTY_MEDIUM,
                "HTTP functions are on the _____ Layer of the OSI model.",
                "Application", "", "", "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q17);

        Question q18 = new Question(
                "MC", 1, Question.DIFFICULTY_MEDIUM,
                "Which information is not included in an Ethernet frame?", "1",
                "Frame size",
                "Source MAC address",
                "Padding",
                "Frame check sequence",
                Topic.COMPUTER_NETWORKING
        );
        addQuestion(q18);

        Question q19 = new Question(
                "FITB", 1, Question.DIFFICULTY_HARD,
                "On Windows 10, what command can help you find the Ethernet (MAC) address?",
                "ipconfig /all", "", "", "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q19);

        Question q20 = new Question(
                "FITB", 2, Question.DIFFICULTY_MEDIUM,
                "What is the data rate for Fast Ethernet? _____Mbps",
                "100", "", "", "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q20);

        Question q21 = new Question(
                "MC", 2, Question.DIFFICULTY_EASY,
                "A data problem is reported to the network administrator. The problem is found to be with the UTP network connection. What steps could the network administrator have taken to isolate the problem?", "2",
                "Visually inspect all UTP terminations",
                "Use the PING command to verify network connectivity",
                "Use pairs 4–5 and 7–8 to repair the connection",
                "Contact the installer of the UTP cable to obtain a certification report",
                Topic.COMPUTER_NETWORKING
        );
        addQuestion(q21);

        Question q22 = new Question(
                "TF", 2, Question.DIFFICULTY_HARD,
                "Only two wire pairs are used to obtain a proper power-sum measurement.",
                Question.TFQ_FALSE, Question.TFQ_TRUE, Question.TFQ_FALSE, "", "", Topic.COMPUTER_NETWORKING
        );
        addQuestion(q22);

        Question q23 = new Question(
                "MC", 3, Question.DIFFICULTY_HARD,
                "Which of the following is not the advantage of optical communication links?", "3",
                "Extremely wide bandwidth",
                "Elimination of crosstalk",
                "Elimination of attenuation",
                "Security",
                Topic.COMPUTER_NETWORKING
        );
        addQuestion(q23);

        Question q24 = new Question(
                "MC", 3, Question.DIFFICULTY_MEDIUM,
                "Which type of fiber is preferred for use in modern computer networks?", "3",
                "Multi-mode",
                "Polarized mode",
                "Single-mode",
                "All of these answers are correct",
                Topic.COMPUTER_NETWORKING
        );
        addQuestion(q24);

        Question q25 = new Question(
                "MC", 3, Question.DIFFICULTY_EASY,
                "What is the material surrounding the core of an optical waveguide called?", "4",
                "Aperture",
                "Mode field",
                "Step-index",
                "Cladding",
                Topic.COMPUTER_NETWORKING
        );
        addQuestion(q25);
    }

    private void addTopic(Topic topic) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TopicsTable.COLUMN_NAME, topic.getName());
        db.insert(TopicsTable.TABLE_NAME, null, contentValues);
    }

    private void addQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(QuestionsTable.COLUMN_CATEGORY, question.getCategory());
        contentValues.put(QuestionsTable.COLUMN_CHAPTER, question.getChapter());
        contentValues.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        contentValues.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        contentValues.put(QuestionsTable.COLUMN_ANSWER, question.getAnswer());
        contentValues.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        contentValues.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        contentValues.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        contentValues.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        contentValues.put(QuestionsTable.COLUMN_TOPIC_ID, question.getTopicId());
        db.insert(QuestionsTable.TABLE_NAME, null, contentValues);
    }

    public List<Topic> getAllTopics() {
        List<Topic> topicList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TopicsTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Topic topic = new Topic();
                topic.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TopicsTable._ID)));
                topic.setName(cursor.getString(cursor.getColumnIndexOrThrow(TopicsTable.COLUMN_NAME)));
                topicList.add(topic);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return topicList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable._ID)));
                question.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_CATEGORY)));
                question.setChapter(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_CHAPTER)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_ANSWER)));
                question.setOption1(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION4)));
                question.setTopicId(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_TOPIC_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int topicId, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String selection = QuestionsTable.COLUMN_TOPIC_ID + " = ? "
                + " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[] {String.valueOf(topicId), difficulty};
        Cursor cursor = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable._ID)));
                question.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_CATEGORY)));
                question.setChapter(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_CHAPTER)));
                question.setDifficulty(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setQuestion(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_QUESTION)));
                question.setAnswer(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_ANSWER)));
                question.setOption1(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(cursor.getString(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_OPTION4)));
                question.setTopicId(cursor.getInt(cursor.getColumnIndexOrThrow(QuestionsTable.COLUMN_TOPIC_ID)));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }

}
