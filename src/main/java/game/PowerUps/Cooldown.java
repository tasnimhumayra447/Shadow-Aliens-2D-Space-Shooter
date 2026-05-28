package game.PowerUps;

import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Power-up that temporarily reduces projectile cooldown time.
 */
public class Cooldown extends PowerUp {

    /**
     * Constructs a cooldown reduction power-up.
     *
     * @param gameProps loaded game properties file
     * @param x initial x-coordinate
     * @param wave wave number
     * @param index power-up index
     */
    public Cooldown(Properties gameProps, double x, int wave, int index) {
        super(gameProps, x, "cooldown", wave, index);
    }

    /**
     * Applies cooldown reduction to player ship.
     *
     * @param playerShip player ship receiving the effect
     */
    @Override
    public void applyEffect(PlayerShip playerShip) {
        playerShip.applyCooldownReduction();
    }

    /**
     * Restores player's normal cooldown value.
     *
     * @param playerShip player ship losing the effect
     */
    @Override
    public void removeEffect(PlayerShip playerShip) {
        playerShip.resetCooldown();
    }
}