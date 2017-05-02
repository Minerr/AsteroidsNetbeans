/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.commonplayer.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

public class PlayerControlSystemTest {

    GameData gameData;
    World world;
    PlayerControlSystem instance;

    public PlayerControlSystemTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        world = new World();
        gameData = new GameData();
        gameData.setDisplayWidth(800);
        gameData.setDisplayHeight(600);
        instance = new PlayerControlSystem();
    }

    @After
    public void tearDown() {
        world = null;
        gameData = null;
        instance = null;
    }

    /**
     * Test of start method, of class PlayerControlSystem.
     */
    @Test
    public void testStart() {
        System.out.println("start");
        instance.start(gameData, world);
        assertFalse(world.getEntities().isEmpty());
    }

    /**
     * Test of process method, of class PlayerControlSystem.
     */
    @Test
    public void testProcess() {
        System.out.println("process");
        float prevX;
        float prevY;
        Entity player = null;

        instance.start(gameData, world);
        assertFalse(world.getEntities().isEmpty());
        instance.process(gameData, world);

        for (Entity e : world.getEntities(Player.class)) {
            player = e;
        }

        prevX = player.getX();
        prevY = player.getY();

        for (int i = 0; i < 100; i++) {
            gameData.setDelta(i);
            gameData.getKeys().setKey(gameData.getKeys().W, true);
            gameData.getKeys().setKey(gameData.getKeys().A, true);
            instance.process(gameData, world);
        }

        assertNotEquals(prevX, player.getX());
        assertNotEquals(prevY, player.getY());

    }

    /**
     * Test of stop method, of class PlayerControlSystem.
     */
    @Test
    public void testStop() {
        System.out.println("stop");
        instance.start(gameData, world);
        assertFalse(world.getEntities().isEmpty());
        instance.stop(gameData, world);
        assertTrue(world.getEntities().isEmpty());

    }

}