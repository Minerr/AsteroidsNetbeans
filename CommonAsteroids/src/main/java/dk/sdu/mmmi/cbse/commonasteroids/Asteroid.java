
package dk.sdu.mmmi.cbse.commonasteroids;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.commonexplosion.Explosion;


public class Asteroid extends Entity {

    @Override
    public void collision(Entity collider) {
        if (collider instanceof Explosion) {
            // do nothing
        } else {
            this.setIsHit(true);
        }
    }

}
