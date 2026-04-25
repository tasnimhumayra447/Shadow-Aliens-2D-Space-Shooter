package game;
import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Properties;

public class PlayerShip {
    private final double initialX;
    private final double initialY;
    private double playerX, playerY;
    private final double speed;
    private final Image playerImage;
    private final int coolDown;
    private int coolDownTimer = 0;

    public PlayerShip(Properties gameProps){
        initialX = ShadowAliens.screenWidth / 2.0;
        initialY = Double.parseDouble(gameProps.getProperty("player.posY"));
        playerX = initialX;
        playerY = initialY;

        playerImage = new Image (gameProps.getProperty("player.image"));
        speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        coolDown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
    }

    public void drawShip() {
        playerImage.draw(playerX, playerY);
    }

    public boolean canShoot(){
        return coolDownTimer == 0;
    }

    public void resetCoolDownTimer(){
        coolDownTimer = coolDown;
    }

    public void moveShip(Input input){
        boolean movingLeft = input.isDown(Keys.A) || input.wasPressed(Keys.A);
        boolean movingRight = input.isDown(Keys.D) || input.wasPressed(Keys.D);

        if (movingLeft && !movingRight) playerX -= speed;
        if (movingRight && !movingLeft) playerX += speed;

        // check if ship outside boundaries
        double halfWidth = playerImage.getWidth() / 2.0;
        if (playerX - halfWidth < 0 ){
            // left edge is now at x = 0
            playerX = halfWidth;
        } else if (playerX + halfWidth > ShadowAliens.screenWidth) {
            // right edge is now at screenWidth
            playerX = ShadowAliens.screenWidth - halfWidth;
        }

        if (coolDownTimer > 0) coolDownTimer--;
    }

    public void update(Input input) {
        moveShip(input);
    }

    public double getX() {
        return playerX;
    }

    public double getY() {
        return playerY;
    }

    public Rectangle getBoundingBoxAt() {
        return playerImage.getBoundingBoxAt(new Point(playerX, playerY));
    }

    public void reset(){
        playerX = initialX;
        playerY = initialY;
        coolDownTimer = 0;
    }

}
