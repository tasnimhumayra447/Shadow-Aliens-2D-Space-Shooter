package game.Ships;

import java.util.Properties;

public class RegularEnemy extends EnemyShip {
    private final int scoreValue;

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