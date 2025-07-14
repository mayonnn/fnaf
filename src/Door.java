import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Door {
    public enum Side { LEFT, RIGHT }

    private int x, y, width, height;
    private final int buttonSize = 50;

    private Rectangle buttonBounds;
    private boolean closed = false;

    private final Side side;
    private DoorLight doorLight;
    private PowerManager powerManager;

    private Image jonesy;


    public Door(int x, int y, int width, int height, Side side, PowerManager powerManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.side = side;
        this.powerManager = powerManager;

        setupLayout();
        doorLight = new DoorLight();

        try {
            jonesy = ImageIO.read(getClass().getResource("/resources/jonesy_fortnite.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupLayout() {
        int buttonX = (side == Side.LEFT) ? x - buttonSize - 10 : x + width + 10;
        int buttonY = y + height / 2;
        buttonBounds = new Rectangle(buttonX, buttonY, buttonSize, buttonSize);
    }

    public void draw(Graphics g) {
        if (closed) {
            g.setColor(Color.gray);
            g.fillRect(x, y, width, height);
            g.setColor(Color.green);
            g.fillRect(buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        } else {
            if (doorLight.lightOn) {
                g.setColor(Color.WHITE);
                g.fillRect(x, y, width, height);
            } else if (jonesy != null) {
                Image scaledImage = jonesy.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                g.drawImage(scaledImage, x, y, null);
            }
            g.setColor(Color.red);
            g.fillRect(buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        }
        g.setColor(Color.white);
        g.drawString("DOOR", buttonBounds.x + 5, buttonBounds.y + buttonBounds.height + 15);

        doorLight.draw(g);
    }

    public void handleClick(Point p) {
        if (buttonBounds.contains(p)) {
            closed = !closed;
            if (closed) {
                powerManager.increasePowerUsage();
            } else {
                powerManager.decreasePowerUsage();
            }
        }
    }

    public void handleMousePressed(Point p) {
        doorLight.handleMousePressed(p);
    }

    public void handleMouseReleased(Point p) {
        doorLight.handleMouseReleased(p);
    }


    public Side getSide() { return side; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getButtonSize() { return buttonSize; }





    private class DoorLight {
        private static final int WINDOW_MARGIN = 5;
        private static final int SWITCH_MARGIN = 10;

        private Rectangle window;
        private Rectangle lightSwitch;

        private boolean lightOn = false;

        public DoorLight() {
            int switchX, switchY;
            int switchSize = buttonSize;

            int windowX = (side == Side.LEFT) ? x + width + WINDOW_MARGIN : x - width - WINDOW_MARGIN;
            int windowY = y;
            int windowWidth = width;
            int windowHeight = height / 2;

            window = new Rectangle(windowX, windowY, windowWidth, windowHeight);

            switchX = (side == Side.LEFT) ? x - switchSize - SWITCH_MARGIN : x + width + SWITCH_MARGIN;
            switchY = y + height / 2 - switchSize - 20;

            lightSwitch = new Rectangle(switchX, switchY, switchSize, switchSize);
        }

        public void handleMousePressed(Point p) {
            if (lightSwitch.contains(p)) {
                lightOn = true;
                powerManager.increasePowerUsage();
            }
        }

        public void handleMouseReleased(Point p) {
            if (lightOn) {
                lightOn = false;
                powerManager.decreasePowerUsage();
            }
        }

        public void draw(Graphics g) {
            g.setColor(lightOn ? Color.WHITE : Color.DARK_GRAY);
            g.fillRect(window.x, window.y, window.width, window.height);

            g.setColor(lightOn ? Color.WHITE : Color.GRAY);
            g.fillRect(lightSwitch.x, lightSwitch.y, lightSwitch.width, lightSwitch.height);

            g.setColor(Color.WHITE);
            g.drawString("LIGHT", lightSwitch.x + 5, lightSwitch.y + lightSwitch.height + 15);
        }

        public boolean isLightOn() {
            return lightOn;
        }
    }
}



