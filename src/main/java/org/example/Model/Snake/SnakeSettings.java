package org.example.Model.Snake;

public class SnakeSettings {
    public int sizeX = 20;
    public int sizeY = 20;
    public int speed = 1;
    public int length = 1;
    public boolean moveToOtherSide = false;
    public int chanceForShortPoint = 10;
    public int pointsOnBoard = 10;

    public SnakeSettings() {
    }

    public SnakeSettings(int sizeX, int sizeY, int speed, int length, boolean moveToOtherSide) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.speed = speed;
        this.length = length;
        this.moveToOtherSide = moveToOtherSide;
    }
}
