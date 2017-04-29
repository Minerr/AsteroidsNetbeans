package dk.sdu.mmmi.cbse.asteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.commonasteroids.Asteroid;
import java.util.Random;

public class AsteroidFactory {

    private static AsteroidFactory instance = new AsteroidFactory();

    private Random rand = new Random();

    private AsteroidFactory() {
    }

    public static AsteroidFactory getInstance() {
        return instance;
    }

    public Entity createAsteroid(AsteroidType asteroidType, float posX, float posY, float dx, float dy, float radians) {
        Entity asteroid = new Asteroid();
        int size;

        switch (asteroidType) {
            case LARGE:
                size = 40;
                asteroid.setShapeNumPoints(12);
                asteroid.setWidth(size);
                asteroid.setHeight(size);
                asteroid.setRadius(size / 2);
                asteroid.setLife(3);
                asteroid.setMaxSpeed(randomInt(20, 30));
                asteroid.setScore(20);
                break;
            case MEDIUM:
                size = 20;
                asteroid.setShapeNumPoints(10);
                asteroid.setWidth(size);
                asteroid.setHeight(size);
                asteroid.setRadius(size / 2);
                asteroid.setLife(2);
                asteroid.setMaxSpeed(randomInt(50, 60));
                asteroid.setScore(50);
                break;
            case SMALL:
                size = 12;
                asteroid.setShapeNumPoints(8);
                asteroid.setWidth(size);
                asteroid.setHeight(size);
                asteroid.setRadius(size / 2);
                asteroid.setLife(1);
                asteroid.setMaxSpeed(randomInt(70, 100));
                asteroid.setScore(100);
                break;
        }
        
        asteroid.setignoreCollisionForClass(Asteroid.class);
        asteroid.setShapeX(new float[asteroid.getShapeNumPoints()]);
        asteroid.setShapeY(new float[asteroid.getShapeNumPoints()]);
        asteroid.setShapeDistances(new float[asteroid.getShapeNumPoints()]);

        asteroid.setRotationSpeed(randomInt(-1, 1));
        asteroid.setPosition(posX, posY);
        asteroid.setDirection(dx, dy);
        asteroid.setRadians(radians);

        float[] dists = asteroid.getShapeDistances();
        for (int i = 0; i < asteroid.getShapeNumPoints(); i++) {
            dists[i] = randomInt((int) (asteroid.getRadius() / 2), (int) asteroid.getRadius());
        }

        asteroid.setShapeDistances(dists);
        return asteroid;
    }

    private int randomInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
