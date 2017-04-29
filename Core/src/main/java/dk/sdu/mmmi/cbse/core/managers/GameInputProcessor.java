package dk.sdu.mmmi.cbse.core.managers;

import com.badlogic.gdx.InputAdapter;
import dk.sdu.mmmi.cbse.common.data.GameData;

public class GameInputProcessor extends InputAdapter {

    private final GameData gameData;

    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }

    //Set key to true when pressed
    @Override
    public boolean keyDown(int k) {
        gameData.getKeys().setKey(k, true);
        return true;
    }
    //Set key to false when released
    @Override
    public boolean keyUp(int k) {
        gameData.getKeys().setKey(k, false);
        return true;
    }
}








