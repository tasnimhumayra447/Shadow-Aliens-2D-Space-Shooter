package game.Ships;

import bagel.*;
import game.Projectiles.EnemyProjectile;
import game.ShadowAliens;

import java.util.Properties;

/**
 * Abstract base class for all enemy ships.
 * Handles arrival, movement, off-screen detection,
 */
public abstract class EnemyShip extends Ship {
    private final int arrivalTime;
    private boolean active = false;
    private double frameCount = 0;

    /**
     * Constructs an EnemyShip with position, speed, and arrival time
     * loaded from the properties file for the given wave and index.
     *
     * @param gameProps the loaded game properties file
     * @param wave      the wave number this enemy belongs to
     * @param index     the index of this enemy within the wave
     * @param type      the enemy type string
     */
    public EnemyShip(Properties gameProps, int wave, int index, String type) {
        super(new Image(gameProps.getProperty("enemy." + type + ".image")),
                Double.parseDouble(gameProps.getProperty(
                        "wave." + wave + ".enemy." + index + ".posX")),
                -new Image(gameProps.getProperty(
                        "enemy." + type + ".image")).getHeight() / 2.0,
                Integer.parseInt(gameProps.getProperty(
                        "wave." + wave + ".enemy." + index + ".movementSpeed")));

        arrivalTime = Integer.parseInt(gameProps.getProperty(
                "wave." + wave + ".enemy." + index + ".arrivalTime"));
    }

    /**
     * Advances  frame count and moves enemy downward once arrived
     *
     * @param actualSpeed the current speed multiplier
     */
    @Override
    public void update(double actualSpeed) {
        frameCount += actualSpeed;

        if (!hasArrived()) return;

        active = true;

        setY(getY() + getSpeed() * actualSpeed);
    }

    /**
     * Draws the enemy ship once arrived
     */
    @Override
    public void draw() {
        DrawOptions options = new DrawOptions();
        options.setRotation(Math.toRadians(90));  // rotate 90° clockwise

        if (active) getImage().draw(getX(), getY(), options);
    }

    /**
     * Returns true if the enemy has moved below bottom of screen
     *
     * @return true if the enemy is off screen
     */
    public boolean isOffScreen() {
        return getY() - getImage().getHeight() / 2.0
                > ShadowAliens.screenHeight;
    }

    /**
     * creates the correct EnemyShip
     * based on the type string in the properties file.
     *
     * @param gameProps the loaded game properties file
     * @param wave      the wave number
     * @param index     the enemy index within the wave
     * @return a new RegularEnemy, StrafingEnemy, or ShootingEnemy
     */
    public static EnemyShip create(Properties gameProps, int wave, int index) {
        String type = gameProps.getProperty
                ("wave." + wave + ".enemy." + index + ".type");
        switch (type) {
            case "strafing": return new StrafingEnemy(gameProps, wave, index);
            case "shooting": return new ShootingEnemy(gameProps, wave, index);
            default: return new RegularEnemy(gameProps, wave, index);
        }
    }

    protected boolean hasArrived() {
        return frameCount >= arrivalTime;
    }

    /**
     * Tries to fire a projectile this frame.
     *
     * @param actualSpeed the current speed multiplier
     * @return a new EnemyProjectile, or null if no shot is fired
     */
    public EnemyProjectile shoot(double actualSpeed) {
        return null;
    }

    public abstract int getScore();
}