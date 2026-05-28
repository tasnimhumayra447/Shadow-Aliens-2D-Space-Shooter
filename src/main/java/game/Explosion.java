package game;

import bagel.*;
import java.util.Properties;

/**
 * Represents an explosion that exists for a set duration.
 */
public class Explosion extends GameEntity {
    private double duration;

    /**
     * Constructs an explosion at given position using game properties.
     *
     * @param gameProps the loaded game properties file
     * @param x x-coordinate
     * @param y y-coordinate
     * @param large size of explosion (true if large)
     */
    public Explosion(Properties gameProps, double x, double y, boolean large) {
        super(new Image(gameProps.getProperty(
                large ? "explosion.large.image" : "explosion.small.image")), x, y);
        this.duration = Integer.parseInt(gameProps.getProperty(
                large ? "explosion.large.duration" : "explosion.small.duration"));
    }

    /**
     * Updates remaining duration of the explosion.
     *
     * @param actualSpeed speed scaling factor
     */
    @Override
    public void update(double actualSpeed) {
        duration -= actualSpeed;
    }

    public boolean isDurationComplete() {
        return duration <= 0;
    }
}