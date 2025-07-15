import java.awt.*;

public interface GameScene {
    void update();
    void render(Graphics g);
    default void transitionUpdate() {}
    default void transitionRender(Graphics g) {}
    default void handleMouseClicked(Point p) {}
    default void handleMouseMoved(Point p) {}
    default void handleMousePressed(Point p) {}
    default void handleMouseReleased(Point p) {}
}
