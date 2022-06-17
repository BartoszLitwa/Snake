package org.example.Model.HighScores;

import java.io.Serializable;

public class HighScore implements Serializable {
    public static int WagaCzasu = 5;
    public int Score;
    public int ShortScore;
    public float Time;
    public int SizeX;
    public int SizeY;
    public String Name;
    public int SnakeLength;
    public int OverallScore;

    public HighScore(int sizeX, int sizeY){
        this.SizeX = sizeX;
        this.SizeY = sizeY;
    }

    public HighScore(int score, int shortScore, float time, int sizeX, int sizeY) {
        this.Score = score;
        this.ShortScore = shortScore;
        this.Time = time;
        this.SizeX = sizeX;
        this.SizeY = sizeY;
    }

    public int getScore() {
        this.OverallScore = (int)((Score + ShortScore) * (Time / (SizeX * SizeY)) * WagaCzasu * 100);
        return OverallScore;
    }
}
