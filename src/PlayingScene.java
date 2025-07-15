import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class PlayingScene implements GameScene{
    public enum ViewState {
        OFFICE,
        CAMERA
    }

    private final int TRANSITION_DURATION = 3000;
    private long transitionStartTime = 0;


    private ViewState viewState;
    private PowerManager powerManager = new PowerManager();
    private CameraManager cameraManager;

    private TransitionScene transitionScene;

    private static final int DAY_LENGTH = 6000; //86000

    private int currentNight = 1;
    private int currentHour = 12;
    private long lastHourStartTime = System.currentTimeMillis();

    private OfficeView officeView;
    private Image officeImg;

    private GameStateManager gameStateManager;

    public PlayingScene(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        officeView = new OfficeView(powerManager);

        cameraManager = new CameraManager(GamePanel.WIDTH, GamePanel.HEIGHT, powerManager);
        viewState = cameraManager.getViewState();
        transitionScene = new TransitionScene(gameStateManager);

        try {
            officeImg = ImageIO.read(getClass().getResource("/resources/office.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        if (currentNight >= 7) {
            gameStateManager.setGameState(GameStateManager.GameState.END);
        } else {
            powerManager.update();
            if (powerManager.isPowerDepleted()) {
                gameStateManager.setGameState(GameStateManager.GameState.GAME_OVER);
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastHourStartTime >= DAY_LENGTH) {
                if (currentHour == 12) {
                    currentHour = 1;
                } else if (currentHour == 5) {
                    gameStateManager.setGameState(GameStateManager.GameState.TRANSITION);
                    transitionStartTime = System.currentTimeMillis();
                }else {
                    currentHour += 1;
                }
                lastHourStartTime = currentTime;
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (viewState == ViewState.OFFICE) {
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
        g.drawString(currentHour + " AM", x, y);
        g.setFont(GamePanel.smallFont);
        g.drawString("NIGHT " + currentNight, x, y + GamePanel.defaultTextSize + 5);
    }

    public void resetPowerManager() {
        powerManager.reset();
    }

    public void handleMouseClicked(Point p) {
        if (viewState == ViewState.OFFICE) {
            officeView.handleMouseClicked(p);
        } else if (viewState == ViewState.CAMERA) {
            cameraManager.handleMouseClicked(p);
        }
    }

    public void handleMousePressed(Point p) {
        if (viewState == ViewState.OFFICE) {
            officeView.handleMousePressed(p);
        }
    }

    public void handleMouseReleased(Point p) {
        if (viewState == ViewState.OFFICE) {
            officeView.handleMouseReleased(p);
        }
    }

    public void handleMouseMoved(Point p) {
        if (gameStateManager.getGameState() == GameStateManager.GameState.PLAYING) {
            cameraManager.handleMouseMoved(p);
            viewState = cameraManager.getViewState();
        }
    }

    public void transitionUpdate() {
        transitionScene.update();
    }

    public void transitionRender(Graphics g) {
        transitionScene.render(g);
    }

    private class TransitionScene implements GameScene{
        private GameStateManager gameStateManager = new GameStateManager();

        public TransitionScene(GameStateManager gameStateManager) {
            this.gameStateManager = gameStateManager;
        }

        @Override
        public void update() {
            long elapsed = System.currentTimeMillis() - transitionStartTime;
            if (elapsed >= TRANSITION_DURATION) {
                currentNight++;
                currentHour = 12;
                officeView.getDoorLeft().reset();
                officeView.getDoorRight().reset();
                powerManager.reset();
                gameStateManager.setGameState(GameStateManager.GameState.PLAYING);
                viewState = PlayingScene.ViewState.OFFICE;
                lastHourStartTime = System.currentTimeMillis();
            }
        }

        @Override
        public void render(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

            g.setFont(GamePanel.titleFont);
            g.setColor(Color.WHITE);

            long elapsed = System.currentTimeMillis() - transitionStartTime;
            String textToShow = (elapsed < TRANSITION_DURATION / 2) ? "5 AM" : "6 AM";

            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(textToShow);
            int x = (GamePanel.WIDTH - textWidth) / 2;
            int y = GamePanel.HEIGHT / 2;

            g.drawString(textToShow, x, y);
        }
    }
}
