/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.explosion;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.commonexplosion.Explosion;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Martin
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class ExplosionProcess implements IEntityProcessingService {

    private float radius;
    
    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities(Explosion.class)) {
            float dt = gameData.getDelta();
            radius = e.getRadius();

            e.reduceExpiration(dt);
            if (e.getExpiration() > 0) {
                radius += Math.sqrt(dt);
                e.setRadius(radius);
            } else {
                System.out.println("Removing explosion");
                world.removeEntity(e);
            }
        }
    }

}
