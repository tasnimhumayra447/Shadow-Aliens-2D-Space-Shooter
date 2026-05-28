package game.PowerUps;

import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Power-up that grants temporary invincibility.
 */
public class Shield extends PowerUp {
    /**
     * Constructs a shield power-up.
     *
     * @param gameProps game properties file
     * @param x initial x-coordinate
     * @param wave wave number
     * @param index power-up index
     */
    public Shield(Properties gameProps, double x, int wave, int index) {
        super(gameProps, x, "shield", wave, index);
    }

    /**
     * Activates invincibility mode for the player ship.
     *
     * @param playerShip player ship receiving the effect
     */
    @Override
    public void applyEffect(PlayerShip playerShip) {
        playerShip.startInvincibility(getDuration());
    }

    /**
     * Removes invincibility from the player ship.
     *
     * @param playerShip player ship losing the effect
     */
    @Override
    public void removeEffect(PlayerShip playerShip) {
        playerShip.stopInvincibility();
    }
}