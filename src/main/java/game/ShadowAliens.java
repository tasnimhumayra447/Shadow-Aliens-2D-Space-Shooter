package game;
import bagel.*;
import bagel.util.Colour;
import java.util.Properties;

/**
 * Main game class that manages initialising the screens and game objects
 */
public class ShadowAliens extends AbstractGame {
    private static Properties gameProps;
    public static double screenWidth;
    public static double screenHeight;

    private final Lives lives;
    private final Wave wave;
    private final Score score;
    private final PlayerShip playerShip;

    private double frameCount = 0;        // tracks game time
    private int timeScale = 1;
    private boolean invincible = false;

    private final EnemyShip[] enemies;
    private int enemyCount = 0;

    private Projectile[] projectiles = new Projectile[1000]; // see ASSUMPTIONS
    private int projectileCount = 0;

    private Explosion[] explosions = new Explosion[1000];    // see ASSUMPTIONS
    private int explosionCount = 0;

    private final PauseScreen pauseScreen;
    private boolean paused = false;

    public ShadowAliens(Properties gameProps) {
        super(Integer.parseInt(gameProps.getProperty("window.width")),
                Integer.parseInt(gameProps.getProperty("window.height")),
                "Shadow Aliens");

        ShadowAliens.gameProps = gameProps;
        screenWidth = Integer.parseInt(gameProps.getProperty("window.width"));
        screenHeight = Integer.parseInt(gameProps.getProperty("window.height"));

        // set background colour
        String[] bgColour = gameProps.getProperty(
                "background.colour").split(",");
        Window.setClearColour(Double.parseDouble(bgColour[0]),
                Double.parseDouble(bgColour[1]),
                Double.parseDouble(bgColour[2]));

        // set text colour
        String[] textColour = gameProps.getProperty("text.colour").split(",");
        Colour colour = new Colour(Double.parseDouble(textColour[0]),
                Double.parseDouble(textColour[1]),
                Double.parseDouble(textColour[2]));

        Font font = new Font(gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));

        // Initialise all game entities
        pauseScreen = new PauseScreen(gameProps, colour);
        lives = new Lives(gameProps);
        wave = new Wave(gameProps, font, colour);
        score = new Score(gameProps, font, colour);
        playerShip = new PlayerShip(gameProps);

        // count how many enemies exist
        while (gameProps.getProperty("enemy." + enemyCount + ".posX") != null) {
            enemyCount++;
        }

        enemies = new EnemyShip[enemyCount];

        // create the enemies
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new EnemyShip(gameProps, i);
        }
    }

    private void resetGame() {
        frameCount = 0;
        timeScale = 1;

        paused = false;
        invincible = false;
        projectileCount = 0;
        explosionCount = 0;

        lives.reset();
        score.reset();
        playerShip.reset();

        // recreate all enemies
        for (int i = 0; i < enemyCount; i++) {
            enemies[i] = new EnemyShip(gameProps, i);
        }
    }

    private void drawShip(Ship ship) {
        ship.drawShip();
    }

    private void drawBattleScreen(){
        // First draw background/effects
        for (int i = 0; i < explosionCount; i++) {
            if (explosions[i].isDestroyed()) continue;
            explosions[i].drawExplosion();
        }

        // then draw ships on top of effects
        for (int i = 0; i < enemyCount; i++) {
            if (enemies[i].isDestroyed()) continue;
            drawShip(enemies[i]);
        }
        drawShip(playerShip);

        // then draw projectiles on top of ships
        for (int i = 0; i < projectileCount; i++) {
            if (projectiles[i] == null || projectiles[i].isDestroyed()) continue;
            projectiles[i].drawProjectile();
        }

        // finally, draw information on top of all other images
        lives.drawLives();
        wave.drawWave();
        score.drawScore();
    }


    /**
     * Run and render the next frame.
     * @param input The current mouse/keyboard input.
     */
    @Override
    protected void update(Input input) {
        drawBattleScreen();

        if (input.wasPressed(Keys.ESCAPE)) {
            paused = !paused;
        }

        // INPUT: Dev-mode Controls
        // increase speed by factor of 1
        if (input.wasPressed(Keys.G)) {
            if (timeScale == -2) timeScale = 1;     // skip speed of 0
            else if (timeScale < 0) timeScale++;
            else timeScale++;
        }

        // decrease speed by factor of 1
        if (input.wasPressed(Keys.F)) {
            if (timeScale == 1) timeScale = -2;    // skip speed of 0
            else if (timeScale < 0) timeScale--;
            else timeScale--;
        }

        if (input.wasPressed(Keys.I)) {
            invincible = !invincible;
        }
        if (input.wasPressed(Keys.R)) {
            resetGame();
        }

        // if actualSpeed > 1, then speed up, otherwise slow down
        double actualSpeed = timeScale > 0 ?
                timeScale : 1.0 / Math.abs(timeScale);

        // skip the game logic since game is frozen
        if (paused) {
            pauseScreen.draw(timeScale, actualSpeed);
            return;
        }

        // only run game logic when not paused:
        frameCount += actualSpeed;

        playerShip.update(input, actualSpeed);

        for (EnemyShip enemy : enemies) {
            enemy.update(frameCount, actualSpeed);
        }

        for (int i = 0; i < explosionCount; i++) {
            explosions[i].update(actualSpeed);
        }

        // check if shoot
        if (input.wasPressed(Keys.SPACE) && playerShip.canShoot()){
            projectiles[projectileCount++] = new Projectile(gameProps,
                    playerShip.getX(), playerShip.getY());
            playerShip.resetCoolDownTimer();
        }

        // check if enemy collides with player
        for (int j = 0; j < enemyCount; j++) {
            if (enemies[j].isDestroyed()) continue;

            // if collision, destroy enemy
            // and player loses a life, unless invincible
            if (playerShip.getBoundingBoxAt().intersects(
                    enemies[j].getBoundingBoxAt())){
                enemies[j].setDestroyed();
                if (!invincible) {
                    lives.decrementLives();
                    // no lives left
                    if (lives.getLives() <= 0) {
                        Window.close();
                    }
                }
            }
        }


        // update ALL projectiles every frame
        for (int i = 0; i < projectileCount; i++) {
            if (projectiles[i] == null || projectiles[i].isDestroyed()){
                continue;
            }
            projectiles[i].update(actualSpeed);

            // check if any enemy collides with this projectile
            for (int j = 0; j < enemyCount; j++) {
                if (enemies[j].isDestroyed()) continue;

                // destroy both and increment score and launch an explosion
                if (projectiles[i].getBoundingBoxAt().intersects(
                        enemies[j].getBoundingBoxAt())){
                       projectiles[i].setDestroyed();
                       enemies[j].setDestroyed();
                       explosions[explosionCount++] = new Explosion(gameProps,
                               enemies[j].getX(), enemies[j].getY());
                       score.incrementScore();
                       break;       // stop checking enemies for this projectile
                }
            }
        }

    }

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
