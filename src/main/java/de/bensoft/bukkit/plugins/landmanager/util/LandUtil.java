/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.util;

import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

/**
 * Created by michaelbenoit on 01.02.17.
 */
public final class LandUtil {


    public static Land getLandAtPlayerPosition(Player player) {
        final LandManager landManager = LandManager.getInstance();
        final LandManagerModel model = landManager.getModel();
        final Chunk currentChunk = player.getWorld().getChunkAt(player.getLocation());
        return ModelUtil.findLandByChunk(model, currentChunk);
    }
}
