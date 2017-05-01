package dk.sdu.mmmi.cbse.player;

import dk.sdu.mmmi.cbse.commonplayer.Player;
import dk.sdu.mmmi.cbse.commonbullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.commonexplosion.Explosion;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)}
)
public class PlayerControlSystem implements IEntityProcessingService, IGamePluginService {

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
    

    @Override
    public void start(GameData gameData, World world) {
        System.out.println("Creating PlayerShip");
        // Add entities to the world
        Entity player = createPlayerShip(gameData);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        for (Entity entity : world.getEntities(Player.class)) {
            world.removeEntity(entity);
        }
    }

    private Entity createPlayerShip(GameData gameData) {

        Entity playerShip = new Player();
        playerShip.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        playerShip.setDirection(0, 0);

        float height = 8;
        float width = 5;

        playerShip.setDimension(width, height);
        playerShip.setRadius(height);

        playerShip.setShapeX(new float[4]);
        playerShip.setShapeY(new float[4]);

        playerShip.setMaxSpeed(200);
        playerShip.setAcceleration(160);
        playerShip.setDeacceleration(10);

        playerShip.setRadians(3.1415f / 2);
        playerShip.setRotationSpeed(3);

        playerShip.setLife(3);
        return playerShip;
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity player : world.getEntities(Player.class)) {

            if (!player.isDestroyed()) {
                // Update player data
                x = player.getX();
                y = player.getY();
                dx = player.getDx();
                dy = player.getDy();
                maxSpeed = player.getMaxSpeed();
                acceleration = player.getAcceleration();
                deceleration = player.getDeacceleration();
                radians = player.getRadians();
                rotationSpeed = player.getRotationSpeed();

                // Update DeltaTime
                dt = gameData.getDelta();

                // Get Key Input
                left = gameData.getKeys().isKeyDown(gameData.getKeys().A);
                right = gameData.getKeys().isKeyDown(gameData.getKeys().D);
                up = gameData.getKeys().isKeyDown(gameData.getKeys().W);
                isShooting = gameData.getKeys().isKeyPressed(gameData.getKeys().SPACE);

                // Rotation
                if (left) {
                    radians += rotationSpeed * dt;
                } else if (right) {
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

                player.setPosition(x, y);
                player.setDirection(dx, dy);
                player.setRadians(radians);

                // set shape
                setShape(player);

                // wrap around screen
                player.wrap(gameData);

                // Shoot bullet
                if (isShooting) {
                    System.out.println("Player is shooting");
                    BulletSPI bulletProvider = Lookup.getDefault().lookup(BulletSPI.class);
                    bulletProvider.createBullet(world, player, Player.class);
                }

                if (player.getIsHit()) {
                    player.decreaseLife(1);
                    player.setIsHit(false);
                }

            } else {
                player.setLife(0);
            }

            if (player.getLife() <= 0) {
                gameData.setIsPlayerDead(true);
                gameData.decreaseLives(1);
                //world.addEntity(createExplosion(player.getX(), player.getY(), player.getRadius()));
                world.removeEntity(player);
            }
        }
    }

    private void setShape(Entity player) {
        float[] shapeX = player.getShapeX();
        float[] shapeY = player.getShapeY();

        float radius = player.getRadius();
        float width = player.getWidth();

        shapeX[0] = x + (float) Math.cos(radians) * radius;
        shapeY[0] = y + (float) Math.sin(radians) * radius;

        shapeX[1] = x + (float) Math.cos(radians - (radius / 2) * 3.1415f / width) * radius;
        shapeY[1] = y + (float) Math.sin(radians - (radius / 2) * 3.1415f / width) * radius;

        shapeX[2] = x + (float) Math.cos(radians + 3.1415f) * width;
        shapeY[2] = y + (float) Math.sin(radians + 3.1415f) * width;

        shapeX[3] = x + (float) Math.cos(radians + (radius / 2) * 3.1415f / width) * radius;
        shapeY[3] = y + (float) Math.sin(radians + (radius / 2) * 3.1415f / width) * radius;

        player.setShapeX(shapeX);
        player.setShapeY(shapeY);
    }

}
