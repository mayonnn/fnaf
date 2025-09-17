package animatronics;

import managers.GameStateManager;
import managers.NightCycleManager;
import views.OfficeView;

public class Chica {
    private int[][] AILevel = {{0, 0, 0, 1, 2, 2}, //night 1, 12AM/1AM/2AM/3AM/4AM/5AM
                                {1, 1, 1, 2, 3, 3}, //night 2
                                {5, 5, 5, 6, 7, 7}, //night 3
                                {4, 4, 4, 5, 6, 6}, //night 4
                                {7, 7, 7, 8, 9, 9}, //night 5
                                {12, 12, 12, 13, 14, 14}}; //night 6

    private double movementTime = 4.98;
    private String location = "1A";
    private long lastMovementOpportunity;

    private NightCycleManager nightCycleManager;
    private OfficeView officeView;
    private GameStateManager gameStateManager;

    public Chica(NightCycleManager nightCycleManager, OfficeView officeView, GameStateManager gameStateManager) {
        this.nightCycleManager = nightCycleManager;
        this.officeView = officeView;
        this.gameStateManager = gameStateManager;
    }

    public void update() {
        int currentHour = nightCycleManager.getCurrentHour();
        int currentNight = nightCycleManager.getCurrentNight();

        int ai = AILevel[currentNight][currentHour];
        if (successfulMovement(ai)) {
            switch (location) {
                case "1A":
                    location = "1B";
                    break;
                case "1B":
                    location = (Math.random() > 0.5) ? "7" : "6";
                    break;
                case "4A":
                    location = "1B";
                    break;
                case "4B":
                    location = "office";
                    break;
                case "6":
                    location = (Math.random() > 0.5) ? "4A" : "7";
                    break;
                case "7":
                    location = (Math.random() > 0.5) ? "4A" : "6";
                    break;
            }

            System.out.println(location);

            if (location.equals("office")) {
                if (officeView.getDoorLeft().isClosed()) {
                    location = "4A";
                } else {
                    //TODO: jumpscare
                    gameStateManager.setGameState(GameStateManager.GameState.END);
                }
            }
        }

        draw();
    }

    private void draw() {
        
    }

    public void reset() {
        lastMovementOpportunity = System.currentTimeMillis();
        location = "1A";
    }

    public boolean successfulMovement(int AI) {
        return Math.random() < AI * 0.05;
    }
}
