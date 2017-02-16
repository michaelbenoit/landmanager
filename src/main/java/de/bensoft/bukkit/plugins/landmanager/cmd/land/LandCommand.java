/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land;

import de.bensoft.bukkit.plugins.landmanager.cmd.AbstractMainCommand;
import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.cmd.land.dynmap.DynmapCommand;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class LandCommand extends AbstractMainCommand {

    private static final Set<SubCommand> SUB_COMMAND_LIST = new HashSet<>();

    static {
        SUB_COMMAND_LIST.add(new InfoCmd());
        SUB_COMMAND_LIST.add(new BuyCmd());
        SUB_COMMAND_LIST.add(new ResetWorldCmd());
        SUB_COMMAND_LIST.add(new ResetCmd());
        SUB_COMMAND_LIST.add(new BackupCmd());
        SUB_COMMAND_LIST.add(new DynmapCommand());
        SUB_COMMAND_LIST.add(new RemoveCmd());
        SUB_COMMAND_LIST.add(new InitCmd());

    }

    @Override
    protected Set<SubCommand> getSubCommands() {
        return SUB_COMMAND_LIST;
    }
}
