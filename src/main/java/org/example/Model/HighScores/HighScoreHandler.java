package org.example.Model.HighScores;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreHandler {
    private static final String HIGH_SCORES_FILE = "highScores.txt";

    private List<HighScore> highScores = new ArrayList<>();

    public HighScoreHandler(){
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
        // Sort
        highScores.sort((h1, h2) -> h2.getScore() - h1.getScore());
    }

    public void setWagaCzasu(int waga){
        HighScore.WagaCzasu = waga;
    }

    public void saveHighScores(HighScore score) {
        highScores = loadHighScore();
        highScores = highScores == null ? new ArrayList<>() : highScores;

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(HIGH_SCORES_FILE);
            oos = new ObjectOutputStream(fos);

            highScores.add(score);

            for (var highScore : highScores) {
                oos.writeObject(highScore);
            }

            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje");
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<HighScore> loadHighScore() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(HIGH_SCORES_FILE);
            ois = new ObjectInputStream(fis);

            highScores = new ArrayList<>();
            HighScore readCase;
            do {
                readCase = (HighScore) ois.readObject();
                if (readCase != null) {
                    highScores.add(readCase);
                }
            } while (readCase != null);

            ois.close();
            fis.close();

            return highScores;
        } catch (FileNotFoundException e) {
            System.out.println("Plik nie istnieje");
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return highScores;
    }
}
