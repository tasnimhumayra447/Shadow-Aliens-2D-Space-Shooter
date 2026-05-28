package game;

import bagel.*;
import game.Screens.*;
import game.Ships.PlayerShip;

import java.util.Properties;

/**
 * Main entry point for Shadow Aliens.
 * Manages screen state transitions, dev-mode controls, and game speed.
 */
public class ShadowAliens extends AbstractGame {
    public static double screenWidth;
    public static double screenHeight;

    private Screen currentScreen;
    private GameState state;
    private int timeScale = 1;
    private double actualSpeed = 1.0;
    private final Properties gameProps;
    private final PlayerShip playerShip;

    /**
     * Constructs the game, initialises the window, loads properties,
     *
     * @param gameProps the loaded game properties file
     */
    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        this.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(
                gameProps.getProperty("window.height"));
        playerShip = new PlayerShip(gameProps);
        state = GameState.START;
        currentScreen = new StartScreen(gameProps, playerShip);
        String[] bg = gameProps.getProperty("background.colour").split(",");
        Window.setClearColour(
                Double.parseDouble(bg[0]),
                Double.parseDouble(bg[1]),
                Double.parseDouble(bg[2]));
    }

    /**
     * Called once per frame.
     * Handles dev controls (in BATTLE/PAUSE only), state transitions,
     * then delegates update and draw to the current screen.
     *
     * @param input the current frame's input
     */
    @Override
    public void update(Input input) {
        if (state == GameState.BATTLE || state == GameState.PAUSE) {
            handleDevControls(input);
        }
        handleStateTransitions(input);
        currentScreen.update(input);
        currentScreen.draw();
    }

    /**
     * Handles dev-mode keypresses in BATTLE and PAUSE states:
     *
     * @param input the current frame's input
     */
    private void handleDevControls(Input input) {
        if (input.wasPressed(Keys.G)) {
            timeScale++;
            recalcActualSpeed();
        }
        if (input.wasPressed(Keys.F)) {
            timeScale--;
            recalcActualSpeed();
        }
        if (input.wasPressed(Keys.I)) {
            playerShip.toggleDevInvincible();
        }
        if (input.wasPressed(Keys.R)) {
            timeScale = 1;
            actualSpeed = 1.0;
            playerShip.reset();
            state = GameState.START;
            currentScreen = new StartScreen(gameProps, playerShip);
        }
        if (input.wasPressed(Keys.N)) {
            if (state == GameState.BATTLE) {
                ((BattleScreen) currentScreen).nextWave();
            } else if (state == GameState.PAUSE) {
                BattleScreen battleScreen =
                        ((PauseScreen) currentScreen).getBattleScreen();
                battleScreen.nextWave();
                if (battleScreen.isGameWon())  {
                    state = GameState.END;
                    currentScreen = new EndScreen(gameProps, playerShip, true);
                }
            }
        }
    }

    /**
     * Handles screen state transitions based on input and game conditions.
     *
     * @param input the current frame's input
     */
    private void handleStateTransitions(Input input) {
        switch (state) {
            case START:
                if (input.wasPressed(Keys.SPACE)) {
                    state = GameState.BATTLE;
                    currentScreen = new BattleScreen(gameProps, playerShip);
                }
                break;

            case BATTLE:
                BattleScreen battleScreen = (BattleScreen) currentScreen;
                if (input.wasPressed(Keys.ESCAPE)) {
                    state = GameState.PAUSE;
                    currentScreen = new PauseScreen(
                            gameProps, battleScreen, timeScale, actualSpeed);
                } else if (battleScreen.isGameOver()) {
                    state = GameState.END;
                    currentScreen = new EndScreen(gameProps, playerShip, false);
                } else if (battleScreen.isGameWon()) {
                    state = GameState.END;
                    currentScreen = new EndScreen(gameProps, playerShip, true);
                }
                break;

            case PAUSE:
                if (input.wasPressed(Keys.ESCAPE)) {
                    state = GameState.BATTLE;
                    currentScreen =
                            ((PauseScreen) currentScreen).getBattleScreen();
                }
                break;

            case END:
                if (input.wasPressed(Keys.SPACE)) {
                    timeScale = 1;
                    actualSpeed = 1.0;
                    state = GameState.BATTLE;
                    currentScreen = new BattleScreen(gameProps, playerShip);
                }
                break;
        }
    }

    /**
     * Recalculates actualSpeed from timeScale
     */
    private void recalcActualSpeed() {
        if (timeScale >= 1) {
            actualSpeed = timeScale;
        } else {
            actualSpeed = 1.0 / (2 - timeScale);
        }
        if (state == GameState.BATTLE) {
            ((BattleScreen) currentScreen).setActualSpeed(actualSpeed);
        } else if (state == GameState.PAUSE) {
            ((PauseScreen) currentScreen).setTimeScale(timeScale, actualSpeed);
            ((PauseScreen) currentScreen).
                    getBattleScreen().setActualSpeed(actualSpeed);
        }
    }

    /**
     * Application entry point. Reads the game data file path from the
     * JVM argument "gameData", defaults to "gameData.properties" if absent.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        String fileName =  System.getProperty("gameData");
        // default file path if no path provided
        if (fileName == null){
            fileName = "gameData.properties";
        }

        Properties gameProps = IOUtils.readPropertiesFile(fileName);
        ShadowAliens game = new ShadowAliens(gameProps);
        game.run();
    }
}