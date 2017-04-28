package dk.sdu.mmmi.cbse.common.data;

public class GameData {

    private float delta;
    private int displayWidth;
    private int displayHeight;
    private final GameKeys keys = new GameKeys();
    private GameState gameState = GameState.PLAY;

    private int score = 0;
    private int level = 1;
    private int lives = 3;
    private boolean isPlayerDead = false;

    private int asteroidCount = 0;
    private boolean canChangeLevel = true;
    private boolean isGameOver = false;

    public void resetData() {
        this.isPlayerDead = false;
        this.canChangeLevel = true;
        this.isGameOver = false;

        this.score = 0;
        this.level = 1;
        this.lives = 3;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean isPlayerDead() {
        return isPlayerDead;
    }

    public void setIsPlayerDead(boolean isPlayerDead) {
        this.isPlayerDead = isPlayerDead;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public int getAsteroidCount() {
        return asteroidCount;
    }

    public void increaseAsteroidCount(int amount) {
        this.asteroidCount += amount;
    }

    public void decreaseAsteroidCount(int amount) {
        this.asteroidCount -= amount;
    }

    public void setAsteroidCount(int asteroidCount) {
        this.asteroidCount = asteroidCount;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int score) {
        this.score += score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void decreaseLives(int amount) {
        this.lives -= amount;
    }

    public void incrementLives() {
        this.lives++;
    }

    public boolean canChangeLevel() {
        return canChangeLevel;
    }

    public void setCanChangeLevel(boolean canChangeLevel) {
        this.canChangeLevel = canChangeLevel;
    }

    public int getLevel() {
        return level;
    }

    public void incrementLevel() {
        this.level++;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
}
