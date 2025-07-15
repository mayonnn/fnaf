package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import managers.*;
import scenes.*;

public class GamePanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {


    public static final int WIDTH = 1000;
    public static final int HEIGHT = 600;

    public static final int defaultTextSize = 20;
    public static final Font defaultFont = new Font("Arial", Font.PLAIN, defaultTextSize);
    public static final Font titleFont = new Font("Arial", Font.BOLD, defaultTextSize + defaultTextSize / 2);
    public  static final Font smallFont = new Font("Arial", Font.BOLD, defaultTextSize - 5);

    private Thread thread;
    private boolean running;


    private GameStateManager gameStateManager = new GameStateManager();

    private Map<GameStateManager.GameState, GameScene> scenes = new HashMap<>();

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        PlayingScene playingScene = new PlayingScene(gameStateManager);
        TransitionScene transitionScene = new TransitionScene(gameStateManager, playingScene);
        EndScene endScene = new EndScene(gameStateManager);
        playingScene.setNightCycleManager(new NightCycleManager(gameStateManager, transitionScene));

        scenes.put(GameStateManager.GameState.MENU, new MenuScene(gameStateManager));
        scenes.put(GameStateManager.GameState.PLAYING, playingScene);
        scenes.put(GameStateManager.GameState.TRANSITION, transitionScene);
        scenes.put(GameStateManager.GameState.GAME_OVER, new GameOverScene(gameStateManager));
        scenes.put(GameStateManager.GameState.END, new EndScene(gameStateManager));
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
        if (gameStateManager.getGameState() == GameStateManager.GameState.PLAYING) {
            scenes.get(GameStateManager.GameState.PLAYING).update();
        }

        if (gameStateManager.getGameState() == GameStateManager.GameState.TRANSITION) {
            scenes.get(GameStateManager.GameState.TRANSITION).update();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (gameStateManager.getGameState()) {
            case MENU -> scenes.get(GameStateManager.GameState.MENU).render(g);
            case PLAYING -> scenes.get(GameStateManager.GameState.PLAYING).render(g);
            case GAME_OVER -> scenes.get(GameStateManager.GameState.GAME_OVER).render(g);
            case END -> scenes.get(GameStateManager.GameState.END).render(g);
            case TRANSITION -> scenes.get(GameStateManager.GameState.TRANSITION).render(g);
        }
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameStateManager.getGameState() == GameStateManager.GameState.MENU) {
            gameStateManager.setGameState(GameStateManager.GameState.PLAYING);
            ((PlayingScene)scenes.get(GameStateManager.GameState.PLAYING)).reset();
        } else if (gameStateManager.getGameState() == GameStateManager.GameState.PLAYING) {
            (scenes.get(GameStateManager.GameState.PLAYING)).handleMouseClicked(e.getPoint());
        } else if (gameStateManager.getGameState() == GameStateManager.GameState.GAME_OVER) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        scenes.get(gameStateManager.getGameState()).handleMouseMoved(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        scenes.get(gameStateManager.getGameState()).handleMousePressed(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        scenes.get(gameStateManager.getGameState()).handleMouseReleased(e.getPoint());
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
