import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraManager {
    private Rectangle cameraButton;
    private boolean cameraButtonClicked = false;
    private boolean hovering = false;

    CameraMap cameraMap = new CameraMap();

    private PlayingScene.ViewState viewState = PlayingScene.ViewState.OFFICE;
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

    public void handleMouseClicked(Point p) {
        cameraMap.handleMouseClicked(p);
    }

    private void toggleCamera() {
        cameraButtonClicked = !cameraButtonClicked;
        if (viewState == PlayingScene.ViewState.OFFICE) {
            viewState = PlayingScene.ViewState.CAMERA;
            powerManager.increasePowerUsage();
        } else if (viewState == PlayingScene.ViewState.CAMERA) {
            viewState = PlayingScene.ViewState.OFFICE;
            powerManager.decreasePowerUsage();
        }
    }

    public void draw(Graphics g, Font font) {
        if (cameraButtonClicked) {
            g.setColor(Color.GREEN);
            cameraMap.draw(g, font);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        g.drawRect(cameraButton.x, cameraButton.y, cameraButton.width, cameraButton.height);

        g.setFont(font);
        g.drawString("SWITCH CAM", cameraButton.x + 30, cameraButton.y + 25);
    }

    public PlayingScene.ViewState getViewState() {
        return viewState;
    }


    private class CameraMap {
        Map<String, Rectangle> camButtons = new HashMap<>();
        String selectedButton;

        Map<String, Image> camImg = new HashMap<>();

        int width = 160;
        int x = GamePanel.WIDTH - width - 20;
        int y = GamePanel.HEIGHT - width - 20;
        int camButtonSize = 30;
        int gap = camButtonSize + 10;

        public CameraMap() {
            camButtons.put("1A", new Rectangle(x + 0, y + 0, camButtonSize, camButtonSize));
            camButtons.put("1B", new Rectangle(x + gap, y + 0, camButtonSize, camButtonSize));
            camButtons.put("1C", new Rectangle(x + 2 * gap, y + 0, camButtonSize, camButtonSize));
            camButtons.put("2A", new Rectangle(x + 0, y + gap, camButtonSize, camButtonSize));
            camButtons.put("2B", new Rectangle(x + gap, y + gap, camButtonSize, camButtonSize));
            camButtons.put("3", new Rectangle(x + 2 * gap, y + gap, camButtonSize, camButtonSize));
            camButtons.put("4A", new Rectangle(x + 0, y + 2 * gap, camButtonSize, camButtonSize));
            camButtons.put("4B", new Rectangle(x + gap, y + 2 * gap, camButtonSize, camButtonSize));
            camButtons.put("5", new Rectangle(x + 2 * gap, y + 2 * gap, camButtonSize, camButtonSize));
            camButtons.put("6", new Rectangle(x + 0, y + 3 * gap, camButtonSize, camButtonSize));
            camButtons.put("7", new Rectangle(x + gap, y + 3 * gap, camButtonSize, camButtonSize));

            selectedButton = "1A";

            try {
                camImg.put("1A", ImageIO.read(getClass().getResource("/resources/cam1A.png")));
                camImg.put("1B", ImageIO.read(getClass().getResource("/resources/cam1B.png")));
                camImg.put("1C", ImageIO.read(getClass().getResource("/resources/cam1C.png")));
                camImg.put("2A", ImageIO.read(getClass().getResource("/resources/cam2A.png")));
                camImg.put("2B", ImageIO.read(getClass().getResource("/resources/cam2B.png")));
                camImg.put("3", ImageIO.read(getClass().getResource("/resources/cam3.png")));
                camImg.put("4A", ImageIO.read(getClass().getResource("/resources/cam4A.png")));
                camImg.put("4B", ImageIO.read(getClass().getResource("/resources/cam4B.png")));
                camImg.put("5", ImageIO.read(getClass().getResource("/resources/cam5.png")));
                camImg.put("6", ImageIO.read(getClass().getResource("/resources/cam6.png")));
                camImg.put("7", ImageIO.read(getClass().getResource("/resources/cam7.png")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void draw(Graphics g, Font font) {
            if (selectedButton != null && camImg.containsKey(selectedButton)) {
                Image scaledImage = camImg.get(selectedButton).getScaledInstance(
                        GamePanel.WIDTH, GamePanel.HEIGHT, Image.SCALE_DEFAULT
                );
                g.drawImage(scaledImage, 0, 0, null);
            }

            for (Map.Entry<String, Rectangle> entry : camButtons.entrySet()) {
                String camId = entry.getKey();
                Rectangle rect = entry.getValue();

                g.setColor((entry.getKey().equals(selectedButton))? Color.GREEN : Color.DARK_GRAY);
                g.fillRect(rect.x, rect.y, rect.width, rect.height);

                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(camId, rect.x + 3, rect.y + rect.height - 4);

            }
        }

        public void handleMouseClicked(Point p) {
            for (Map.Entry<String, Rectangle> entry : camButtons.entrySet()) {
                if (entry.getValue().contains(p)) {
                    selectedButton = entry.getKey();
                }
            }
        }
    }
}
