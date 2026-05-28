package game.Ships;

import java.util.Properties;

/**
 * Basic enemy ship with default movement and no special behaviour.
 */
public class RegularEnemy extends EnemyShip {
    private final int scoreValue;

    /**
     * Constructs a regular enemy for a given wave and index.
     *
     * @param gameProps loaded game properties file
     * @param wave wave number
     * @param index enemy index within wave
     */
    public RegularEnemy(Properties gameProps, int wave, int index) {
        super(gameProps, wave, index, "regular");
        scoreValue = Integer.parseInt(
                gameProps.getProperty("score.destroyedEnemy.regular"));
    }

    @Override
    public int getScore(){
        return scoreValue;
    }
}