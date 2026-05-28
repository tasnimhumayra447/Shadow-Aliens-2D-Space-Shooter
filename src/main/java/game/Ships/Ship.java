package game.Ships;

import bagel.Image;
import game.GameEntity;

/**
 * Abstract base class representing a ship entity
 * Stores common position, speed, and initial state for all ships.
 */
public abstract class Ship extends GameEntity {
    private double speed;
    private final double initialX;
    private final double initialY;

    /**
     * Constructs a ship with an image, position, and movement speed.
     *
     * @param image ship image
     * @param x initial x-coordinate
     * @param y initial y-coordinate
     * @param speed movement speed
     */
    public Ship(Image image, double x, double y, double speed) {
        super(image, x, y);
        this.speed = speed;
        this.initialX = x;
        this.initialY = y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getInitialX() {
        return initialX;
    }
    public double getInitialY() {
        return initialY;
    }
}