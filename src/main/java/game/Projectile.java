package game;
import bagel.*;
import bagel.util.*;
import java.util.Properties;

public class Projectile {
    private final Image projectileImage;
    private double projectileX, projectileY;
    private final int speed;
    private boolean destroyed = false;

    public Projectile(Properties gameProps, double projectileX, double projectileY){
        projectileImage = new Image(gameProps.getProperty("projectile.image"));
        speed = Integer.parseInt(gameProps.getProperty("projectile.movementSpeed"));
        this.projectileX = projectileX;
        this.projectileY = projectileY;
    }

    public void drawProjectile() {
        projectileImage.draw(projectileX, projectileY);
    }

    public boolean isDestroyed(){
        return destroyed;
    }

    public void update(){
        if (destroyed){
            return;     // nothing to do
        }

        projectileY -= speed;


        // fully off top when bottom edge < 0
        if (projectileY + projectileImage.getHeight() / 2.0 < 0) {
            destroyed = true;
            return;
        }
    }

    public double getX() {
        return projectileX;
    }

    public double getY() {
        return projectileY;
    }

    public void setDestroyed(){
        destroyed = true;
    }

    public Rectangle getBoundingBoxAt() {
        return projectileImage.getBoundingBoxAt(new Point(projectileX, projectileY));
    }


}
