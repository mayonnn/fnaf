package scenes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Map;

import main.GamePanel;
import managers.NightCycleManager;
import managers.*;
import views.OfficeView;

public class PlayingScene implements GameScene{
    public enum ViewState {
        OFFICE,
        CAMERA
    }

    private PowerManager powerManager = new PowerManager();
    private CameraManager cameraManager;

    private OfficeView officeView;
    private Image officeImg;

    private GameStateManager gameStateManager;

    private NightCycleManager nightCycleManager;

    public PlayingScene(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        officeView = new OfficeView(powerManager);
        cameraManager = new CameraManager(GamePanel.WIDTH, GamePanel.HEIGHT, powerManager);

        try {
            officeImg = ImageIO.read(getClass().getResource("/resources/office.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        nightCycleManager.udpate();
        powerManager.update();
        if (powerManager.isPowerDepleted()) {
            gameStateManager.setGameState(GameStateManager.GameState.GAME_OVER);
        }
    }

    @Override
    public void render(Graphics g) {
        if (cameraManager.getViewState() == ViewState.OFFICE) {
            if (officeImg != null) {
                Image scaledImage = officeImg.getScaledInstance(GamePanel.WIDTH, GamePanel.HEIGHT, Image.SCALE_DEFAULT);
                g.drawImage(scaledImage, 0, 0, null);
            }
            officeView.draw(g);
        }
        cameraManager.draw(g, GamePanel.defaultFont);
        powerManager.drawPowerStats(g, 10, GamePanel.HEIGHT - 40, GamePanel.defaultFont);
        drawTimeStats(g, GamePanel.WIDTH - 100, 30);
    }

    private void drawTimeStats(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(GamePanel.defaultFont);
        g.drawString(nightCycleManager.getCurrentHour() + " AM", x, y);
        g.setFont(GamePanel.smallFont);
        g.drawString("NIGHT " + nightCycleManager.getCurrentNight(), x, y + GamePanel.defaultTextSize + 5);
    }

    public void reset() {
        powerManager.reset();
        officeView.getDoorLeft().reset();
        officeView.getDoorRight().reset();
        cameraManager.reset();
        nightCycleManager.resetLastHourStartTime();
    }

    public void resetNextDay() {
        nightCycleManager.resetNextDay();
    }

    public void handleMouseClicked(Point p) {
        if (cameraManager.getViewState() == ViewState.OFFICE) {
            officeView.handleMouseClicked(p);
        } else if (cameraManager.getViewState() == ViewState.CAMERA) {
            cameraManager.handleMouseClicked(p);
        }
    }

    public void handleMousePressed(Point p) {
        if (cameraManager.getViewState() == ViewState.OFFICE) {
            officeView.handleMousePressed(p);
        }
    }

    public void handleMouseReleased(Point p) {
        if (cameraManager.getViewState() == ViewState.OFFICE) {
            officeView.handleMouseReleased(p);
        }
    }

    public void handleMouseMoved(Point p) {
        if (gameStateManager.getGameState() == GameStateManager.GameState.PLAYING) {
            cameraManager.handleMouseMoved(p);
        }
    }

    public void setNightCycleManager(NightCycleManager nightCycleManager) {
        this.nightCycleManager = nightCycleManager;
    }

    public int getCurrentNight() {
        return nightCycleManager.getCurrentNight();
    }
}
