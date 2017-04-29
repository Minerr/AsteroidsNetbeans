package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.commonbullet.Bullet;
import dk.sdu.mmmi.cbse.commonbullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)}
)
public class BulletControlSystem implements IEntityProcessingService, IGamePluginService, BulletSPI {

    private float x;
    private float y;
    private float dx;
    private float dy;
    private float speed;
    private float dt;

    @Override
    public void start(GameData gameData, World world) {
        BulletController.getInstance().allocateBullets();

    }

    @Override
    public void stop(GameData gameData, World world) {
        BulletController.getInstance().clearBullets(world);
    }

    @Override
    public void process(GameData gameData, World world) {

        dt = gameData.getDelta();

        // Update every bullet
        for (Entity bullet : world.getEntities(Bullet.class)) {

            bullet.reduceExpiration(dt);

            if (bullet.getIsHit() || bullet.getExpiration() <= 0 || wrap(gameData, bullet)) {
                bullet.setLife(0);
                world.removeEntity(bullet.getID());
            } else {
                // get Data
                x = bullet.getX();
                y = bullet.getY();
                dx = bullet.getDx();
                dy = bullet.getDy();
                speed = bullet.getMaxSpeed();

                // update position
                x += dx * speed * dt;
                y += dy * speed * dt;

                // Set new position
                bullet.setPosition(x, y);
            }
        }
    }

    private boolean wrap(GameData gameData, Entity e) {
        if (e.getX() < 0) {
            return true;
        }
        if (e.getX() > gameData.getDisplayWidth()) {
            return true;
        }
        if (e.getY() < 0) {
            return true;
        }
        if (e.getY() > gameData.getDisplayHeight()) {
            return true;
        }

        return false;
    }

    @Override
    public void createBullet(World world, Entity shooter, Class shooterType) {
        BulletController.getInstance().shootBullet(world, shooter, shooterType);
    }
    
    
}
