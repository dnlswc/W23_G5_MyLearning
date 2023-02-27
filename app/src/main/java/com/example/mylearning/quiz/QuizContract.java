package com.example.mylearning.quiz;

import android.provider.BaseColumns;

public final class QuizContract {

    // for preventing accidentally creating objects with this class
    private QuizContract() {

    }

    public static class TopicsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_topics";
        public static final String COLUMN_NAME = "name";
    }

    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_CHAPTER = "chapter";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_TOPIC_ID = "topic_id";
    }

}
