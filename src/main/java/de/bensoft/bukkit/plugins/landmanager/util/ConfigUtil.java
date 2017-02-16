/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.util;

import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class ConfigUtil {


    public static void addDefaults() {
        LandManager landManager = LandManager.getInstance();
        final FileConfiguration config = landManager.getConfig();
        config.options().copyDefaults(true);
        config.options().header("** LANDMANAGER **");

        // worlds
        for (final World world : Bukkit.getWorlds()) {
            config.addDefault("worlds." + world.getName() + ".enabled", false);
        }

        config.addDefault("updatePeriod", 20);

        config.addDefault("price.available", 500);
        config.addDefault("price.transfareable", 800);

        config.addDefault("dynmap.enabled", false);
        config.addDefault("dynmap.updatePeriod", 30);

        landManager.saveConfig();
    }

    public static int getPriceAvailable() {
        return LandManager.getInstance().getConfig().getInt("price.available");
    }

    public static int getPriceTransfareable() {
        return LandManager.getInstance().getConfig().getInt("price.transfareable");
    }

    public static boolean isWorldEnabled(World world) {
        final String name = world.getName();
        return LandManager.getInstance().getConfig().getBoolean("worlds." + name + ".enabled");
    }

    public static boolean isWorldEnabled(LandWorld world) {
        final String name = world.getName();
        return LandManager.getInstance().getConfig().getBoolean("worlds." + name + ".enabled");
    }

    public static boolean isDynmapEnabled() {
        return LandManager.getInstance().getConfig().getBoolean("dynmap.enabled");
    }

    public static int getDynmapUpdatePeriod() {
        return LandManager.getInstance().getConfig().getInt("dynmap.updatePeriod");
    }

    public static int getUpdatePeriod() {
        return LandManager.getInstance().getConfig().getInt("updatePeriod");
    }
}
