package game;

import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Properties;
import java.lang.Math;

public class EnemyShip {
    private final Image enemyImage;
    private double enemyX, enemyY;
    private final int arrivalTime;
    private final int speed;
    private boolean active = false;
    private boolean destroyed = false;

    public EnemyShip(Properties gameProps, int index){
        enemyX = Double.parseDouble(gameProps.getProperty("enemy." + index + ".posX"));
        speed = Integer.parseInt(gameProps.getProperty("enemy." + index + ".movementSpeed"));
        enemyImage = new Image (gameProps.getProperty("enemy.image"));

        // start just above screen, not visible
        enemyY = -enemyImage.getHeight()/2.0;
        arrivalTime = Integer.parseInt(gameProps.getProperty("enemy." + index + ".arrivalTime"));
    }

    public void drawShip() {
        DrawOptions options = new DrawOptions();
        options.setRotation(Math.toRadians(90));  // rotate 90° clockwise
        enemyImage.draw(enemyX, enemyY, options);
    }

    public void update(double frameCount){
        if (destroyed){
            return;
        }

        // activate when real game frame matches arrival time
        if (frameCount >= arrivalTime) {
            active = true;
        }

        if (active){
            enemyY += speed;
        }

        // destroy if fully off bottom
        if (enemyY - enemyImage.getHeight() / 2.0 > ShadowAliens.screenHeight) {
            destroyed = true;
            return;
        }
    }

    public double getX() {
        return enemyX;
    }

    public double getY() {
        return enemyY;
    }

    public void setDestroyed(){
        destroyed = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public Rectangle getBoundingBoxAt() {
        return enemyImage.getBoundingBoxAt(new Point(enemyX, enemyY));
    }
}
