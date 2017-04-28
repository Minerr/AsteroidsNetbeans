package dk.sdu.mmmi.cbse.collisiondetection;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IPostEntityProcessingService.class)
public class CollisionSystem implements IPostEntityProcessingService {

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities()) {
            for (Entity otherEntity : world.getEntities()) {
                if (otherEntity != entity && circleCollision(entity, otherEntity)) {
                    entity.collision(otherEntity);
                }
            }
        }
    }

    private boolean circleCollision(Entity e1, Entity e2) {
        boolean b = false;

        // hyp = sqrt((x1-x2)^2 + (y1-y2)^2)
        float x = e1.getX() - e2.getX();
        float y = e1.getY() - e2.getY();
        float hyp = (float) Math.hypot(x, y);

        // If the distance between both entities (hyp) is less than their combined radius, there is a collision.
        if ((e1.getRadius() + e2.getRadius()) > hyp) {
            b = true;
        }

        return b;
    }
}
