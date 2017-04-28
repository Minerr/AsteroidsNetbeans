package dk.sdu.mmmi.cbse.commonplayer;

import dk.sdu.mmmi.cbse.common.data.Entity;

public class Player extends Entity {

    @Override
    public void collision(Entity collider) {
        this.setIsHit(true);
    }
}
