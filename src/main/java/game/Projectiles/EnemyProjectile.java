package game.Projectiles;

import game.GameEntity;
import bagel.*;
import game.ShadowAliens;

import java.util.Properties;

/**
 * Projectile fired by an enemy entity.
 */
public class EnemyProjectile extends GameEntity {
    private final int speed;

    /**
     * Constructs an enemy projectile at the given position
     *
     * @param gameProps the loaded game properties file
     * @param x initial x-coordinate
     * @param y initial y-coordinate
     */
    public EnemyProjectile(Properties gameProps, double x, double y) {
        super(new Image(gameProps.getProperty("enemyProjectile.image")), x, y);
        speed = Integer.parseInt(gameProps.getProperty(
                "enemyProjectile.movementSpeed"));
    }

    /**
     * Updates projectile position based on speed
     *
     * @param actualSpeed speed multiplier
     */
    @Override
    public void update(double actualSpeed) {
        setY(getY() + speed * actualSpeed);
    }

    /**
     * Checks whether projectile has gone beyond the bottom of screen.
     *
     * @return true if off-screen
     */
    public boolean isOffScreen() {
        return getY() - getImage().getHeight() / 2.0 > ShadowAliens.screenHeight;
    }
}