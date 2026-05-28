package game;

import bagel.*;
import java.util.Properties;

/**
 * Manages and displays the player's remaining lives.
 */
public class Lives {
    private int lives;
    private final int initialLives;
    private final double livesX, livesY, livesGap;
    private final Image livesImage;

    /**
     * Constructs the lives display using game properties.
     *
     * @param gameProps loaded game properties file
     */
    public Lives(Properties gameProps) {
        String[] livesPosition = gameProps.getProperty(
                "playerLives.startPosition").split(",");
        initialLives = Integer.parseInt(
                gameProps.getProperty("player.initialLives"));
        lives = initialLives;
        livesX = Double.parseDouble(livesPosition[0]);
        livesY = Double.parseDouble(livesPosition[1]);
        livesGap = Double.parseDouble(gameProps.getProperty("playerLives.gap"));
        livesImage = new Image(gameProps.getProperty("playerLives.image"));
    }

    /**
     * Draws the current number of lives on screen.
     */
    public void drawLives() {
        for (int i = 0; i < lives; i++) {
            livesImage.draw(livesX + livesGap * i, livesY);
        }
    }

    public void decrement() {
        if (lives > 0) {
            lives--;
        }
    }

    public void increment() {
        if (lives < initialLives) {
            lives++;
        }
    }

    public int getLives() {
        return lives;
    }

    /**
     * Restores lives to the initial amount.
     */
    public void reset() {
        lives = initialLives;
    }
}