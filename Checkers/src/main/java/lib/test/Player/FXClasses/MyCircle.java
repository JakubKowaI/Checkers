package lib.test.Player.FXClasses;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import lib.test.Player.Client;

import java.util.ArrayList;
import java.util.List;

public class MyCircle extends Circle {
    private int x;
    private int y;
    static List<MyCircle> circles = new ArrayList<>();
    static Client client;

    final double[] offsetX = {0};
    final double[] offsetY = {0};

    private double lastX = 0;
    private double lastY = 0;

    public void setClient(Client client) {
        this.client = client;
    }

    public MyCircle(int x, int y) {
        super(10);
        this.x = x;
        this.y = y;


        this.setPickOnBounds(true);

        // Press event: capture the offset between mouse and circle center
        this.setOnMousePressed(event -> {
            if(isColor(this.getFill())) return;
                lastX = this.getCenterX();
                lastY = this.getCenterY();
                offsetX[0] = event.getSceneX() - this.getCenterX();
                offsetY[0] = event.getSceneY() - this.getCenterY();

        });

        // Drag event: update the circle's position
        this.setOnMouseDragged(event -> {
            if(!isColor(this.getFill())) return;
            this.setCenterX(event.getSceneX() - offsetX[0]);
            this.setCenterY(event.getSceneY() - offsetY[0]);
        });

        this.setOnMouseReleased(event -> {
            if(!isColor(this.getFill())) return;
            //boolean collisionDetected = false;
            int collisions = 0;
            int targetX = 0;
            int targetY = 0;

            for (MyCircle target : circles) {
                if (isColliding(this, target)&& target!=this) {
                    //collisionDetected = true;
                    //client.sendMove(this.x, this.y, target.getX(), target.getY());
                    collisions++;
                    targetX = target.getX();
                    targetY = target.getY();
                    continue;
                }
            }
            if(collisions == 1){
                //collisionDetected = true;
                //System.out.println("Collision detected");
                client.sendMove(this.x, this.y, targetX, targetY);
            }

            //if (!collisionDetected) {
                this.setCenterX(lastX);
                this.setCenterY(lastY);
            //}
        });
    }

    private boolean isColor(Paint fill) {
        switch (client.myColor) {
            case 'r':
                return fill.equals(Color.RED);
            case 'b':
                return fill.equals(Color.BLUE);
            case 'y':
                return fill.equals(Color.YELLOW);
            case 'g':
                return fill.equals(Color.GREEN);
            case 'o':
                return fill.equals(Color.ORANGE);
            case 'v':
                return fill.equals(Color.PURPLE);
            default:
                return false;
        }
    }

    private boolean isColliding(Circle c1, Circle c2) {
        // Calculate the distance between the centers of the two circles
        double dx = c1.getCenterX() - c2.getCenterX();
        double dy = c1.getCenterY() - c2.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Check if the distance is less than or equal to the sum of the radii
        return distance <= (c1.getRadius() + c2.getRadius());
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void flushCircles() {
        circles.clear();
    }

    public void addCircle() {
        circles.add(this);
    }


}
