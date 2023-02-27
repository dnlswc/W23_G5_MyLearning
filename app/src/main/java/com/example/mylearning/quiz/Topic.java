package com.example.mylearning.quiz;

public class Topic {

    public static final int C_SHARP = 1;
    public static final int JAVA = 2;
    public static final int SQL = 3;
    public static final int SYSTEMS_DESIGN = 4;
    public static final int OPERATING_SYSTEMS = 5;
    public static final int COMPUTER_NETWORKING = 6;

    private int id;
    private String name;

    public Topic() {

    }

    public Topic(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
