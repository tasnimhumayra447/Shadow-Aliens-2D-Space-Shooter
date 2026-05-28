package game;

import bagel.*;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Manages and displays the current game wave.
 */
public class Wave {
    private int currentWave;
    private final Font font;
    private final Colour colour;
    private final String waveText;
    private final double waveX, waveY;

    /**
     * Constructs a wave display using game properties file
     *
     * @param gameProps loaded properties file
     * @param font font used to render the wave text
     * @param colour colour used to render the wave text
     */
    public Wave(Properties gameProps, Font font, Colour colour) {
        String[] wavePos = gameProps.getProperty("wave.pos").split(",");
        this.font = font;
        this.colour = colour;
        this.waveText = gameProps.getProperty("wave.text");
        this.waveX = Double.parseDouble(wavePos[0]);
        this.waveY = Double.parseDouble(wavePos[1]);
        this.currentWave = 1;
    }

    /**
     * Draws the current wave number on the screen.
     */
    public void drawWave() {
        font.drawString(waveText + " " + currentWave, waveX, waveY,
                new DrawOptions().setBlendColour(colour));
    }

    public void increment() {
        currentWave++;
    }
}