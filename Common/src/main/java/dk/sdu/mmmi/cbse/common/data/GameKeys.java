package dk.sdu.mmmi.cbse.common.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameKeys {
    /* use this for keycode reference: 
    https://libgdx.badlogicgames.com/nightlies/docs/api/constant-values.html
    */    
    public final int UP = 19;    // Up arrow
    public final int DOWN = 20;  // down arrow
    public final int LEFT = 21;  // left arrow
    public final int RIGHT = 22; // right arrow

    public final int SPACE = 62;   // space
    public final int ESCAPE = 131; // escape
    public final int ENTER = 66;   // Enter

    public final int A = 29; // A
    public final int W = 51; // W
    public final int S = 47; // S
    public final int D = 32; // D
    public final int E = 33; // E

    private Map<Integer, Boolean> currentKeyStates = new ConcurrentHashMap<>();
    private Map<Integer, Boolean> previousKeyStates = new ConcurrentHashMap<>();

    public GameKeys() {
        currentKeyStates.put(UP, false);
        currentKeyStates.put(DOWN, false);
        currentKeyStates.put(LEFT, false);
        currentKeyStates.put(RIGHT, false);

        currentKeyStates.put(SPACE, false);
        currentKeyStates.put(ESCAPE, false);
        currentKeyStates.put(ENTER, false);

        currentKeyStates.put(A, false);
        currentKeyStates.put(S, false);
        currentKeyStates.put(W, false);
        currentKeyStates.put(D, false);
        currentKeyStates.put(E, false);
        
        update();
    }

    public void update() {
        previousKeyStates.clear();
        previousKeyStates.putAll(currentKeyStates);
    }

    public boolean isKeyDown(int key) {
        return currentKeyStates.get(key);
    }

    public boolean isKeyPressed(int key) {
        return currentKeyStates.get(key) && !previousKeyStates.get(key);
    }

    public void setKey(int key, boolean state) {
        currentKeyStates.put(key, state);
    }
}