import java.awt.*;

public class CameraManager {
    private Rectangle cameraButton;
    private boolean cameraButtonClicked = false;
    private boolean hovering = false;

    private GamePanel.ViewState viewState = GamePanel.ViewState.OFFICE;
    private PowerManager powerManager;

    public CameraManager(int panelWidth, int panelHeight, PowerManager powerManager) {
        this.powerManager = powerManager;

        int buttonWidth = panelWidth / 2;
        int buttonHeight = panelHeight / 11;
        int buttonX = panelWidth / 2 - panelWidth / 4;
        int buttonY = panelHeight - panelHeight / 10;
        cameraButton = new Rectangle(buttonX, buttonY, buttonWidth, buttonHeight);
    }

    public void handleMouseMoved(Point p) {
        if (cameraButton.contains(p)) {
            if (!hovering) {
                toggleCamera();
                hovering = true;
            }
        } else if (hovering) {
            hovering = false;
        }
    }

    private void toggleCamera() {
        cameraButtonClicked = !cameraButtonClicked;
        if (viewState == GamePanel.ViewState.OFFICE) {
            viewState = GamePanel.ViewState.CAMERA;
            powerManager.increasePowerUsage();
        } else if (viewState == GamePanel.ViewState.CAMERA) {
            viewState = GamePanel.ViewState.OFFICE;
            powerManager.decreasePowerUsage();
        }
    }

    public void drawButton(Graphics g, Font font) {
        if (cameraButtonClicked) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        g.drawRect(cameraButton.x, cameraButton.y, cameraButton.width, cameraButton.height);

        g.setFont(font);
        g.drawString("SWITCH CAM", cameraButton.x + 30, cameraButton.y + 25);
    }

    public GamePanel.ViewState getViewState() {
        return viewState;
    }
}
