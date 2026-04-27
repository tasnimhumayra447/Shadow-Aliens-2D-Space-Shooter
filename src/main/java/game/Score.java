package game;
import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
import java.util.Properties;

public class Score {
    private int score;
    private final Colour textColour;
    private final String scoreText;
    private final double scoreX, scoreY;
    private final Font font;

    public Score (Properties gameProps, Font font, Colour colour){
        this.font = font;
        textColour = colour;
        scoreText = gameProps.getProperty("score.text");
        String[] scorePosition = gameProps.getProperty("score.pos").split(",");
        scoreX = Double.parseDouble(scorePosition[0]);
        scoreY = Double.parseDouble(scorePosition[1]);
    }

    public void incrementScore(){
        score ++;
    }

    public void incrementScore(int amount) {
        score += amount;
    }

    public void drawScore(){
        DrawOptions options = new DrawOptions();
        font.drawString(scoreText + " " + score, scoreX, scoreY,
                options.setBlendColour(textColour));
    }

    public void reset() {
        score = 0;
    }
}
