import java.awt.*;

public class OfficeView {
    private Door doorLeft;
    private Door doorRight;
    private int doorWidth = 150;
    private int doorHeight = 700;
    private int doorLeftX = 70;
    private int doorRightX = GamePanel.WIDTH - doorWidth - doorLeftX;
    private int doorY = 35;


    public OfficeView(PowerManager powerManager) {
        doorLeft = new Door(doorLeftX, doorY, doorWidth, doorHeight, Door.Side.LEFT, powerManager);
        doorRight = new Door(doorRightX, doorY, doorWidth, doorHeight, Door.Side.RIGHT, powerManager);
    }

    public void update() {

    }

    public void draw(Graphics g) {
        doorLeft.draw(g);
        doorRight.draw(g);
    }

    public void handleMouseClicked(Point p) {
        doorLeft.handleClick(p);
        doorRight.handleClick(p);
    }

    public void handleMousePressed(Point p) {
        doorLeft.handleMousePressed(p);
        doorRight.handleMousePressed(p);
    }

    public void handleMouseReleased(Point p) {
        doorLeft.handleMouseReleased(p);
        doorRight.handleMouseReleased(p);
    }

    public Door getDoorLeft() {
        return doorLeft;
    }

    public Door getDoorRight() {
        return doorRight;
    }
}
