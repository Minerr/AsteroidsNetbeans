
package dk.sdu.mmmi.cbse.bullet;

import dk.sdu.mmmi.cbse.commonbullet.Bullet;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;
import java.util.LinkedList;
import java.util.List;

public class BulletController {

    private static BulletController instance = new BulletController();

    private List<Entity> bulletList = new LinkedList<>();
    private final int MAX_BULLET_COUNT = 50;
    private Entity[] aBulletList = new Entity[MAX_BULLET_COUNT];
    
    private BulletController() {
    }

    public static BulletController getInstance() {
        return instance;
    }
    
    // Allocates all bullets as placeholders in bulletList
    public void allocateBullets() {
        System.out.println("Allocating " + MAX_BULLET_COUNT + " bullets");
        for (int i = 0; i < MAX_BULLET_COUNT; i++) {
            Entity placeHolder = new Bullet();
            placeHolder.setLife(0);
            //bulletList.add(placeHolder);
            aBulletList[i] = placeHolder;
        }
    }

    // Clears all bullets from World and afterwards the BulletList
    public void clearBullets(World world) {
        for (Entity bullet : aBulletList) {
            world.removeEntity(bullet.getID());
        }
        allocateBullets();
    }

    // Find an unused bullet in BulletList and activate it
    public void shootBullet(World world, Entity shooter) {
        for (Entity bullet : aBulletList) {
            if (bullet.getLife() == 0) {

                // Bullet init
                bullet.setHeight(2);
                bullet.setWidth(2);
                bullet.setLife(1);
                bullet.setIsHit(false);
                bullet.setExpiration(5);
                bullet.setRadius(bullet.getWidth() / 2);

                // set shoot direction and speed
                float radians = shooter.getRadians();
                float dx = (float) Math.cos(radians);
                float dy = (float) Math.sin(radians);
                bullet.setRadians(radians);
                bullet.setDirection(dx, dy);
                bullet.setMaxSpeed(shooter.getMaxSpeed() * 1.2f);

                // offset bullet in front of shooter
                float bulletPosX = shooter.getX() + dx * (shooter.getRadius());
                float bulletPosY = shooter.getY() + dy * (shooter.getRadius());
                bullet.setPosition(bulletPosX, bulletPosY);
                
                // Add bullet to world
                world.addEntity(bullet);
                break;
            }
        }
    }

    public Entity[] getBulletList() {
        return aBulletList;
    }
}
