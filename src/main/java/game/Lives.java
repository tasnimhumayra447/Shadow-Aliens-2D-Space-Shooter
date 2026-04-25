package game;
import bagel.*;
import java.util.Properties;

public class Lives {
    private int lives;
    private final int initialLives;
    private final double livesX, livesY, livesGap;
    private final Image livesImage;

    public Lives(Properties gameProps){
        String[] livesPosition = gameProps.getProperty("playerLives.startPosition").split(",");
        initialLives = Integer.parseInt(gameProps.getProperty("player.initialLives"));
        lives = initialLives;
        livesX = Double.parseDouble(livesPosition[0]);
        livesY = Double.parseDouble(livesPosition[1]);
        livesGap = Integer.parseInt(gameProps.getProperty("playerLives.gap"));
        livesImage = new Image (gameProps.getProperty("playerLives.image"));
    }

    public void drawLives() {
        for (int i = 0; i < lives; i++) {
            livesImage.draw(livesX + livesGap * i, livesY);
        }
    }

    public void decrementLives(){
        lives --;
    }

    public int getLives(){
        return lives;
    }

    public void reset(){
        lives = initialLives;
    }
}
