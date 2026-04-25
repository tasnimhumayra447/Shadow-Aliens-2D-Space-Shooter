package game;
import bagel.DrawOptions;
import bagel.util.Colour;
import bagel.Font;
import java.util.Properties;

public class PauseScreen {
    // title
    private final Font titleFont;
    private final String titleText;
    private final Colour textColour;
    private final double titleX;
    private final double titleY;
    private final int titleSize;

    private final Font defaultFont;
    private final String[] controls;
    private final double controlsStartY;
    private final double rowGap;

    private final String timescaleText;
    private final double timescaleX;
    private final double timescaleY;

    public PauseScreen(Properties gameProps, Colour colour){
        titleText = gameProps.getProperty("pausedTitle.text");
        textColour = colour;
        titleY = Double.parseDouble(gameProps.getProperty("pausedTitle.posY"));
        titleSize = Integer.parseInt(gameProps.getProperty("pausedTitle.size"));
        titleFont = new Font(gameProps.getProperty("text.font"), titleSize);

        // centre of text lands on centre of screen
        titleX = ShadowAliens.screenWidth / 2.0 - titleFont.getWidth(titleText) / 2.0;

        // controls list
        controls = gameProps.getProperty("controlsList.text").split(",");
        controlsStartY = Double.parseDouble(gameProps.getProperty("controlsList.startPosY"));
        rowGap = Double.parseDouble(gameProps.getProperty("controlsList.rowGap"));
        defaultFont = new Font(gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));

        // timescale
        timescaleText = gameProps.getProperty("timescale.text");
        String[] timescalePos = gameProps.getProperty("timescale.pos").split(",");
        timescaleX = Double.parseDouble(timescalePos[0]);
        timescaleY = Double.parseDouble(timescalePos[1]);
    }


    public void draw(int timeScale, double actualSpeed) {
        // draw title centred
        titleFont.drawString(titleText, titleX, titleY, new DrawOptions().setBlendColour(textColour));

        // draw controls list
        for (int i = 0; i < controls.length; i++) {
            double x = ShadowAliens.screenWidth / 2.0 - defaultFont.getWidth(controls[i]) / 2.0;
            double y = controlsStartY + i * rowGap;
            defaultFont.drawString(controls[i], x, y, new DrawOptions().setBlendColour(textColour));
        }

        // draw timescale
        DrawOptions options = new DrawOptions();
        String displayScale = timeScale > 0 ? String.valueOf(timeScale) : String.format("%.2f", actualSpeed);
        defaultFont.drawString(timescaleText + " " + displayScale, timescaleX, timescaleY,
                options.setBlendColour(textColour));
    }
}
