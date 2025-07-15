import java.awt.*;

public class GameOverScene implements GameScene{
    GameStateManager gameStateManager = new GameStateManager();
    public GameOverScene(GameStateManager gameStateManager) {
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

        String gameOverText = "GAME OVER";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);
        int x = (GamePanel.WIDTH - textWidth) / 2;
        int y = GamePanel.HEIGHT / 2;

        g.drawString(gameOverText, x, y);
    }
}
