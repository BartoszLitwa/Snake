package org.example.Model.Snake;

import javafx.scene.input.KeyCode;
import org.example.Model.HighScores.HighScore;

import java.util.ArrayList;
import java.util.Random;

public class SnakeHandler {
    private ArrayList<Pos> snake;
    private ArrayList<PointPos> points;
    private SnakeSettings settings;
    private HighScore currentScore;
    private KeyCode lastDirection = KeyCode.DOWN;

    private final String SNAKE_IMAGE_PATH = "file:src/main/resources/images/";
    // Img paths
    private String apple = "apple.png", apple2 = "apple_2.png",
            tailDown = "tail_down.png", tailUp = "tail_up.png", tailRight = "tail_right.png", tailLeft = "tail_left.png",
            headDown = "head_down.png", headUp = "head_up.png", headRight = "head_right.png", headLeft = "head_left.png",
            bodyBottomLeft = "body_bottomleft.png", bodyBottomRight = "body_bottomright.png", bodyTopLeft = "body_topleft.png",
            bodyTopRight = "body_topright.png", bodyVertical = "body_vertical.png", bodyHorizontal = "body_horizontal.png";

    public SnakeHandler(SnakeSettings settings) {
        this.settings = settings;

        snake = new ArrayList<>();
        points = new ArrayList<>();
        currentScore = new HighScore(settings.sizeX, settings.sizeY);

        // Add the snake head and body to the queue
        for (int i = settings.length - 1; i >= 0 ; i--) {
            snake.add(new Pos(0, 0 + i));
        }
    }

    public ArrayList<PointPos> getPoints() {
        return points;
    }

    public ArrayList<Pos> getSnake() {
        return snake;
    }

    public boolean SnakeDied(){
        var currentPos = new Pos(getPos().X, getPos().Y);

        // Check if hit the wall and if turned on move to the other side
        if(snake.stream().anyMatch(x -> x.X < 0)) {
            for (int i = 0; i < snake.size(); i++) {
                if(snake.get(i).X < 0) {
                    snake.get(i).X = settings.sizeX - 1;
                }
            }
            if(!settings.moveToOtherSide) {
                return true;
            }
        }
        if(snake.stream().anyMatch(x -> x.Y < 0)) {
            for (int i = 0; i < snake.size(); i++) {
                if(snake.get(i).Y < 0) {
                    snake.get(i).Y = settings.sizeY - 1;
                }
            }
            if(!settings.moveToOtherSide) {
                return true;

            }
        }
        if(snake.stream().anyMatch(x -> x.X >= settings.sizeX)) {
            for (int i = 0; i < snake.size(); i++) {
                if(snake.get(i).X >= settings.sizeX) {
                    snake.get(i).X = 0;
                }
            }
            if(!settings.moveToOtherSide) {
                return true;
            }
        }
        if(snake.stream().anyMatch(x -> x.Y >= settings.sizeX)) {
            for (int i = 0; i < snake.size(); i++) {
                if(snake.get(i).Y >= settings.sizeY) {
                    snake.get(i).Y = 0;
                }
            }
            if(!settings.moveToOtherSide) {
                return true;
            }
        }
        // Start from second element of the queue
        for (int i = 1; i < snake.size(); i++) {
            if(snake.get(i).X == snake.get(0).X &&
                snake.get(i).Y == snake.get(0).Y){
                return true;
            }
        }
        return snake.size() < 1;
    }

    public SnakeStatus SnakeAteFood(){
        var currentPos = getPos();

        for (int i = 0; i < points.size(); i++) {
            var point = points.get(i);
            if(point.X == currentPos.X && point.Y == currentPos.Y){
                var type = point.isShortentingPoint ? SnakeStatus.SHORT_POINT : SnakeStatus.POINT;
                points.remove(i);
                return type;
            }
        }
        return SnakeStatus.NOTHING;
    }

    public Pos getPos(){
        return getSnakeSize() == 0 ? null : snake.get(0);
    }

    public int getSnakeSize() {
        return snake.size();
    }

    public KeyCode getLastDirection(){
        return lastDirection;
    }

    public SnakeStatus move(KeyCode keyCode) {
        // Set last direction that snake moved
        lastDirection = keyCode == KeyCode.UNDEFINED ? lastDirection : keyCode;
        switch (keyCode) {
            case UP:
                return move(0, -1);
            case DOWN:
                return move(0, 1);
            case LEFT:
                return move(-1, 0);
            case RIGHT:
                return move(1, 0);
            default: // UNDEFINED
                return move();
        }
    }

    private SnakeStatus move(int x, int y) {
        if(getSnakeSize() == 0){
            return SnakeStatus.DEAD;
        }

        // Copy last position of the snake
        var currentPos = new Pos(getPos().X, getPos().Y);

        // Push new position to move snake
        snake.add(0, new Pos(currentPos.X + x, currentPos.Y + y));
        // Remove the last position
        snake.remove(snake.size() - 1);

        if(SnakeDied()) {
            // Game over
            return SnakeStatus.DEAD;
        }

        var status = SnakeAteFood();
        if(status != SnakeStatus.NOTHING){
            if(status == SnakeStatus.POINT){
                // Add new point
                currentScore.Score++;
                // Add new point to make snake longer
                snake.add(new Pos(getPos().X + x, getPos().Y + y));
            } else if(status == SnakeStatus.SHORT_POINT){
                // Add new point
                currentScore.ShortScore++;
                // Remove 1 point
                snake.remove(0);
            }
            return status;
        }

        return SnakeStatus.ALIVE;
    }

