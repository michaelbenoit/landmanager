/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land.dynmap;

import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import org.bukkit.command.CommandSender;

/**
 * Created by michaelbenoit on 06.02.17.
 */
public class DynmapReloadCommand implements SubCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        LandManager.getInstance().getDynmapHandler().reload();
        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("reload");
    }
}
