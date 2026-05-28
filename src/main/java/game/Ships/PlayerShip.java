package game.Ships;

import bagel.*;
import game.Lives;
import game.ShadowAliens;

import java.util.Properties;

/**
 * Handles left/right movement, shooting cooldown, invincibility,
 * and powerup effects.
 */
public class PlayerShip extends Ship {
    private final Lives lives;

    private final int coolDown;
    private final Image invincibilityImage;
    private double coolDownTimer = 0;
    private double invincibilityTimer = 0;
    private boolean devInvincible = false;
    private double currentSpeed;
    private int currentCoolDown;

    /**
     * Constructs PlayerShip by loading image, position, speed,
     * cooldown, and invincibility image from properties.
     *
     * @param gameProps the loaded game properties file
     */
    public PlayerShip(Properties gameProps) {
        super(new Image(gameProps.getProperty("player.image")),
                ShadowAliens.screenWidth / 2.0,
                Double.parseDouble(gameProps.getProperty("player.posY")),
                Integer.parseInt(gameProps.getProperty("player.speed")));
        this.lives = new Lives(gameProps);
        coolDown = Integer.parseInt(
                gameProps.getProperty("player.shootCooldown"));
        invincibilityImage   = new Image(
                gameProps.getProperty("invincibility.image"));

        currentSpeed = getSpeed();
        currentCoolDown = coolDown;
    }

    /**
     * moves the ship and ticks timers based on input.
     * Called by BattleScreen each frame.
     *
     * @param input       the current frame's input
     * @param actualSpeed the current speed multiplier
     */
    public void update(Input input, double actualSpeed) {
        moveShip(input, actualSpeed);
    }

    @Override
    public void update(double actualSpeed) {}

    /**
     * Draws the player ship. If invincible, draws invincibility
     * image on top
     */
    @Override
    public void draw() {
        super.draw();
        if (isInvincible()) {
            invincibilityImage.draw(getX(), getY());
        }
    }

    /**
     * Handles left/right movement, respects screen boundaries,
     * and decrements cooldown and invincibility timers each frame.
     *
     * @param input       the current frame's input
     * @param actualSpeed the current speed multiplier
     */
    private void moveShip(Input input, double actualSpeed) {
        boolean movingLeft  = input.isDown(Keys.A) || input.wasPressed(Keys.A);
        boolean movingRight = input.isDown(Keys.D) || input.wasPressed(Keys.D);

        if (movingLeft && !movingRight)
            setX(getX() - currentSpeed * actualSpeed);
        if (movingRight && !movingLeft)
            setX(getX() + currentSpeed * actualSpeed);

        restrictToScreen();

        if (coolDownTimer > 0) coolDownTimer -= actualSpeed;
        if (coolDownTimer < 0) coolDownTimer = 0;

        if (invincibilityTimer > 0) invincibilityTimer -= actualSpeed;
        if (invincibilityTimer < 0) invincibilityTimer = 0;
    }

    /**
     * Restrict ship's x coordinate so doesn't go beyond screen edges
     */
    private void restrictToScreen() {
        double halfWidth = getImage().getWidth() / 2.0;
        if (getX() - halfWidth < 0) {
            setX(halfWidth);
        } else if (getX() + halfWidth > ShadowAliens.screenWidth) {
            setX(ShadowAliens.screenWidth - halfWidth);
        }
    }

    /**
     * @return true if the shooting cooldown has elapsed.
     */
    public boolean canShoot() {
        return coolDownTimer == 0;
    }

    public void resetCoolDownTimer() {
        coolDownTimer = currentCoolDown;
    }

    public boolean isInvincible() {
        return invincibilityTimer > 0 || devInvincible;
    }

    /**
     * Starts invincibility period for the given duration.
     *
     * @param duration number of frames invincibility lasts
     */
    public void startInvincibility(int duration) {
        invincibilityTimer = duration;
    }

    /** Immediately ends timed invincibility by zeroing the timer. */
    public void stopInvincibility() {
        invincibilityTimer = 0;
    }

    /** Toggles dev-mode invincibility */
    public void toggleDevInvincible() {
        devInvincible = !devInvincible;
    }

    public Lives getLives(){
        return lives;
    }

    /**
     * Reduces shooting cooldown
     * Called by the Cooldown powerup
     */
    public void applyCooldownReduction() {
        currentCoolDown = Math.max(1, coolDown / 3);
    }

    /**
     * Restores shooting cooldown to the original value
     * Called by the Cooldown powerup on expiry.
     */
    public void resetCooldown() {
        currentCoolDown = coolDown;
    }

    /**
     * Doubles the player's speed.
     * Called by the Engine powerup
     */
    public void applySpeedBoost() {
        currentSpeed = getSpeed() * 2;
    }
    public void resetSpeed() {
        currentSpeed = getSpeed();
    }

    /**
     * Resets all mutable attributes to initial values
     * Called when starting a new game.
     */
    public void reset() {
        setX(getInitialX());
        setY(getInitialY());
        lives.reset();
        coolDownTimer      = 0;
        invincibilityTimer = 0;
        devInvincible      = false;
        currentSpeed       = getSpeed();
        currentCoolDown    = coolDown;
    }
}