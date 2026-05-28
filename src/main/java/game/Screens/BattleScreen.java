package game.Screens;

import bagel.*;
import bagel.util.Colour;
import bagel.util.Rectangle;
import game.*;
import game.Ships.*;
import game.PowerUps.*;
import game.Projectiles.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * BattleScreen manages the main gameplay state of Shadow Aliens.
 * It handles wave loading, entity updates, collision detection,
 * score/lives display, dev-mode controls, and game-over/win conditions.
 */
public class BattleScreen implements Screen {

    private int currentWave;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstFrame = true;
    private double actualSpeed;

    private final Properties gameProps;
    private final PlayerShip playerShip;

    private final Score score;
    private final Wave waveDisplay;

    private final int scoreGotHit;
    private final int scoreWaveCompleted;
    private final int scoreGotPowerUp;
    private final int scoreHitProjectile;

    private final int hitInvincibilityTime;

    // Game entities 
    private ArrayList<EnemyShip> enemies = new ArrayList<>();
    private ArrayList<PlayerProjectile> playerProjectiles = new ArrayList<>();
    private ArrayList<EnemyProjectile> enemyProjectiles = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private ArrayList<PowerUp> PowerUps = new ArrayList<>();

    // Currently-active PowerUp (at most one at a time)
    private PowerUp activePowerUp = null;

    /**
     * Creates a new BattleScreen, initialising all elements and loading wave 1.
     *
     * @param gameProps  the loaded game properties file
     * @param playerShip the shared PlayerShip instance
     */
    public BattleScreen(Properties gameProps, PlayerShip playerShip) {
        this.gameProps  = gameProps;
        this.playerShip = playerShip;

        // Reset mutable state
        this.currentWave  = 1;
        this.gameOver     = false;
        this.gameWon      = false;
        this.actualSpeed  = 1.0;

        // Shared font / colour used across background elements
        Font defaultFont = new bagel.Font(
                gameProps.getProperty("text.font"),
                Integer.parseInt(gameProps.getProperty("text.size")));

        String[] colourParts = gameProps.getProperty("text.colour").split(",");
        Colour textColour = new bagel.util.Colour(
                Double.parseDouble(colourParts[0]),
                Double.parseDouble(colourParts[1]),
                Double.parseDouble(colourParts[2]));

        this.score = new Score(gameProps, defaultFont, textColour);
        this.waveDisplay = new Wave(gameProps, defaultFont, textColour);

        // Score constants
        scoreGotHit = Integer.parseInt(
                gameProps.getProperty("score.gotHit"));
        scoreWaveCompleted = Integer.parseInt(
                gameProps.getProperty("score.waveCompleted"));
        scoreGotPowerUp = Integer.parseInt(
                gameProps.getProperty("score.gotPowerup"));
        scoreHitProjectile = Integer.parseInt(
                gameProps.getProperty("score.hitProjectile"));

        hitInvincibilityTime =
                Integer.parseInt(gameProps.getProperty(
                        "player.hitInvincibilityTime"));

        // Reset player and load the first wave
        playerShip.reset();
        loadWave(currentWave);
    }

