package game;
import bagel.*;
import bagel.util.Colour;
import java.util.Properties;

public class Wave {
    private final String waveText;
    private final Colour textColour;
    private final double waveX, waveY;
    private final int WAVE_NUM = 1;
    private final Font font;

    public Wave(Properties gameProps, Font font, Colour colour){
        String[] wavesPosition = gameProps.getProperty("wave.pos").split(",");
        textColour = colour;
        waveX = Double.parseDouble(wavesPosition[0]);
        waveY = Double.parseDouble(wavesPosition[1]);
        waveText =  gameProps.getProperty("wave.text");
        this.font = font;
    }

    public void drawWave(){
        DrawOptions options = new DrawOptions();
        font.drawString(waveText + " " + WAVE_NUM, waveX, waveY,
                options.setBlendColour(textColour));
    }

}
