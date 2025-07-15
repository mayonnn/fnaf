package managers;

import scenes.TransitionScene;

public class NightCycleManager {
    private static final int DAY_LENGTH = 1000; //86000

    private int currentNight = 6;
    private int currentHour = 12;
    private long lastHourStartTime = System.currentTimeMillis();

    private GameStateManager gameStateManager;
    private TransitionScene transitionScene;

    private long transitionStartTime = 0;


    public NightCycleManager(GameStateManager gameStateManager, TransitionScene transitionScene) {
        this.gameStateManager = gameStateManager;
        this.transitionScene = transitionScene;
    }

    public void udpate() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHourStartTime >= DAY_LENGTH) {
            if (currentHour == 12) {
                currentHour = 1;
            } else if (currentHour == 5) {
                transitionStartTime = System.currentTimeMillis();
                transitionScene.setTransitionStartTime(transitionStartTime);
                gameStateManager.setGameState(GameStateManager.GameState.TRANSITION);
            }else {
                currentHour += 1;
            }
            lastHourStartTime = currentTime;
        }
    }

    public int getCurrentNight() {
        return currentNight;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void resetNextDay() {
        resetLastHourStartTime();
        currentNight++;
        currentHour = 12;
    }

    public void resetLastHourStartTime() {
        lastHourStartTime = System.currentTimeMillis();
    }
}
