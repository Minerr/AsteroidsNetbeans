/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.enemyosgi;

import dk.sdu.mmmi.cbse.commonbullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.commonenemy.Enemy;
import dk.sdu.mmmi.cbse.commonexplosion.Explosion;
import dk.sdu.mmmi.cbse.commonexplosion.IExplosionCreator;
import java.util.Random;
import org.openide.util.Lookup;

public class EnemyProcess implements IEntityProcessingService {

    private Random rand = new Random();

    private float x;
    private float y;
    private float dx;
    private float dy;

    private float maxSpeed;
    private float acceleration;
    private float deceleration;

    private float radians;
    private float rotationSpeed;

    private float dt;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean isShooting;

    private final int maxRandomAction = 40;

    private IExplosionCreator explosionCreator;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity enemy : world.getEntities(Enemy.class)) {
            if (!enemy.getIsHit()) {
                // Update player data
                x = enemy.getX();
                y = enemy.getY();
                dx = enemy.getDx();
                dy = enemy.getDy();
                maxSpeed = enemy.getMaxSpeed();
                acceleration = enemy.getAcceleration();
                deceleration = enemy.getDeacceleration();
                radians = enemy.getRadians();
                rotationSpeed = enemy.getRotationSpeed();

                // Update DeltaTime
                dt = gameData.getDelta();

                // Do random action
                left = false;
                right = false;
                up = false;
                isShooting = false;

                int randomAction = randomInt(0, maxRandomAction + 1);
                if (randomAction < (maxRandomAction / 4)) {
                    left = true;
                } else if (randomAction >= (maxRandomAction / 4) && randomAction < (maxRandomAction / 4) * 3) {
                    right = true;
                } else if (randomAction >= (maxRandomAction / 4) * 3 && randomAction < maxRandomAction) {
                    up = true;
                } else {
                    isShooting = true;
                }

                // Rotation
                if (left) {
                    radians += rotationSpeed * dt;
                }
                if (right) {
                    radians -= rotationSpeed * dt;
                }

                // acceleration
                if (up) {
                    dx += Math.cos(radians) * acceleration * dt;
                    dy += Math.sin(radians) * acceleration * dt;
                }

                // deceleration
                float vec = (float) Math.sqrt(dx * dx + dy * dy);
                if (vec > 0) {
                    dx -= (dx / vec) * deceleration * dt;
                    dy -= (dy / vec) * deceleration * dt;
                }
                if (vec > maxSpeed) {
                    dx = (dx / vec) * maxSpeed;
                    dy = (dy / vec) * maxSpeed;
                }

                // set position
                x += dx * dt;
                y += dy * dt;

                enemy.setPosition(x, y);
                enemy.setDirection(dx, dy);
                enemy.setRadians(radians);

                // set shape
                setShape(enemy);

                // wrap around screen
                enemy.wrap(gameData);

                // Shoot bullet
                if (isShooting) {
                    BulletSPI bulletProvider = Lookup.getDefault().lookup(BulletSPI.class);
                    bulletProvider.createBullet(world, enemy, Enemy.class);
                }
            } else {
                enemy.decreaseLife(1);
                enemy.setIsHit(false);
            }

            if (enemy.isDestroyed()) {
                enemy.setLife(0);
            }

            if (enemy.getLife() <= 0) {
                gameData.increaseScore(enemy.getScore());
                Entity explosion = explosionCreator.createExplosion(enemy.getX(), enemy.getY(), enemy.getRadius());
                world.addEntity(explosion);
                world.removeEntity(enemy);
            }
        }
    }

    private int randomInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private void setShape(Entity ship) {
        float[] shapeX = ship.getShapeX();
        float[] shapeY = ship.getShapeY();

        float radius = ship.getRadius();
        float width = ship.getWidth();

        shapeX[0] = x + (float) Math.cos(radians) * radius;
        shapeY[0] = y + (float) Math.sin(radians) * radius;

        shapeX[1] = x + (float) Math.cos(radians - (radius / 2) * 3.1415f / width) * radius;
        shapeY[1] = y + (float) Math.sin(radians - (radius / 2) * 3.1415f / width) * radius;

        shapeX[2] = x + (float) Math.cos(radians + 3.1415f) * width;
        shapeY[2] = y + (float) Math.sin(radians + 3.1415f) * width;

        shapeX[3] = x + (float) Math.cos(radians + (radius / 2) * 3.1415f / width) * radius;
        shapeY[3] = y + (float) Math.sin(radians + (radius / 2) * 3.1415f / width) * radius;

        ship.setShapeX(shapeX);
        ship.setShapeY(shapeY);

    }

    public void setExplosionCreator(IExplosionCreator explosionCreator) {
        this.explosionCreator = explosionCreator;
    }

    public void removeExplosionCreator(IExplosionCreator explosionCreator) {
        this.explosionCreator = null;
    }
}
