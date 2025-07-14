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

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 600;

    private Thread thread;
    private boolean running;

    private GameState gameState = GameState.MENU;
    private int currentNight = 1;
    private int currentHour = 12;
    private long lastHourStartTime = System.currentTimeMillis();

    private PowerManager powerManager = new PowerManager();

    private CameraManager cameraManager;
    private ViewState viewState;

    private Image defaultImg;
    private Image endScreen;

    private final int defaultTextSize = 20;
    private final Font defaultFont = new Font("Arial", Font.PLAIN, defaultTextSize);
    private final Font titleFont = new Font("Arial", Font.BOLD, defaultTextSize + defaultTextSize / 2);
    private final Font smallFont = new Font("Arial", Font.BOLD, defaultTextSize - 5);

    private Door doorLeft;
    private Door doorRight;
    private int doorWidth = 200;
    private int doorHeight = 400;
    private int doorLeftX = 70;
    private int doorRightX = WIDTH - doorWidth - doorLeftX;
    private int doorY = 70;


    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        cameraManager = new CameraManager(WIDTH, HEIGHT, powerManager);
        viewState = cameraManager.getViewState();

        doorLeft = new Door(doorLeftX, doorY, doorWidth, doorHeight, Door.Side.LEFT, powerManager);
        doorRight = new Door(doorRightX, doorY, doorWidth, doorHeight, Door.Side.RIGHT, powerManager);


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
                doorLeft.draw(g);
                doorRight.draw(g);
            } else if (viewState == ViewState.CAMERA){
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, WIDTH, HEIGHT);
            }

            powerManager.drawPowerStats(g, 10, HEIGHT - 40, defaultFont);
            drawTimeStats(g, WIDTH - 100, 30);
            cameraManager.drawButton(g, defaultFont);
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
        } else if (gameState == GameState.PLAYING) {
            doorLeft.handleClick(e.getPoint());
            doorRight.handleClick(e.getPoint());
            repaint();
        }else if (gameState == GameState.GAME_OVER) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState == GameState.PLAYING) {
            cameraManager.handleMouseMoved(e.getPoint());
            viewState = cameraManager.getViewState();
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        doorLeft.handleMousePressed(e.getPoint());
        doorRight.handleMousePressed(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        doorLeft.handleMouseReleased(e.getPoint());
        doorRight.handleMouseReleased(e.getPoint());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
