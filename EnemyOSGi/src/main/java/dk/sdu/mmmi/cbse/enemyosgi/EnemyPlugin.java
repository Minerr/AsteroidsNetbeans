/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemyosgi;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.commonenemy.Enemy;

/**
 *
 * @author Martin
 */
public class EnemyPlugin implements IGamePluginService {

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        Entity enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity entity : world.getEntities(Enemy.class)) {
            world.removeEntity(entity);
        }
    }

    private Entity createEnemyShip(GameData gameData) {
        System.out.println("Creating enemy ship");
        Entity enemyShip = new Enemy();
        enemyShip.setPosition(gameData.getDisplayHeight() - gameData.getDisplayWidth() / 5, gameData.getDisplayHeight() - gameData.getDisplayHeight() / 5);
        enemyShip.setDirection(0, 0);

        float height = 8;
        float width = 5;

        enemyShip.setDimension(width, height);
        enemyShip.setRadius(height);

        enemyShip.setShapeX(new float[4]);
        enemyShip.setShapeY(new float[4]);

        enemyShip.setMaxSpeed(200);
        enemyShip.setAcceleration(350);
        enemyShip.setDeacceleration(10);

        enemyShip.setRadians(3.1415f / 2);
        enemyShip.setRotationSpeed(3);

        enemyShip.setLife(3);
        enemyShip.setScore(500);

        return enemyShip;
    }
}
