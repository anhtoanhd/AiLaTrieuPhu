package com.toanpt.ailatrieuphu.Model;

public class HighScore {
    private int id;
    private String name;
    private int highscore;

    public HighScore() {
    }

    public HighScore(int id, String name, int highscore) {
        this.id = id;
        this.name = name;
        this.highscore = highscore;
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

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }
}
