/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land;

import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.security.Permissions;
import de.bensoft.bukkit.plugins.landmanager.util.LandUtil;
import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import de.bensoft.bukkit.plugins.landmanager.cmd.Permission;
import de.bensoft.bukkit.plugins.landmanager.util.CmdUtil;
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michaelbenoit on 31.01.17.
 */
@Permission(Permissions.BUY)
public class BuyCmd implements SubCommand {


    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        final Player player = CmdUtil.ensureAndGetPlayer(commandSender);
        if (player == null) {
            return false;
        }

        final Land land = LandUtil.getLandAtPlayerPosition(player);

        if (land == null) {
            player.sendMessage(MessageUtil.translateMessage(player, Message.NO_LAND_FOUND));
        } else {
            land.buy(player);
        }

        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("buy");
    }
}
