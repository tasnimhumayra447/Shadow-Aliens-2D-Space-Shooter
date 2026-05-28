package game.Projectiles;

import game.GameEntity;
import bagel.*;
import java.util.Properties;

/**
 * Projectile fired by the player.
 * Moves upward at a fixed speed
 */
public class PlayerProjectile extends GameEntity {
    private final int speed;

    /**
     * Constructs a player projectile at given position using game properties
     *
     * @param gameProps the loaded game properties file
     * @param x initial x-coordinate
     * @param y initial y-coordinate
     */
    public PlayerProjectile(Properties gameProps, double x, double y) {
        super(new Image(gameProps.getProperty("projectile.image")), x, y);
        speed = Integer.parseInt(gameProps.getProperty("projectile.movementSpeed"));
    }

    /**
     * Updates projectile position based on speed and frame
     *
     * @param actualSpeed speed scaling factor
     */
    @Override
    public void update(double actualSpeed) {
        setY(getY() - speed * actualSpeed);
    }

    /**
     * Checks whether projectile has gone beyond the top of screen
     *
     * @return true if off-screen, otherwise false
     */
    public boolean isOffScreen() {
        return getY() + getImage().getHeight() / 2.0 < 0;
    }
}