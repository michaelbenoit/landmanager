/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.listener;

import de.bensoft.bukkit.plugins.landmanager.LandManager;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class SaveWorldListener implements Listener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent worldSaveEvent) {
        final LandManager landManager = LandManager.getInstance();
        final World world = worldSaveEvent.getWorld();
        landManager.getModel().saveWorld(landManager.getDataFolder(), world);
    }
}
