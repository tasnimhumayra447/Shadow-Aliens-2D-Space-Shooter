package game;
import bagel.*;
import java.util.Properties;

public class Explosion {
    private double duration;
    private final double explosionX;
    private final double explosionY;
    private boolean destroyed = false;
    private final Image explosionImage;

    public Explosion(Properties gameProps, double enemyX, double enemyY ){
        explosionX = enemyX;
        explosionY = enemyY;
        duration = Integer.parseInt(gameProps.getProperty("explosion.duration"));
        explosionImage = new Image (gameProps.getProperty("explosion.image"));
    }

    public void drawExplosion() {
        explosionImage.draw(explosionX, explosionY);
    }

    public void decrementDuration(double actualSpeed){
        duration -= actualSpeed;
        if (duration <= 0){
            destroyed = true;
        }
    }

    public void update(double actualSpeed){
        if (destroyed){
            return;
        }
        decrementDuration(actualSpeed);
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
