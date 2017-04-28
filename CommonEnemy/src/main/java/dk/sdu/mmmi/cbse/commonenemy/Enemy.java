package dk.sdu.mmmi.cbse.commonenemy;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author corfixen
 */
public class Enemy extends Entity {

    @Override
    public void collision(Entity collider) {
        this.setIsHit(true);
    }
}
