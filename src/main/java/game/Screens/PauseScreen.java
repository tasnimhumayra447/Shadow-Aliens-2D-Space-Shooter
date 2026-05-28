package game.Screens;

import bagel.*;
import bagel.util.Colour;
import game.ShadowAliens;
import java.util.Properties;

/**
 * Displays a frozen BattleScreen.
 * No entities move or change while this screen is active.
 * Displays paused title, controls list, and current game timescale.
 * Dev-mode controls remain active and are handled by ShadowAliens.
 */

public class PauseScreen implements Screen {
    private final Font titleFont;
    private final String titleText;
    private final Colour textColour;
    private final double titleX;
    private final double titleY;

    private final Font defaultFont;

    private final String[] controls;
    private final double controlsStartY;
    private final double rowGap;

    private final String timescaleText;
    private final double timescaleX;
    private final double timescaleY;
    private int timeScale;
    private double actualSpeed;

    private final BattleScreen battleScreen;

    /**
     * Creates a new PauseScreen overlaying the given BattleScreen.
     *
     * @param gameProps    the loaded game properties file
     * @param battleScreen the active BattleScreen to draw frozen underneath
     * @param timeScale    the current integer timescale
     * @param actualSpeed  current speed multiplier
     */
    public PauseScreen(Properties gameProps, BattleScreen battleScreen,
                       int timeScale, double actualSpeed) {
        this.battleScreen = battleScreen;
        this.timeScale = timeScale;
        this.actualSpeed = actualSpeed;

        String[] colourParts = gameProps.getProperty("text.colour").split(",");
        textColour = new Colour(
                Double.parseDouble(colourParts[0].trim()),
                Double.parseDouble(colourParts[1].trim()),
                Double.parseDouble(colourParts[2].trim()));

        titleText = gameProps.getProperty("pausedTitle.text");
        titleY    = Double.parseDouble(
                gameProps.getProperty("pausedTitle.posY"));
        titleFont = new Font(gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("pausedTitle.size")));
        titleX    = ShadowAliens.screenWidth / 2.0 -
                titleFont.getWidth(titleText) / 2.0;

        controls       = gameProps.getProperty("controlsList.text").split(",");
        controlsStartY = Double.parseDouble(gameProps.getProperty(
                "controlsList.startPosY"));
        rowGap         = Double.parseDouble(gameProps.getProperty(
                "controlsList.rowGap"));
        defaultFont    = new Font(gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));

        timescaleText  = gameProps.getProperty("timescale.text");
        String[] timescalePos = gameProps.getProperty(
                "timescale.pos").split(",");
        timescaleX = Double.parseDouble(timescalePos[0].trim());
        timescaleY = Double.parseDouble(timescalePos[1].trim());
    }

    /**
     * No entity updates occur while paused as time is frozen.
     * Dev-mode controls are handled by ShadowAliens, not here.
     *
     * @param input the current frame's input
     */
    @Override
    public void update(Input input) {
        // Time is frozen — no entity updates
    }

    /**
     * Draws the frozen BattleScreen underneath, then renders the pause overlay
     */
    @Override
    public void draw() {
        // Draw frozen battle screen
        battleScreen.draw();

        DrawOptions options = new DrawOptions().setBlendColour(textColour);

        // Paused title
        titleFont.drawString(titleText, titleX, titleY, options);

        // Controls list
        for (int i = 0; i < controls.length; i++) {
            String line = controls[i].trim();
            double x = ShadowAliens.screenWidth / 2.0 -
                    defaultFont.getWidth(line) / 2.0;
            double y = controlsStartY + i * rowGap;
            defaultFont.drawString(line, x, y, options);
        }

        String displayScale = timeScale >= 1
                ? String.valueOf(timeScale)
                : String.format("%.2f", actualSpeed);
        defaultFont.drawString(timescaleText + " " + displayScale,
                timescaleX, timescaleY, options);
    }

    /**
     * Updates the displayed timescale and speed, called by ShadowAliens
     * whenever G or F is pressed while paused.
     *
     * @param timeScale   the new integer timescale
     * @param actualSpeed the new fractional speed multiplier
     */
    public void setTimeScale(int timeScale, double actualSpeed) {
        this.timeScale   = timeScale;
        this.actualSpeed = actualSpeed;
    }

    /**
     * Returns the underlying BattleScreen so ShadowAliens can restore it
     * as the current screen when player unpauses.
     *
     * @return the BattleScreen this PauseScreen was created from
     */
    public BattleScreen getBattleScreen() {
        return battleScreen;
    }
}