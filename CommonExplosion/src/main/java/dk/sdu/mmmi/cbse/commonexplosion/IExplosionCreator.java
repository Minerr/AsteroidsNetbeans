/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.commonexplosion;

/**
 *
 * @author Martin
 */
public interface IExplosionCreator {
    Explosion createExplosion(float x, float y, float radius);
}
