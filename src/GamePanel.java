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
        GAME_OVER,
        END,
        TRANSITION
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
    private int currentNight = 1;
    private int currentHour = 12;
    private long lastHourStartTime = System.currentTimeMillis();

    private PowerManager powerManager = new PowerManager();

    private Image defaultImg;
    private Image endScreen;

    private final int defaultTextSize = 20;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, defaultTextSize);
    private final Font titleFont = new Font("Arial", Font.BOLD, defaultTextSize + defaultTextSize / 2);
    private final Font smallFont = new Font("Arial", Font.BOLD, defaultTextSize - 5);


    private Rectangle cameraButton = new Rectangle(WIDTH / 2 - WIDTH / 4, HEIGHT - HEIGHT / 10, WIDTH / 2, HEIGHT / 11);
    private boolean cameraButtonClicked = false;
    private boolean hovering = false;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        try {
            defaultImg = ImageIO.read(getClass().getResource("/resources/fn_mold.png"));
            endScreen = defaultImg;
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
        if (gameState == GameState.PLAYING) {
            if (currentNight >= 7) {
                gameState = GameState.END;
            } else {
                powerManager.update();
                if (powerManager.isPowerDepleted()) {
                    gameState = GameState.GAME_OVER;
                }

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastHourStartTime >= 89000) {
                    if (currentHour == 12) {
                        currentHour = 1;
                    } else if (currentHour == 5) {
                        currentNight++;
                        currentHour = 12;
                        powerManager.reset();
                    }else {
                        currentHour += 1;
                    }
                    lastHourStartTime = currentTime;
                }
            }

        }
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
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
            }

            drawPowerStats(g, 10, HEIGHT - 40);
            drawTimeStats(g, WIDTH - 100, 30);
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

        if (gameState == GameState.END) {
            if (endScreen != null) {
                Image scaledImage = endScreen.getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
                g.drawImage(scaledImage, 0, 0, null);

                g.setColor(Color.WHITE);
                g.setFont(titleFont);

                String endText = "END";
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(endText);
                int x = (WIDTH - textWidth) / 2;
                int y = HEIGHT / 2;

                g.drawString(endText, x, y);
            }
        }
    }

    private void drawPowerStats(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(defaultFont);
        g.drawString("Power left: " + (int) powerManager.getPowerLevel() + "%", x, y);

        g.drawString("Usage:", x, y + defaultTextSize + 5);
        FontMetrics fm = g.getFontMetrics();
        int usageTextWidth = fm.stringWidth("Usage:");
        int barStartX = x + usageTextWidth + 10;
        int barY = y + defaultTextSize - 10;

        if (powerManager.getPowerUsage() >= 0) {
            g.setColor(Color.GREEN);
            g.fillRect(barStartX , barY, 5, defaultTextSize);
        }
        if (powerManager.getPowerUsage() >= 1) {
            g.setColor(Color.GREEN);
            g.fillRect(barStartX + 10, barY, 5, defaultTextSize);
        }
        if (powerManager.getPowerUsage() >= 2) {
            g.setColor(Color.ORANGE);
            g.fillRect(barStartX + 20, barY, 5, defaultTextSize);
        }
        if (powerManager.getPowerUsage() >= 3) {
            g.setColor(Color.RED);
            g.fillRect(barStartX + 30, barY, 5, defaultTextSize);
        }

        if (cameraButtonClicked) {
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.LIGHT_GRAY);
        }

        g.drawRect(cameraButton.x, cameraButton.y, cameraButton.width, cameraButton.height);
        g.drawString("SWITCH CAM", cameraButton.x + 30, cameraButton.y + 25);
    }

    private void drawTimeStats(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.setFont(defaultFont);
        g.drawString(currentHour + " AM", x, y);
        g.setFont(smallFont);
        g.drawString("NIGHT " + currentNight, x, y + defaultTextSize + 5);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameState == GameState.MENU) {
            gameState = GameState.PLAYING;
            powerManager.reset();
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
                        powerManager.setPowerUsage(powerManager.getPowerUsage() + 1);
                        repaint();
                    } else if (viewState == ViewState.CAMERA) {
                        viewState = ViewState.OFFICE;
                        powerManager.setPowerUsage(powerManager.getPowerUsage() - 1);
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
