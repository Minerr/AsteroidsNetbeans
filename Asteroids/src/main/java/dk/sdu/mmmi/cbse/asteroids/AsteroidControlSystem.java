package dk.sdu.mmmi.cbse.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.commonasteroids.Asteroid;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)}
)
public class AsteroidControlSystem implements IEntityProcessingService, IGamePluginService {

    private Random rand;

    private final int ASTEROIDS_AT_START = 2;
    private int numberOfAsteroids;
    private int numberOfLargeAsteroids;
    private int numberOfMediumAsteroids;
    private int numberOfSmallAsteroids;

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("creating all asteroids");
        rand = new Random();

        gameData.setCanChangeLevel(true);

        // Creates random size of asteroids and their amount, dependand on the level.
        int lvl = gameData.getLevel();
        numberOfAsteroids = ((lvl + ASTEROIDS_AT_START) - 1) + lvl;
        numberOfLargeAsteroids = (numberOfAsteroids / 3);
        numberOfSmallAsteroids = (numberOfAsteroids / 3);
        numberOfMediumAsteroids = numberOfAsteroids - (numberOfLargeAsteroids + numberOfSmallAsteroids);

        createRandomAsteroids(gameData, world, numberOfLargeAsteroids, AsteroidType.LARGE);
        createRandomAsteroids(gameData, world, numberOfMediumAsteroids, AsteroidType.MEDIUM);
        createRandomAsteroids(gameData, world, numberOfSmallAsteroids, AsteroidType.SMALL);
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("Removing all asteroids");
        gameData.setCanChangeLevel(false);
    }

    private void createRandomAsteroids(GameData gameData, World world, int count, AsteroidType size) {
        AsteroidFactory asteroidFactory = AsteroidFactory.getInstance();

        for (int i = 0; i < count; i++) {
            int screenSide = rand.nextInt(4);
            float radians = rand.nextInt(2 * 31415);
            radians = radians / 10000;

            float posX = 0;
            float posY = 0;
            float dx;
            float dy;

            switch (screenSide) {
                case 0: // top
                    posX = rand.nextInt(gameData.getDisplayWidth());
                    posY = gameData.getDisplayHeight();
                    break;
                case 1: // right
                    posX = gameData.getDisplayWidth();
                    posY = rand.nextInt(gameData.getDisplayHeight());
                    break;
                case 2: // bottom
                    posX = rand.nextInt(gameData.getDisplayWidth());
                    posY = 0;
                    break;
                case 3: // left
                    posX = 0;
                    posY = rand.nextInt(gameData.getDisplayHeight());
                    break;
            }

            dx = (float) Math.cos(radians);
            dy = (float) Math.sin(radians);

            Entity asteroid = asteroidFactory.createAsteroid(size, posX, posY, dx, dy, radians);
            world.addEntity(asteroid);
            gameData.increaseAsteroidCount(1);
        }
    }

    @Override
    public void process(GameData gameData, World world) {

        float dt = gameData.getDelta();

        // Update all asteroids
        for (Entity e : world.getEntities(Asteroid.class)) {
            e.setX(e.getX() + (e.getDx() * e.getMaxSpeed() * dt));
            e.setY(e.getY() + (e.getDy() * e.getMaxSpeed() * dt));

            e.setRadians(e.getRadians() + (e.getRotationSpeed() * dt));
            setShape(e);
            e.wrap(gameData);

            if (e.getIsHit()) {
                e.setIsDestroyed(true);
                e.setIsHit(false);
            }

            if (e.isDestroyed()) {
                e.decreaseLife(1);
                gameData.increaseScore(e.getScore());
                
                // if asteroid still has lives, split the asteroid by creating two smaller asteroids
                if (e.getLife() != 0) {
                    AsteroidFactory asteroidFactory = AsteroidFactory.getInstance();

                    float originalRotation = (float) Math.toDegrees(Math.acos(e.getDx() / 1));
                    float rad1 = (float) Math.toRadians(originalRotation - 45);
                    float rad2 = (float) Math.toRadians(originalRotation + 45);

                    AsteroidType asteroidSize = AsteroidType.MEDIUM;
                    if (e.getLife() == 1) {
                        asteroidSize = AsteroidType.SMALL;
                    }

                    Entity a1 = asteroidFactory.createAsteroid(asteroidSize, e.getX(), e.getY(), (float) Math.cos(rad1), (float) Math.sin(rad1), rad1);
                    Entity a2 = asteroidFactory.createAsteroid(asteroidSize, e.getX(), e.getY(), (float) Math.cos(rad2), (float) Math.sin(rad2), rad2);

                    gameData.increaseAsteroidCount(2);
                    world.addEntity(a1);
                    world.addEntity(a2);
                }
                world.removeEntity(e.getID());
                gameData.decreaseAsteroidCount(1);
            }

        }

//        // On Event Split Asteroids
//        for (Event event : gameData.getEvents()) {
//            if (event.getType() == EventType.ASTEROID_SPLIT) {
//
//                Entity asteroid = world.getEntityByID(event.getEntityID());
//
//                // Splitting asteroid
//                if (asteroid != null) {
//                    asteroid.decreaseLife(1);
//                    gameData.increaseScore(asteroid.getScore());
//
//                    // if asteroid still has lives, split the asteroid by creating two smaller asteroids
//                    if (asteroid.getLife() != 0) {
//                        AsteroidFactory asteroidFactory = AsteroidFactory.getInstance();
//
//                        float originalRotation = (float) Math.toDegrees(Math.acos(asteroid.getDx() / 1));
//                        float rad1 = (float) Math.toRadians(originalRotation - 45);
//                        float rad2 = (float) Math.toRadians(originalRotation + 45);
//
//                        AsteroidType asteroidSize = AsteroidType.MEDIUM;
//                        if (asteroid.getLife() == 1) {
//                            asteroidSize = AsteroidType.SMALL;
//                        }
//
//                        Entity a1 = asteroidFactory.createAsteroid(asteroidSize, asteroid.getX(), asteroid.getY(), (float) Math.cos(rad1), (float) Math.sin(rad1), rad1);
//                        Entity a2 = asteroidFactory.createAsteroid(asteroidSize, asteroid.getX(), asteroid.getY(), (float) Math.cos(rad2), (float) Math.sin(rad2), rad2);
//
//                        gameData.increaseAsteroidCount(2);
//                        world.addEntity(a1);
//                        world.addEntity(a2);
//                    }
//
//                    world.removeEntity(asteroid.getID());
//                    gameData.removeEvent(event);
//                    gameData.decreaseAsteroidCount(1);
//                }
//            }
//        }
    }

    private void setShape(Entity e) {
        float angle = 0;
        float[] shapeX = e.getShapeX();
        float[] shapeY = e.getShapeY();
        float[] dists = e.getShapeDistances();
        float radians = e.getRadians();
        float numPoints = e.getShapeNumPoints();

        for (int i = 0; i < numPoints; i++) {
            shapeX[i] = e.getX() + (float) Math.cos(angle + radians) * dists[i];
            shapeY[i] = e.getY() + (float) Math.sin(angle + radians) * dists[i];
            angle += 2 * 3.1415f / numPoints;
        }

        e.setShapeX(shapeX);
        e.setShapeY(shapeY);
    }

}
