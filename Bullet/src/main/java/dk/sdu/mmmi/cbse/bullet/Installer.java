/*
 */
package dk.sdu.mmmi.cbse.bullet;

import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        // TODO
        System.out.println("Start Bullet");
    }

    @Override
    public void uninstalled() {
        super.uninstalled(); //To change body of generated methods, choose Tools | Templates.

    }

}