    /**
     * Called once per game frame. Updates all entities, handles input,
     * checks collisions, and advances wave / game state as needed.
     *
     * @param input the current frame's input
     */
    @Override
    public void update(Input input) {
        // skip first frame so player doesn't shoot
        if (firstFrame) {
            firstFrame = false;
            return;
        }

        playerShip.update(input, actualSpeed);

        // Create new projectile if player shoots this frame
        if (playerShip.canShoot() && input.wasPressed(Keys.SPACE)) {
            playerProjectiles.add(new PlayerProjectile(
                    gameProps, playerShip.getX(), playerShip.getY()));
            playerShip.resetCoolDownTimer();
        }

        // Update all entities
        for (EnemyShip e : enemies) {
            e.update(actualSpeed);

            EnemyProjectile ep = e.shoot(actualSpeed);
            if (ep != null) {
                enemyProjectiles.add(ep);
            }

        }

        for (PlayerProjectile pp : playerProjectiles) pp.update(actualSpeed);
        for (EnemyProjectile  ep : enemyProjectiles) ep.update(actualSpeed);
        for (Explosion ex : explosions) ex.update(actualSpeed);
        for (PowerUp p : PowerUps) p.update(actualSpeed);


        // Tick the active PowerUp's duration
        if (activePowerUp != null && !activePowerUp.isActive()) {
            activePowerUp.removeEffect(playerShip);
            activePowerUp = null;
        }


        // Collisions
        checkCollisions();

        // Remove off-screen entities
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).isOffScreen()) enemies.remove(i);
        }
        for (int i = playerProjectiles.size() - 1; i >= 0; i--) {
            if (playerProjectiles.get(i).isOffScreen())
                playerProjectiles.remove(i);
        }
        for (int i = enemyProjectiles.size() - 1; i >= 0; i--) {
            if (enemyProjectiles.get(i).isOffScreen())
                enemyProjectiles.remove(i);
        }
        for (int i = explosions.size() - 1; i >= 0; i--) {
            if (explosions.get(i).isDurationComplete()) explosions.remove(i);
        }
        for (int i = PowerUps.size() - 1; i >= 0; i--) {
            if (PowerUps.get(i).isOffScreen() || PowerUps.get(i).isActive())
                PowerUps.remove(i);
        }

        // Wave / game-over transitions
        if (!gameOver && playerShip.getLives().getLives() <= 0) {
            gameOver = true;
            return;
        }

        if (!gameOver && !gameWon && isWaveComplete()) {
            score.incrementScore(scoreWaveCompleted);
            if (hasNextWave()) {
                currentWave++;
                waveDisplay.increment();
                loadWave(currentWave);
            } else {
                gameWon = true;
            }
        }
    }

    /**
     * Draws all entities and the HUD for this frame.
     */
    @Override
    public void draw() {
        for (Explosion ex : explosions) ex.draw();
        for (PowerUp p : PowerUps) p.draw();
        for (EnemyShip e : enemies) e.draw();

        playerShip.draw();

        for (PlayerProjectile pp : playerProjectiles) pp.draw();
        for (EnemyProjectile ep : enemyProjectiles) ep.draw();

        score.drawScore();
        playerShip.getLives().drawLives();
        waveDisplay.drawWave();
    }

    /**
     * Performs collision detection between all relevant entity pairs
     * and applies the specified effects (destruction, score changes, etc.).
     */
    private void checkCollisions() {
        Rectangle playerBox = playerShip.getBoundingBox();

        List<EnemyShip> enemiesToRemove = new ArrayList<>();
        List<PlayerProjectile> playerProjectilesToRemove = new ArrayList<>();
        List<EnemyProjectile> enemyProjectilesToRemove = new ArrayList<>();

        // Player projectile vs enemy ship
        for (PlayerProjectile pp : playerProjectiles) {
            if (playerProjectilesToRemove.contains(pp)) continue;
            Rectangle ppBox = pp.getBoundingBox();
            for (EnemyShip e : enemies) {
                if (enemiesToRemove.contains(e)) continue;
                if (ppBox.intersects(e.getBoundingBox())) {
                    playerProjectilesToRemove.add(pp);
                    enemiesToRemove.add(e);
                    // Award type-specific score
                    score.incrementScore(e.getScore());
                    // Large explosion at enemy centre
                    spawnExplosion(e, true);
                    break;
                }
            }
        }

        // Player projectile vs enemy projectile
        for (PlayerProjectile pp : playerProjectiles) {
            if (playerProjectilesToRemove.contains(pp)) continue;
            for (EnemyProjectile ep : enemyProjectiles) {
                if (enemyProjectilesToRemove.contains(ep)) continue;
                if (pp.getBoundingBox().intersects(ep.getBoundingBox())) {
                    playerProjectilesToRemove.add(pp);
                    enemyProjectilesToRemove.add(ep);
                    // small explosion at enemy projectile centre
                    spawnExplosion(ep, false);
                    score.incrementScore(scoreHitProjectile);
                    break;
                }
            }
        }

        // Enemy ship vs player ship
        for (EnemyShip e : enemies) {
            if (enemiesToRemove.contains(e)) continue;
            if (e.getBoundingBox().intersects(playerBox)) {
                enemiesToRemove.add(e);
                // large explosion at enemy centre
                spawnExplosion(e, true);
                if (!playerShip.isInvincible()) {
                    playerShip.getLives().decrement();
                    score.decrementScore(scoreGotHit);
                    playerShip.startInvincibility(hitInvincibilityTime);
                }
            }
        }

        // Enemy projectile vs player ship
        for (EnemyProjectile ep : enemyProjectiles) {
            if (enemyProjectilesToRemove.contains(ep)) continue;
            if (ep.getBoundingBox().intersects(playerBox)) {
                enemyProjectilesToRemove.add(ep);
                // small explosion at enemy projectile centre
                spawnExplosion(ep, false);
                if (!playerShip.isInvincible()) {
                    playerShip.getLives().decrement();
                    score.decrementScore(scoreGotHit);
                    playerShip.startInvincibility(hitInvincibilityTime);
                }
            }
        }

        // PowerUp vs player ship
        for (PowerUp p : PowerUps) {
            if (p.isActive()) continue;
            if (p.getBoundingBox().intersects(playerBox)) {
                if (activePowerUp != null)
                    activePowerUp.removeEffect(playerShip);
                activePowerUp = p;
                p.applyEffect(playerShip);
                score.incrementScore(scoreGotPowerUp);
                p.markCollected();
            }
        }

        enemies.removeAll(enemiesToRemove);
        playerProjectiles.removeAll(playerProjectilesToRemove);
        enemyProjectiles.removeAll(enemyProjectilesToRemove);
    }

    /**
     * @return true if a next wave is defined in the properties file.
     */
    private boolean hasNextWave() {
        return gameProps.getProperty(
                "wave." + (currentWave + 1) + ".enemy.0.arrivalTime") != null
                || gameProps.getProperty(
                        "wave." + (currentWave + 1) + ".powerup.0.type") != null;
    }

    public void setActualSpeed(double actualSpeed){
        this.actualSpeed = actualSpeed;
    }

    /**
     * Loads all enemies and PowerUps for the given wave number.
     *
     * @param waveNum the 1-based wave index
     */
    private void loadWave(int waveNum) {
        enemies.clear();
        enemyProjectiles.clear();
        playerProjectiles.clear();
        PowerUps.clear();

        // remove active powerup from previous wave
        if (activePowerUp != null) {
            activePowerUp.removeEffect(playerShip);
            activePowerUp = null;
        }

        // Load enemies
        int enemyIndex = 0;
        while (gameProps.getProperty(
                "wave." + waveNum + ".enemy." + enemyIndex + ".arrivalTime")
                != null) {
            EnemyShip enemy = (EnemyShip.create(gameProps, waveNum, enemyIndex));
            enemies.add(enemy);
            enemyIndex++;
        }

        // Load PowerUps
        int PowerUpIndex = 0;
        while (gameProps.getProperty(
                "wave." + waveNum + ".powerup." + PowerUpIndex + ".type")
                != null) {
            PowerUp p = PowerUp.create(gameProps, waveNum, PowerUpIndex);
            PowerUps.add(p);
            PowerUpIndex++;
        }
    }

    /**
     * @return true if all enemies, PowerUps, and enemy projectiles in the
     * current wave have been destroyed or have left the screen.
     */
    private boolean isWaveComplete() {
        if (!enemies.isEmpty()) return false;
        if (!enemyProjectiles.isEmpty()) return false;
        for (PowerUp p : PowerUps) {
            if (!p.isActive()) return false;
        }
        return true;
    }


    /** @return true if the player has lost all lives */
    public boolean isGameOver() { return gameOver; }

    /** @return true if all waves have been completed */
    public boolean isGameWon()  { return gameWon; }

    /**
     * Jumps to the next wave immediately (dev-mode N key).
     * All on-screen enemies, enemy projectiles, and player projectiles are
     * silently destroyed (no explosions). Wave-cleared score is awarded.
     */
    public void nextWave() {
        enemies.clear();
        enemyProjectiles.clear();
        playerProjectiles.clear();

        score.incrementScore(scoreWaveCompleted);

        if (hasNextWave()) {
            currentWave++;
            waveDisplay.increment();
            loadWave(currentWave);
        } else {
            gameWon = true;
        }
    }

    /**
     * Creates an explosion depending on type of collision
     * @param source entity that explodes
     * @param large size of the explosion
     */
    private void spawnExplosion(GameEntity source, boolean large) {
        explosions.add(new Explosion(
                gameProps, source.getX(), source.getY(), large));
    }
}