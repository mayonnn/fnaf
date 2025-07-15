package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("super scary fnaf");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        GamePanel gamePanel = new GamePanel();

        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
        gamePanel.startGame();
    }
}