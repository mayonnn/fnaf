package scenes;

import main.GamePanel;
import managers.GameStateManager;

import java.awt.*;

public class MenuScene implements GameScene{
    private GameStateManager gameStateManager = new GameStateManager();

    public MenuScene(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.RED);
        g.setFont(GamePanel.titleFont);

        String clickToPlayText = "CLICK TO PLAY";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(clickToPlayText);
        int x = (GamePanel.WIDTH - textWidth) / 2;
        int y = GamePanel.HEIGHT / 2;

        g.drawString(clickToPlayText, x, y);
    }
}
