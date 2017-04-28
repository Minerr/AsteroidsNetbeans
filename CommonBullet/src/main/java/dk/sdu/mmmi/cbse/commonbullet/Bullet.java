package dk.sdu.mmmi.cbse.commonbullet;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author corfixen
 */
public class Bullet extends Entity {

    @Override
    public void collision(Entity collider) {
        this.setIsHit(true);
    }
}
