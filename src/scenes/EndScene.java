package scenes;

import main.GamePanel;
import managers.GameStateManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class EndScene implements GameScene{
    private Image endScreen;
    private GameStateManager gameStateManager = new GameStateManager();

    public EndScene(GameStateManager gameStateManager) {
        this.gameStateManager = new GameStateManager();
        try {
            endScreen = ImageIO.read(getClass().getResource("/resources/office.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (endScreen != null) {
            Image scaledImage = endScreen.getScaledInstance(GamePanel.WIDTH, GamePanel.HEIGHT, Image.SCALE_DEFAULT);
            g.drawImage(scaledImage, 0, 0, null);

            g.setColor(Color.WHITE);
            g.setFont(GamePanel.titleFont);

            String endText = "END";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(endText);
            int x = (GamePanel.WIDTH - textWidth) / 2;
            int y = GamePanel.HEIGHT / 2;

            g.drawString(endText, x, y);
        } else {
            g.setColor(Color.green);
            g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        }
    }
}
