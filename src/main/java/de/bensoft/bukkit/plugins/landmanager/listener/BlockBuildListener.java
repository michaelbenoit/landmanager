/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.listener;

import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class BlockBuildListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent ev) {
        final boolean canBuild = canBuild(ev.getPlayer(),
                ev.getBlock());
        if (!canBuild) {
            ev.getPlayer().sendMessage(MessageUtil.translateMessage(
                    ev.getPlayer(), Message.NOT_ALLOWED_TO_BUILD));
            ev.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent ev) {
        final boolean canBuild = canBuild(ev.getPlayer(),
                ev.getBlock());
        if (!canBuild) {
            ev.getPlayer().sendMessage(MessageUtil.translateMessage(
                    ev.getPlayer(), Message.NOT_ALLOWED_TO_BUILD));
            ev.setCancelled(true);
        }
    }


    private boolean canBuild(Player player, Block blk) {
        final LandManager landManager = LandManager.getInstance();
        final Land land = ModelUtil.findLandByChunk(
                landManager.getModel(),
                blk.getChunk());
        if (land == null) {
            return true;
        } else {
            return land.canBuild(player);
        }
    }
}
