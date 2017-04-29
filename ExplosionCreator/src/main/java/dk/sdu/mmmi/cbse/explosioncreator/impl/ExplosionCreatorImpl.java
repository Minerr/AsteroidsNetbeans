/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.explosioncreator.impl;

import dk.sdu.mmmi.cbse.commonexplosion.Explosion;
import dk.sdu.mmmi.cbse.commonexplosion.IExplosionCreator;

/**
 *
 * @author Martin
 */
public class ExplosionCreatorImpl implements IExplosionCreator{

    @Override
    public Explosion createExplosion(float x, float y, float radius) {
        System.out.println("Creating explosion using OSGi Dependency Injection");
        Explosion explosion = new Explosion();
        explosion.setX(x);
        explosion.setY(y);
        explosion.setRadius(radius / 2);
        explosion.setExpiration(1.5f);

        return explosion;
    }
    
}
