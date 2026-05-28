package game;

import bagel.*;
import bagel.util.Rectangle;

/**
 * Abstract base class for all game entities.
 * Handles position, image, drawing, and bounding box.
 */
public abstract class GameEntity {
    private double x, y;
    private final Image image;

    /**
     * Constructs a game entity with an image and position.
     *
     * @param image entity image
     * @param x initial x-coordinate
     * @param y initial y-coordinate
     */
    public GameEntity(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    /**
     * Updates the entity state each frame.
     *
     * @param actualSpeed speed scaling factor
     */
    public abstract void update(double actualSpeed);

    /**
     * Draws the entity on the screen.
     */
    public void draw(){
        image.draw(x, y);
    }

    public Rectangle getBoundingBox() {
        return image.getBoundingBoxAt(new bagel.util.Point(x, y));
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    protected void setX(double x) {
        this.x = x;
    }
    protected void setY(double y) {
        this.y = y;
    }
    protected Image getImage() {
        return image;
    }
}
