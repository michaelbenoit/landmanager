/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land;

import de.bensoft.bukkit.plugins.landmanager.cmd.Permission;
import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.security.Permissions;
import de.bensoft.bukkit.plugins.landmanager.util.CmdUtil;
import de.bensoft.bukkit.plugins.landmanager.util.LandUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michaelbenoit on 01.02.17.
 */
@Permission(Permissions.SELL)
public class SellCmd implements SubCommand {

    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        final Player player = CmdUtil.ensureAndGetPlayer(commandSender);
        if (player == null) {
            return false;
        }

        final int price;
        try {
            price = Integer.parseInt(params[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        final Land land = LandUtil.getLandAtPlayerPosition(player);
        land.sell(player, price);
        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("sell");
    }
}
