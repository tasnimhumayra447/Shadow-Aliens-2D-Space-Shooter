package game.Screens;

import bagel.Font;
import bagel.Input;
import bagel.util.Colour;
import bagel.DrawOptions;
import game.Ships.PlayerShip;
import game.ShadowAliens;

import java.util.Properties;

/**
 * Displays start screen showing the player ship, game title, and instructions
 */
public class StartScreen implements Screen{
    private PlayerShip playerShip;
    private final double actualSpeed = 1.0;

    private final Font titleFont;
    private final String titleText;
    private final double titlePosX;
    private final double titlePosY;
    private final Colour textColour;

    private final String[] instructions;
    private final double instructionsStartPosY;
    private final double instructionsRowGap;
    private final Font instructionsFont;

    /**
     * Creates a new StartScreen, by loading text and layout properties
     *
     * @param gameProps  the loaded game properties file
     * @param playerShip the shared player ship instance
     */
    public StartScreen(Properties gameProps, PlayerShip playerShip) {
        this.playerShip = playerShip;

        titleText = gameProps.getProperty("start.title.text");

        String[] tempColour = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(Double.parseDouble(tempColour[0]),
                Double.parseDouble(tempColour[1]),
                Double.parseDouble(tempColour[2]));

        titlePosY = Double.parseDouble(gameProps.getProperty("start.title.posY"));
        int titleSize = Integer.parseInt(
                gameProps.getProperty("start.title.size"));
        titleFont = new Font(gameProps.getProperty("text.font"), titleSize);

        // Centre title on x axis
        titlePosX = ShadowAliens.screenWidth / 2.0
                - titleFont.getWidth(titleText) / 2.0;

        // Instructions list
        instructions = gameProps.getProperty(
                "start.instructionsList.text").split(",");
        instructionsStartPosY = Double.parseDouble(
                gameProps.getProperty("start.instructionsList.startPosY"));
        instructionsRowGap = Double.parseDouble(
                gameProps.getProperty("start.instructionsList.rowGap"));
        instructionsFont = new Font(gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));
    }

    /**
     * Updates the player ship movement only as no shooting allowed
     *
     * @param input the current frame's input
     */
    @Override
    public void update(Input input) {
        playerShip.update(input, actualSpeed);
    }

    /**
     * Draws player ship, game title, and instructions list.
     */
    @Override
    public void draw() {
        playerShip.draw();
        DrawOptions colourOption = new DrawOptions().setBlendColour(textColour);

        // Draw title
        titleFont.drawString(titleText, titlePosX, titlePosY, colourOption);

        // Draw instructions, each centred on x axis
        for (int i = 0; i < instructions.length; i++) {
            String line = instructions[i];
            double x = ShadowAliens.screenWidth / 2.0
                    - instructionsFont.getWidth(line) / 2.0;
            double y = instructionsStartPosY + i * instructionsRowGap;
            instructionsFont.drawString(line, x, y, colourOption);
        }
    }
}