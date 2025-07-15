package animatronics;

import managers.NightCycleManager;

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

    public Chica(NightCycleManager nightCycleManager) {
        this.nightCycleManager = nightCycleManager;
    }

    public void update() {
        int currentHour = nightCycleManager.getCurrentHour();
        int currentNight = nightCycleManager.getCurrentNight();

        int ai = AILevel[currentNight][currentHour];
        if (successfulMovement(ai)) {
            //TODO
        }
    }

    public void reset() {
        lastMovementOpportunity = System.currentTimeMillis();
        location = "1A";
    }

    public boolean successfulMovement(int AI) {
        return Math.random() > AI * 0.05;
    }
}
