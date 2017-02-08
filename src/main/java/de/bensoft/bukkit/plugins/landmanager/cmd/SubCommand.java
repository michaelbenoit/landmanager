/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd;

import org.bukkit.command.CommandSender;

/**
 * Created by michaelbenoit on 18.01.17.
 */
public interface SubCommand {

    boolean execute(CommandSender commandSender, String[] params);

    boolean isResponsilbe(String[] path);
}
