package game.Screens;

import bagel.*;
import bagel.util.Colour;
import game.ShadowAliens;
import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Displays the end screen in either a win or lose state.
 * The player ship is visible and can move left and right but can't shoot
 */
public class EndScreen implements Screen {
    private final boolean won;
    private final Font winFont;
    private final String winText;
    private final double winPosY;

    private final Font loseFont;
    private final String loseText;
    private final double losePosY;

    private final String[] instructions;
    private final double instructionsStartPosY;
    private final double instructionsRowGap;
    private final Colour textColour;
    private final Font defaultFont;

    private final PlayerShip playerShip;
    private final double actualSpeed = 1.0;
    private final double screenCentreX = ShadowAliens.screenWidth / 2.0;

    /**
     * Creates a new EndScreen for the win/lose outcome.
     *
     * @param gameProps  the loaded game properties file
     * @param playerShip the shared player ship instance
     * @param won        true if the player won, false if they lost
     */
    public EndScreen(Properties gameProps, PlayerShip playerShip, boolean won) {
        this.playerShip = playerShip;
        this.won = won;

        String[] colourParts = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(colourParts[0]),
                Double.parseDouble(colourParts[1]),
                Double.parseDouble(colourParts[2]));

        defaultFont = new Font(
                gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));

        winFont = new Font(
                gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("end.win.size")));
        winText = gameProps.getProperty("end.win.text");
        winPosY = Double.parseDouble(gameProps.getProperty("end.win.posY"));

        loseFont = new Font(
                gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("end.lose.size")));
        loseText = gameProps.getProperty("end.lose.text");
        losePosY = Double.parseDouble(gameProps.getProperty("end.lose.posY"));

        instructions = gameProps.getProperty(
                "end.instructionsList.text").split(",");
        instructionsStartPosY = Double.parseDouble(gameProps.getProperty(
                "end.instructionsList.startPosY"));
        instructionsRowGap = Double.parseDouble(gameProps.getProperty(
                "end.instructionsList.rowGap"));
    }

    /**
     * Updates player ship movement only — no shooting allowed
     *
     * @param input the current frame's input
     */
    @Override
    public void update(Input input) {
        playerShip.update(input, actualSpeed);
    }

    /**
     * Draws player ship, win or lose title, and instructions list.
     */
    @Override
    public void draw() {
        playerShip.draw();

        DrawOptions options = new DrawOptions().setBlendColour(textColour);

        if (won) {
            double x = screenCentreX - winFont.getWidth(winText) / 2.0;
            winFont.drawString(winText, x, winPosY, options);
        } else {
            double x = screenCentreX - loseFont.getWidth(loseText) / 2.0;
            loseFont.drawString(loseText, x, losePosY, options);
        }

        // Instructions shown in both states
        for (int i = 0; i < instructions.length; i++) {
            String line = instructions[i];
            double x = screenCentreX - defaultFont.getWidth(line) / 2.0;
            double y = instructionsStartPosY + i * instructionsRowGap;
            defaultFont.drawString(line, x, y, options);
        }
    }
}
