package com.example.mylearning.quiz;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {
    public static final String TYPE_TFQ = "True or False";
    public static final String TYPE_MCQ = "Multiple Choice";
    public static final String TYPE_FITBQ = "Fill in the Blank";

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private String type;
    private String difficulty;
    private String topic;
    private String chapter;
    private String question;
    private String answer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

    public Question() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public static String[] getAllTypes() {
        return new String[] {
                TYPE_TFQ, TYPE_MCQ, TYPE_FITBQ
        };
    }

    public static String[] getAllDifficultyLevels() {
        return new String[] {
          DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD
        };
    }

    protected Question(Parcel in) {
        type = in.readString();
        difficulty = in.readString();
        topic = in.readString();
        chapter = in.readString();
        question = in.readString();
        answer = in.readString();
        option1 = in.readString();
        option2 = in.readString();
        option3 = in.readString();
        option4 = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(difficulty);
        dest.writeString(topic);
        dest.writeString(chapter);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(option1);
        dest.writeString(option2);
        dest.writeString(option3);
        dest.writeString(option4);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}