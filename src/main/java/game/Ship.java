package game;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Ship {
    private Image shipImage;
    private double x, y;

    protected void setShipImage(Image image) {
        this.shipImage = image;
    }
    protected void setX(double x) {
        this.x = x;
    }
    protected void setY(double y) {
        this.y = y;
    }
    protected Image getShipImage() {
        return shipImage;
    }

    public void drawShip() {
        shipImage.draw(x, y);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public Rectangle getBoundingBoxAt() {
        return shipImage.getBoundingBoxAt(new Point(x, y));
    }

}