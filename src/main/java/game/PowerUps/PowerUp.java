package game.PowerUps;

import bagel.*;
import game.GameEntity;
import game.ShadowAliens;
import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Abstract base class for powerups.
 * Handles movement, activation, and effects.
 */
public abstract class PowerUp extends GameEntity {
    private final int arrivalTime;
    private final int speed;
    private final int duration;
    private double frameCount = 0;
    private double durationTimer = 0;
    private boolean active = false;

    /**
     * Constructs a powerup using game properties
     *
     * @param gameProps loaded game properties file
     * @param x initial x-coordinate
     * @param type powerup type string
     * @param wave wave number containing the power-up
     * @param index index of the power-up in the wave
     */
    public PowerUp(Properties gameProps, double x,
                   String type, int wave, int index) {
        super(new Image(gameProps.getProperty(
                "powerup." + type + ".image")), x, 0);

        arrivalTime = Integer.parseInt(gameProps.getProperty(
                "wave." + wave + ".powerup." + index + ".arrivalTime"));
        speed = Integer.parseInt(gameProps.getProperty(
                "powerup." + type + ".movementSpeed"));
        duration = gameProps.getProperty("powerup." + type + ".duration") != null
                ? Integer.parseInt(
                        gameProps.getProperty("powerup." + type + ".duration"))
                : 0;
    }

    /**
     * Updates the powerup movement and duration.
     *
     * @param actualSpeed speed scaling factor
     */
    @Override
    public void update(double actualSpeed) {
        frameCount += actualSpeed;

        if (frameCount < arrivalTime) return;

        if (!active) {
            setY(getY() + speed * actualSpeed);
        } else {
            durationTimer += actualSpeed;
            if (durationTimer >= duration) {
                active = false;
            }
        }
    }

    /**
     * Draws the powerup if it has arrived and is active
     */
    @Override
    public void draw() {
        if (!active && frameCount >= arrivalTime) {
            super.draw();
        }
    }

    /**
     * Checks whether the powerup has gone below screen.
     *
     * @return true if off-screen
     */
    public boolean isOffScreen() {
        return getY() - getImage().getHeight() / 2.0 > ShadowAliens.screenHeight;
    }

    /**
     * Marks the powerup as collected and starts its duration timer
     */
    public void markCollected() {
        active = true;
        durationTimer = 0;
    }

    public boolean isActive()  {
        return active;
    }

    /**
     * Creates a powerup instance
     *
     * @param gameProps loaded properties file
     * @param wave wave number
     * @param index power-up index within the wave
     * @return created powerup instance
     */
    public static PowerUp create(Properties gameProps, int wave, int index) {
        String type = gameProps.getProperty(
                "wave." + wave + ".powerup." + index + ".type");
        double posX = Double.parseDouble(gameProps.getProperty(
                "wave." + wave + ".powerup." + index + ".posX"));
        switch (type) {
            case "shield":   return new Shield(gameProps, posX, wave, index);
            case "cooldown": return new Cooldown(gameProps, posX, wave, index);
            case "engine":   return new Engine(gameProps, posX, wave, index);
            default:         return new Life(gameProps, posX, wave, index);
        }
    }

    /**
     * Applies the powerup effect to the player ship.
     *
     * @param playerShip player ship receiving the effect
     */
    public abstract void applyEffect(PlayerShip playerShip);

    /**
     * Removes the powerup effect from the player ship.
     *
     * @param playerShip player ship losing the effect
     */
    public abstract void removeEffect(PlayerShip playerShip);

    protected int getDuration() { return duration; }
}