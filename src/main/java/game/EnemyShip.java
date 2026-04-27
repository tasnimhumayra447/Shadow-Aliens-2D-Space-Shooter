package game;

import bagel.DrawOptions;
import bagel.Image;

import java.util.Properties;

public class EnemyShip extends Ship{
    private final int arrivalTime;
    private final int speed;
    private boolean active = false;
    private boolean destroyed = false;

    public EnemyShip(Properties gameProps, int index){
        super.setX(Double.parseDouble(
                gameProps.getProperty("enemy." + index + ".posX")));
        speed = Integer.parseInt(
                gameProps.getProperty("enemy." + index + ".movementSpeed"));
        super.setShipImage(new Image (gameProps.getProperty("enemy.image")));

        // start just above screen, not visible
        super.setY(-super.getShipImage().getHeight()/2.0);
        arrivalTime = Integer.parseInt(
                gameProps.getProperty("enemy." + index + ".arrivalTime"));
    }

    @Override
    public void drawShip() {
        DrawOptions options = new DrawOptions();
        options.setRotation(Math.toRadians(90));  // rotate 90° clockwise
        super.getShipImage().draw(super.getX(), super.getY(), options);
    }


    public void update(double frameCount, double actualSpeed){
        if (destroyed){
            return;
        }

        // activate when real game frame matches arrival time
        if (frameCount >= arrivalTime) {
            active = true;
        }

        if (active){
            super.setY(super.getY() + speed * actualSpeed);
        }

        // destroy if fully off bottom
        if (super.getY() - super.getShipImage().getHeight() / 2.0
                > ShadowAliens.screenHeight) {
            destroyed = true;
            return;
        }
    }

    public void setDestroyed(){
        destroyed = true;
    }

    public boolean isDestroyed(){
        return destroyed;
    }
}
