package managers;

/**
 * Class to help handle the game state universally
 */
public class GameStateManager {
    public enum GameState {
        MENU,
        PLAYING,
        GAME_OVER,
        END,
        TRANSITION
    }
    private GameState gameState = GameState.MENU;

    public GameStateManager() {

    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
