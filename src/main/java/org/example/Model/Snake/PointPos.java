package org.example.Model.Snake;

public class PointPos extends Pos{
    public boolean isShortentingPoint;

    public PointPos(int x, int y, boolean isShortentingPoint) {
        super(x, y);
        this.isShortentingPoint = isShortentingPoint;
    }
}
