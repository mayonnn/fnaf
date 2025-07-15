package scenes;

import main.GamePanel;
import managers.GameStateManager;

import java.awt.*;

public class TransitionScene implements GameScene{
    private final int TRANSITION_DURATION = 3000;
    private long transitionStartTime;

    private GameStateManager gameStateManager = new GameStateManager();
    private PlayingScene playingScene;

    public TransitionScene(GameStateManager gameStateManager, PlayingScene playingScene) {
        this.gameStateManager = gameStateManager;
        this.playingScene = playingScene;
    }

    @Override
    public void update() {
        long elapsed = System.currentTimeMillis() - transitionStartTime;
        if (elapsed >= TRANSITION_DURATION) {
            if (playingScene.getCurrentNight() + 1 >= 7) {
                gameStateManager.setGameState(GameStateManager.GameState.END);
            } else {
                playingScene.reset();
                playingScene.resetNextDay();
                gameStateManager.setGameState(GameStateManager.GameState.PLAYING);
            }
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

    public void setTransitionStartTime(long transitionStartTime) {
        this.transitionStartTime = transitionStartTime;
    }

}