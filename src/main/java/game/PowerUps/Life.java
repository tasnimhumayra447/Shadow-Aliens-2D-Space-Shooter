package game.PowerUps;

import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Power-up that grants one player life.
 */
public class Life extends PowerUp {

    /**
     * Constructs a life power-up.
     *
     * @param gameProps game properties file
     * @param x initial x-coordinate
     * @param wave wave number
     * @param index power-up index
     */
    public Life(Properties gameProps, double x, int wave, int index) {
        super(gameProps, x, "life", wave, index);
    }

    /**
     * Increases the player's lives by one.
     *
     * @param playerShip player ship receiving the effect
     */
    @Override
    public void applyEffect(PlayerShip playerShip) {
        playerShip.getLives().increment();
    }

    /**
     * Removes the power-up effect.
     * Life power-ups have no removable effect.
     *
     * @param playerShip player ship
     */
    @Override
    public void removeEffect(PlayerShip playerShip) {
        // Life has no duration, so nothing to remove
    }
}