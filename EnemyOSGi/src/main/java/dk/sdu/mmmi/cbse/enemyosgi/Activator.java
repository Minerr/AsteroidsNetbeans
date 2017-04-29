package dk.sdu.mmmi.cbse.enemyosgi;

import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        System.out.println("Installed Player module");
        context.registerService(IGamePluginService.class.getName(), new EnemyControlSystem(), null);
    }

    public void stop(BundleContext context) throws Exception {
        System.out.println("Uninstalled Player module");
        context.ungetService(context.getServiceReference(IGamePluginService.class.getName()));
    }

}
