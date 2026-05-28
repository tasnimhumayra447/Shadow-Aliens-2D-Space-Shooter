package game;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Manages and displays the player's score.
 */
public class Score {
    private int score;
    private final Colour textColour;
    private final String scoreText;
    private final double scoreX, scoreY;
    private final Font font;

    /**
     * Constructs a score display using game properties file
     *
     * @param gameProps loaded properties file
     * @param font font used to render the score
     * @param colour colour used to render the score text
     */
    public Score(Properties gameProps, Font font, Colour colour) {
        this.font = font;
        textColour = colour;
        scoreText = gameProps.getProperty("score.text");
        String[] scorePosition = gameProps.getProperty("score.pos").split(",");
        scoreX = Double.parseDouble(scorePosition[0]);
        scoreY = Double.parseDouble(scorePosition[1]);
        score = 0;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public void decrementScore(int amount) {
        score = Math.max(0, score - amount);
    }

    /**
     * Draws the current score on the screen.
     */
    public void drawScore() {
        font.drawString(scoreText + " " + score, scoreX, scoreY,
                new DrawOptions().setBlendColour(textColour));
    }
}