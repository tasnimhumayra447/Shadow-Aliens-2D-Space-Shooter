package game.Ships;

import game.Projectiles.EnemyProjectile;

import java.util.Properties;

/**
 * Enemy ship that moves normally and can fire projectiles
 */
public class ShootingEnemy extends EnemyShip {
    private final int firingRate;
    private double firingTimer = 0;
    private final Properties gameProps;

    /**
     * Constructs a shooting enemy for a given wave and index.
     *
     * @param gameProps loaded game properties file
     * @param wave wave number
     * @param index enemy index within wave
     */
    public ShootingEnemy(Properties gameProps, int wave, int index) {
        super(gameProps, wave, index, "shooting");
        this.gameProps  = gameProps;
        this.firingRate = Integer.parseInt(
                gameProps.getProperty("enemy.shooting.firingRate"));
    }

    /**
     * Generates enemy projectiles at a fixed firing rate after arrival
     *
     * @param actualSpeed the current speed multiplier
     * @return new projectile if fired, otherwise null
     */
    @Override
    public EnemyProjectile shoot(double actualSpeed) {
        if (!hasArrived()) return null;

        firingTimer += actualSpeed;

        // First shot at arrivalTime + firingRate, then every firingRate frames
        if (firingTimer >= firingRate) {
            firingTimer = 0;
            return new EnemyProjectile(gameProps, getX(), getY());
        }
        return null;
    }

    @Override
    public int getScore(){
        return Integer.parseInt(
                gameProps.getProperty("score.destroyedEnemy.shooting"));
    }
}
