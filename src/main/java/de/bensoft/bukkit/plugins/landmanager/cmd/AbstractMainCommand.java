/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by michaelbenoit on 06.02.17.
 */
public abstract class AbstractMainCommand implements CommandExecutor {

    protected abstract Set<SubCommand> getSubCommands();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 0) {
            return false;
        }

        SubCommand subCommand = null;
        for (final SubCommand sc : getSubCommands()) {
            if (sc.isResponsilbe(strings)) {
                subCommand = sc;
                break;
            }
        }

        if (subCommand == null) {
            return false;
        }

        return subCommand.execute(commandSender, strings);
    }

    private String[] buildPath(String cmd, String[] param) {
        final List<String> path = new LinkedList<>();
        path.add(cmd);
        path.addAll(Arrays.asList(param));
        return path.toArray(new String[path.size()]);
    }
}
