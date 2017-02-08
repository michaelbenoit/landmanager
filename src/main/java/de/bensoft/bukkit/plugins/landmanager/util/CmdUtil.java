/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public final class CmdUtil {

    public static Player ensureAndGetPlayer(CommandSender commandSender) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(MessageUtil.translateMessage(Message.ONLY_PLAYER_CAN_EXECUTE));
            return null;
        }
        return (Player) commandSender;
    }
}
