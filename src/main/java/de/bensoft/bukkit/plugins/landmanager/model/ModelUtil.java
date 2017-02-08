/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public final class ModelUtil {

    public static LandWorld getLandWorldByWorld(LandManagerModel model, World world) {
        for (final LandWorld landWorld : model.getLandWorlds()) {
            if (world.getName().equals(landWorld.getName())) {
                return landWorld;
            }
        }
        return null;
    }

    public static Chunk findChunkByLand(Land land) {
        final World world = Bukkit.getWorld(land.getLandWorld().getName());
        return world.getChunkAt(new Location(world,
                land.getStartX() * 16, 0,
                land.getStartZ() * 16));
    }

    public static Land findLandByChunk(LandManagerModel model, Chunk chunk) {
        final LandWorld landWorld = getLandWorldByWorld(model, chunk.getWorld());
        if (landWorld == null) {
            return null;
        }

        for (final Land land : landWorld.getLands()) {
            if (getLandNameByChunk(chunk).equals(land.getName())) {
                return land;
            }
        }
        return null;
    }

    public static String getLandNameByChunk(final Chunk chunk) {
        return String.format("land_%s_%s", chunk.getX(), chunk.getZ());
    }
}
