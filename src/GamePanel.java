import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {

    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER
    }

    public enum ViewState {
        OFFICE,
        CAMERA
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Thread thread;
    private boolean running;

    private GameState gameState = GameState.MENU;
    private ViewState viewState = ViewState.OFFICE;

    private Image defaultImg;

    private final int defautTextSize = 20;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, defautTextSize);
    private final Font titleFont = new Font("Arial", Font.BOLD, 36);

    private Rectangle cameraButton = new Rectangle(WIDTH / 2 - WIDTH / 4, HEIGHT - HEIGHT / 10, WIDTH / 2, HEIGHT / 11);
    private boolean cameraButtonClicked = false;
    private boolean hovering = false;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            defaultImg = ImageIO.read(getClass().getResource("/resources/fn_mold.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame() {
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    @Override
    public void run() {
        while(running) {
            update();
            repaint();

            try {
                Thread.sleep(16);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {

    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(gameState == GameState.MENU) {
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.RED);
            g.setFont(titleFont);

            String clickToPlayText = "CLICK TO PLAY";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(clickToPlayText);
            int x = (WIDTH - textWidth) / 2;
            int y = HEIGHT / 2;

            g.drawString(clickToPlayText, x, y);
        }

        if(gameState == GameState.PLAYING) {
            if (viewState == ViewState.OFFICE) {
                if (defaultImg != null) {
                    Image scaledImage = defaultImg.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
                    g.drawImage(scaledImage, 0, 0, null);
                }
            } else if (viewState == ViewState.CAMERA){
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, WIDTH, HEIGHT);
            }

            drawPowerStats(g, 10, HEIGHT - 40);
        }

        if(gameState == GameState.GAME_OVER) {
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            g.setColor(Color.RED);
            g.setFont(titleFont);

            String gameOverText = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(gameOverText);
            int x = (WIDTH - textWidth) / 2;
            int y = HEIGHT / 2;

            g.drawString(gameOverText, x, y);
        }

    }

    private void drawPowerStats(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(defaultFont);
        g.drawString("Power left: 100%", x, y);

        g.drawString("Usage:", x, y + defautTextSize + 5);
        FontMetrics fm = g.getFontMetrics();
        int usageTextWidth = fm.stringWidth("Usage:");
        int barStartX = x + usageTextWidth + 10;
        int barY = y + defautTextSize - 10;

        g.setColor(Color.GREEN);
        g.fillRect(barStartX , barY, 5, defautTextSize);
        g.fillRect(barStartX + 10, barY, 5, defautTextSize);
        g.setColor(Color.ORANGE);
        g.fillRect(barStartX + 20, barY, 5, defautTextSize);
        g.setColor(Color.RED);
        g.fillRect(barStartX + 30, barY, 5, defautTextSize);

        if (cameraButtonClicked) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        g.drawRect(cameraButton.x, cameraButton.y, cameraButton.width, cameraButton.height);
        g.drawString("SWITCH CAM", cameraButton.x + 30, cameraButton.y + 25);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameState == GameState.MENU) {
            gameState = GameState.PLAYING;
        } else if (gameState == GameState.PLAYING) {
            //TODO
        } else if (gameState == GameState.GAME_OVER) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState == GameState.PLAYING) {
            if (cameraButton.contains(e.getPoint())) {
                if (!hovering) {
                    cameraButtonClicked = !cameraButtonClicked;
                    if (viewState == ViewState.OFFICE) {
                        viewState = ViewState.CAMERA;
                        repaint();
                    } else if (viewState == ViewState.CAMERA) {
                        viewState = ViewState.OFFICE;
                        repaint();
                    }
                    hovering = true;
                }
            } else if (hovering) {
                hovering = false;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
