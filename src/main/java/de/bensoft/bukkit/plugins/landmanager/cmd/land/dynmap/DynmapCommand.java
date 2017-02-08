/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land.dynmap;

import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.cmd.AbstractMainCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by michaelbenoit on 06.02.17.
 */
public class DynmapCommand extends AbstractMainCommand implements SubCommand {


    private static final Set<SubCommand> SUB_COMMAND_LIST = new HashSet<>();

    static {
        SUB_COMMAND_LIST.add(new DynmapReloadCommand());
    }

    @Override
    protected Set<SubCommand> getSubCommands() {
        return SUB_COMMAND_LIST;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        final List<String> paramLst = new ArrayList<>();
        paramLst.addAll(Arrays.asList(params));
        paramLst.remove(0);
        return this.onCommand(commandSender, null, "dynmap", paramLst.toArray(new String[paramLst.size()]));
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("dynmap");
    }
}
