package game;
import bagel.*;

import java.util.Properties;

public class PlayerShip extends Ship{
    private final double initialX;
    private final double initialY;
    private final double speed;
    private final int coolDown;
    private double coolDownTimer = 0;

    public PlayerShip(Properties gameProps){
        initialX = ShadowAliens.screenWidth / 2.0;
        initialY = Double.parseDouble(gameProps.getProperty("player.posY"));
        super.setX(initialX);
        super.setY(initialY);
        super.setShipImage(new Image (gameProps.getProperty("player.image")));
        speed = Integer.parseInt(gameProps.getProperty("player.speed"));
        coolDown = Integer.parseInt(gameProps.getProperty("player.shootCooldown"));
    }

    @Override
    public void drawShip() {
        super.getShipImage().draw(super.getX(), super.getY());
    }

    public boolean canShoot(){
        return coolDownTimer == 0;
    }

    public void resetCoolDownTimer(){
        coolDownTimer = coolDown;
    }

    private void moveShip(Input input, double actualSpeed){
        boolean movingLeft = input.isDown(Keys.A) || input.wasPressed(Keys.A);
        boolean movingRight = input.isDown(Keys.D) || input.wasPressed(Keys.D);

        if (movingLeft && !movingRight) super.setX(super.getX() - speed*actualSpeed);
        if (movingRight && !movingLeft) super.setX(super.getX() + speed*actualSpeed);

        // check if ship outside boundaries
        double halfWidth = super.getShipImage().getWidth() / 2.0;
        if (super.getX() - halfWidth < 0 ){
            // left edge is now at x = 0
            super.setX(halfWidth);
        } else if (super.getX() + halfWidth > ShadowAliens.screenWidth) {
            // right edge is now at screenWidth
            super.setX(ShadowAliens.screenWidth - halfWidth);
        }

        if (coolDownTimer > 0) coolDownTimer -= actualSpeed;
        if (coolDownTimer < 0) coolDownTimer = 0;
    }

    public void update(Input input, double actualSpeed) {
        moveShip(input, actualSpeed);
    }

    public void reset(){
        super.setX(initialX);
        super.setY(initialY);
        coolDownTimer = 0;
    }
}
