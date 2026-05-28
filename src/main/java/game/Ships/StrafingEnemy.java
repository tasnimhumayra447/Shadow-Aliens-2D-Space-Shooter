package game.Ships;

import game.ShadowAliens;

import java.util.Properties;

/**
 * enemy moves along x axis at the same time as it moves down y axis
 * while bouncing between screen edges.
 */
public class StrafingEnemy extends EnemyShip {
    private int directionX = 0;
    private final int scoreValue;

    /**
     * Constructs a strafing enemy for a given wave and index.
     *
     * @param gameProps game properties file
     * @param wave wave number
     * @param index enemy index within wave
     */
    public StrafingEnemy(Properties gameProps, int wave, int index) {
        super(gameProps, wave, index, "strafing");
        scoreValue = Integer.parseInt(
                gameProps.getProperty("score.destroyedEnemy.strafing"));
    }

    /**
     * Updates enemy movement.
     * Moves vertically until arrival, then moves horizontally also
     * and bounces off screen edges.
     *
     * @param actualSpeed speed scaling factor
     */
    @Override
    public void update(double actualSpeed) {
        if (!hasArrived()) {
            // Still increase frameCount via parent
            super.update(actualSpeed);
            return;
        }

        // On first arrival, determine closest edge
        if (directionX == 0) {
            directionX = getX() < ShadowAliens.screenWidth / 2.0 ? -1 : 1;
        }

        // Move down (via parent)
        super.update(actualSpeed);

        // Move horizontally
        double halfWidth = getImage().getWidth() / 2.0;
        double newX = getX() + directionX * getSpeed() * actualSpeed;

        // Bounce off edges
        if (newX - halfWidth <= 0) {
            newX = halfWidth;
            directionX = 1;
        } else if (newX + halfWidth >= ShadowAliens.screenWidth) {
            newX = ShadowAliens.screenWidth - halfWidth;
            directionX = -1;
        }

        setX(newX);
    }

    @Override
    public int getScore(){
        return scoreValue;
    }
}