package game.PowerUps;

import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Power-up that temporarily increases player movement speed.
 */
public class Engine extends PowerUp {
    /**
     * Constructs an engine power-up.
     *
     * @param gameProps game properties file
     * @param x initial x-coordinate
     * @param wave wave number
     * @param index power-up index
     */
    public Engine(Properties gameProps, double x, int wave, int index) {
        super(gameProps, x, "engine", wave, index);
    }

    /**
     * Applies a speed boost to the player ship.
     *
     * @param playerShip player ship receiving the effect
     */
    @Override
    public void applyEffect(PlayerShip playerShip) {
        playerShip.applySpeedBoost();
    }

    /**
     * Restores the player's normal movement speed.
     *
     * @param playerShip player ship losing the effect
     */
    @Override
    public void removeEffect(PlayerShip playerShip) {
        playerShip.resetSpeed();
    }
}