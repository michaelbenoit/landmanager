/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land;

import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.cmd.Permission;
import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import de.bensoft.bukkit.plugins.landmanager.security.Permissions;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * Created by michaelbenoit on 01.02.17.
 */
@Permission(Permissions.RESET_WORLD)
public class ResetWorldCmd implements SubCommand {
    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        if (params.length < 2) {
            return false;
        }
        final String sWorld = params[1];

        final World world = Bukkit.getWorld(sWorld);
        if (world == null) {
            commandSender.sendMessage(MessageUtil.translateMessage(commandSender,
                    Message.WORLD_NOT_FOUND, sWorld));
            return true;
        }

        commandSender.sendMessage(MessageUtil.translateMessage(commandSender,
                Message.WORLD_RE_INTIALIZE_STARTED, sWorld));

        final LandManager landManager = LandManager.getInstance();
        final LandManagerModel landManagerModel = landManager.getModel();
        final LandWorld landWorld = ModelUtil.getLandWorldByWorld(landManagerModel, world);
        if (landWorld != null) {
            landManagerModel.getLandWorlds().remove(landWorld);
        }

        if (ConfigUtil.isWorldEnabled(world)) {
            for (final Chunk chunk : world.getLoadedChunks()) {
                landManager.getChunkLoadListener().handleChunk(chunk);
            }
        }

        commandSender.sendMessage(MessageUtil.translateMessage(commandSender,
                Message.WORLD_RE_INITIALIZED, sWorld));

        landManager.save();
        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("resetworld");
    }
}