    public SnakeStatus move() {
        switch (lastDirection) {
            case UP:
                return move(0, -1);
            case LEFT:
                return move(-1, 0);
            case RIGHT:
                return move(1, 0);
            default: // DOWN AND UNDEFINED
                return move(0, 1);
        }
    }

    public String getImgForPoint(int index){
        var point = points.get(index);

        if(point == null) {
            return null;
        }

        return SNAKE_IMAGE_PATH + (point.isShortentingPoint ? apple2 : apple);
    }

    public String getImgForPart(int index){
        var size = getSnakeSize();
        var pos = getSnake().get(index);
       var result = "";
        // Head
        if(index == 0){
            if(size > 1){
                // Check body after head
                var nextPos = getSnake().get(index + 1);

                // To Left
                if(nextPos.X > pos.X){
                    result =  headLeft;
                } // To Right
                else if(nextPos.X < pos.X){
                    result =  headRight;
                } // To Up
                else if(nextPos.Y > pos.Y){
                    result =  headUp;
                } // To Down
                else if(nextPos.Y < pos.Y){
                    result =  headDown;
                }
            } else {
                result = lastDirection == KeyCode.UP ? headUp : lastDirection == KeyCode.DOWN ? headDown : lastDirection == KeyCode.LEFT ? headLeft : headRight;
            }
        } // tail
        else if (index == size - 1 && size > 1) {
            // Check body before tail
            var prevPos = getSnake().get(index - 1);

            // To Left
            if(prevPos.X > pos.X){
                result =  tailLeft;
            } // To Right
            else if(prevPos.X < pos.X){
                result =  tailRight;
            } // To Up
            else if(prevPos.Y > pos.Y){
                result =  tailUp;
            } // To Down
            else if(prevPos.Y < pos.Y){
                result =  tailDown;
            }
        } // body
        else if (index != 0 && index != (size - 1) && size > 2) {
            var prevPos = getSnake().get(index - 1);
            var nextPos = getSnake().get(index + 1);

            // Horizontal
            if((prevPos.X > pos.X && nextPos.X < pos.X) || (prevPos.X < pos.X && nextPos.X > pos.X)){
                result =  bodyHorizontal;
            } // Vertical
            else if((prevPos.Y > pos.Y && nextPos.Y < pos.Y) || (prevPos.Y < pos.Y && nextPos.Y > pos.Y)){
                result =  bodyVertical;
            } // Bottom Left |_
            else if((prevPos.X == pos.X && prevPos.Y < pos.Y && nextPos.X > pos.X && nextPos.Y == pos.Y)
            || (nextPos.X == pos.X && nextPos.Y < pos.Y && prevPos.X > pos.X && prevPos.Y == pos.Y)){
                result =  bodyBottomLeft;
            } // Bottom Right _|
            else if((prevPos.X == pos.X && prevPos.Y < pos.Y && nextPos.X < pos.X && nextPos.Y == pos.Y)
                    || (nextPos.X == pos.X && nextPos.Y < pos.Y && prevPos.X < pos.X && prevPos.Y == pos.Y)){
                result =  bodyBottomRight;
            } // Top Left |-
            else if((prevPos.X == pos.X && prevPos.Y > pos.Y && nextPos.X > pos.X && nextPos.Y == pos.Y)
                    || (nextPos.X == pos.X && nextPos.Y > pos.Y && prevPos.X > pos.X && prevPos.Y == pos.Y)){
                result =  bodyTopLeft;
            } // Top Right -|
            else if((prevPos.X == pos.X && prevPos.Y > pos.Y && nextPos.X < pos.X && nextPos.Y == pos.Y)
                    || (nextPos.X == pos.X && nextPos.Y > pos.Y && prevPos.X < pos.X && prevPos.Y == pos.Y)){
                result =  bodyTopRight;
            } else {
                result = lastDirection == KeyCode.UP || lastDirection == KeyCode.DOWN ? bodyVertical : bodyHorizontal;
            }
        }

        return SNAKE_IMAGE_PATH + result;
    }

    public boolean generatePoints(int quantity){
        if(points.size() < quantity){
            var random = new Random();
            for (int i = points.size(); i < quantity; i++) {
                var x = random.nextInt(settings.sizeX);
                var y = random.nextInt(settings.sizeY);
                var shortPoint = random.nextInt(settings.chanceForShortPoint) < 2;
                var point = new PointPos(x, y, shortPoint);
                if(!points.contains(point)){
                    points.add(point);
                }
            }

            return true;
        }
        return false;
    }

    public HighScore setCurrentScore(float time){
        currentScore.Time = time;
        currentScore.SnakeLength = getSnakeSize();
        return currentScore;
    }

    public HighScore getCurrentScore(){
        return currentScore;
    }
}
