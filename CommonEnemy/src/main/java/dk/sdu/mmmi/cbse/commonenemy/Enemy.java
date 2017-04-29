package dk.sdu.mmmi.cbse.commonenemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.commonasteroids.Asteroid;

public class Enemy extends Entity {
    
    @Override
    public void collision(Entity collider) {
        if (collider instanceof Asteroid) {
            this.setIsDestroyed(true);
        } else {
            this.setIsHit(true);
        }
    }
}
