package dk.sdu.mmmi.cbse.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.sdu.mmmi.cbse.commonbullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameState;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.mmmi.cbse.commonenemy.Enemy;
import dk.sdu.mmmi.cbse.commonexplosion.Explosion;
import dk.sdu.mmmi.cbse.core.managers.GameInputProcessor;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class Game implements ApplicationListener {

    private final Lookup lookup = Lookup.getDefault();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> result;

    private static OrthographicCamera cam;
    private ShapeRenderer sr;

    private SpriteBatch batch;
    private BitmapFont font;

    private final GameData gameData = new GameData();
    private World world = new World();

    // Play State
    private final float PLAYER_RESURRECTION_TIME = 3f;
    private float playerResurrectionTimer = 0;

    // Game Over State
    private boolean isGameOverAnimationDone = false;
    private float gameOverAnimationTimer = 0;
    private float pressEnterTimer;
    private final float PRESS_ENTER_TIME = 1f;

    @Override
    public void create() {

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        cam = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        cam.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        cam.update();

        sr = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        result = lookup.lookupResult(IGamePluginService.class);
        result.addLookupListener(lookupListener);
        result.allItems();

        for (IGamePluginService plugin : result.allInstances()) {
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }
    }

    private void runIPostEntityProcessingServiceProcess() {
        for (IPostEntityProcessingService ie : lookup.lookupAll(IPostEntityProcessingService.class)) {
            ie.process(gameData, world);
        }
    }

    private void runIEntityProcessingServiceProcess() {
        for (IEntityProcessingService ie : lookup.lookupAll(IEntityProcessingService.class)) {
            ie.process(gameData, world);
        }
    }

    private void runIGamePluginServiceStart() {
        for (IGamePluginService ig : lookup.lookupAll(IGamePluginService.class)) {
            ig.start(gameData, world);
        }
    }

    private void runIGamePluginServiceStop() {
        for (IGamePluginService ig : lookup.lookupAll(IGamePluginService.class)) {
            ig.stop(gameData, world);
        }
    }

    private void reloadAllComponents() {
        runIGamePluginServiceStop();
        runIGamePluginServiceStart();
    }

    @Override
    public void render() {

        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();
    }

    private void update() {

        //if ESC is pressed, toggle between play or paused mode.
        if (gameData.getKeys().isKeyPressed(gameData.getKeys().ESCAPE)) {
            toggleGameState();
        }

        // Game State switch
        switch (gameData.getGameState()) {
            case GAMEOVER:
                gameOverAnimationTimer += gameData.getDelta();
                if (gameOverAnimationTimer >= PLAYER_RESURRECTION_TIME * 1.5) {
                    isGameOverAnimationDone = true;
                    gameOverAnimationTimer = 0;
                }

                if (isGameOverAnimationDone) {
                    updateGameOverScreen();
                    drawGameOverText();
                    drawGameOverScreen();
                } else {
                    updatePlayScreen();
                    drawPlayScreen();
                    drawGameOverText();
                }

                break;
            case PAUSED:
                drawPlayScreen();
                drawPauseScreen();
                break;
            case PLAY:
                updatePlayScreen();
                drawPlayScreen();
                break;
        }

        // update game keys
        gameData.getKeys().update();
    }

    private void resetTimers() {
        this.playerResurrectionTimer = 0;
        this.isGameOverAnimationDone = false;
        this.gameOverAnimationTimer = 0;
        this.pressEnterTimer = PRESS_ENTER_TIME;
    }

    private void drawText(String text, float posX, float posY) {
        font.setColor(1, 1, 1, 1);
        batch.begin();
        font.draw(batch, text, posX, posY);
        batch.end();
    }

    //----------- TOGGLE GAME STATE -------------
    private void toggleGameState() {
        if (gameData.getGameState() != (GameState.GAMEOVER)) {
            if (gameData.getGameState() == GameState.PAUSED) {
                gameData.setGameState(GameState.PLAY);
            } else {
                gameData.setGameState(GameState.PAUSED);
            }
        }
    }

    //----------- GAME OVER STATE -------------
    private void updateGameOverScreen() {

        if (gameData.getKeys().isKeyPressed(gameData.getKeys().ENTER)) {
            gameData.setGameState(GameState.PLAY);
            gameData.resetData();

            //TODO: reset player when dead
            resetTimers();
        }

        pressEnterTimer -= gameData.getDelta();
        if (pressEnterTimer < PRESS_ENTER_TIME * -1) {
            pressEnterTimer = PRESS_ENTER_TIME;
        }
    }

    private void drawGameOverText() {
        drawText("GAME OVER", (gameData.getDisplayWidth() / 2) - 50, (gameData.getDisplayHeight() / 2) - 10);
    }

    private void drawGameOverScreen() {
        if (pressEnterTimer > 0) {
            drawText("PRESS ENTER TO START GAME", gameData.getDisplayWidth() / 2 - 120, 80);
        }

    }

    //----------- PAUSED STATE -------------
    private void drawPauseScreen() {
        drawText("PAUSED", (gameData.getDisplayWidth() / 2) - 40, (gameData.getDisplayHeight() / 2));
    }

    // TODO: Reset player correctly at death
    //----------- PLAY STATE -------------
    private void updatePlayScreen() {
        // Update
        runIEntityProcessingServiceProcess();

        if (!gameData.getGameState().equals(GameState.GAMEOVER)) {

            if (gameData.isPlayerDead()) {
                gameData.setCanChangeLevel(false);

                if (gameData.getLives() <= 0) {
                    gameData.setGameState(GameState.GAMEOVER);
                }

                playerResurrectionTimer += gameData.getDelta();
                if (playerResurrectionTimer >= PLAYER_RESURRECTION_TIME) {
                    gameData.setIsPlayerDead(false);
                    // TODO: reset Player position
                    playerResurrectionTimer = 0;
                    gameData.setCanChangeLevel(true);
                }
            }

            // If the player can change level and all asteroids are cleared
            if (gameData.canChangeLevel() && gameData.getAsteroidCount() <= 0) {
                gameData.incrementLevel();
                gameData.incrementLives();

                // TODO: Stop and start relevant components
                reloadAllComponents();
            }
        }

        // Run all post processes, such as Collision Detection
        runIPostEntityProcessingServiceProcess();
    }

    private void drawPlayScreen() {
        drawText("ASTEROIDS GAME", (gameData.getDisplayWidth() / 2) - 60, (gameData.getDisplayHeight()) - 10);

        for (Entity entity : world.getEntities()) {
            if (entity instanceof Bullet) {
                sr.setColor(1, 1, 1, 1);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.circle(entity.getX(), entity.getY(), entity.getRadius());
                sr.end();
            } else if (entity instanceof Explosion) {
                sr.setColor(1, 1, 1, 1);
                sr.begin(ShapeRenderer.ShapeType.Line);
                sr.circle(entity.getX(), entity.getY(), entity.getRadius());
                sr.end();
            } else if (!entity.getIsHit()) {
                float[] shapex = entity.getShapeX();
                float[] shapey = entity.getShapeY();
                if (shapex != null && shapey != null) {

                    if (entity instanceof Enemy) {
                        sr.setColor(Color.RED);
                    } else {
                        sr.setColor(1, 1, 1, 1);
                    }

                    sr.begin(ShapeRenderer.ShapeType.Line);

                    for (int i = 0, j = shapex.length - 1;
                            i < shapex.length;
                            j = i++) {

                        sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
                    }
                    sr.end();
                }
            }
        }

        // Draw stats
        drawText("Level: " + gameData.getLevel(), 20, gameData.getDisplayHeight() - 20);
        drawText("Score: " + gameData.getScore(), 20, gameData.getDisplayHeight() - 40);
        drawText("Lives: " + gameData.getLives(), 20, gameData.getDisplayHeight() - 60);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IGamePluginService> updated = result.allInstances();

            for (IGamePluginService us : updated) {
                // Newly installed modules
                if (!gamePlugins.contains(us)) {
                    us.start(gameData, world);
                    gamePlugins.add(us);
                }
            }

            // Stop and remove module
            for (IGamePluginService gs : gamePlugins) {
                if (!updated.contains(gs)) {
                    gs.stop(gameData, world);
                    gamePlugins.remove(gs);
                }
            }
        }

    };
}
