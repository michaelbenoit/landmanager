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
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import de.bensoft.bukkit.plugins.landmanager.cmd.Permission;
import de.bensoft.bukkit.plugins.landmanager.model.SaveState;
import de.bensoft.bukkit.plugins.landmanager.model.SaveStateType;
import de.bensoft.bukkit.plugins.landmanager.util.CmdUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by michaelbenoit on 01.02.17.
 */
@Permission(Permissions.BACKUP)
public class BackupCmd implements SubCommand {

    private final String SUB_CREATE = "create";
    private final String SUB_RESTORE = "restore";
    private final String SUB_DELETE = "delete";

    @Override
    public boolean execute(CommandSender commandSender, String[] params) {

        final Player player = CmdUtil.ensureAndGetPlayer(commandSender);
        if (player == null) {
            return false;
        }

        if (params.length != 3) {
            return false;
        }

        final Land land = LandUtil.getLandAtPlayerPosition(player);

        if (land == null) {
            player.sendMessage(MessageUtil.translateMessage(player, Message.NO_LAND_FOUND));
        }

        final String subCmd = params[1];
        final String name = params[2];
        if (subCmd.equals(SUB_CREATE)) {
            land.backup(name);
        } else if (subCmd.equals(SUB_RESTORE)) {
            SaveState saveState = null;
            for (final SaveState ss : land.getSaveStates()) {
                if (ss.getSaveStateType().equals(SaveStateType.CUSTOM) && ss.getName().equals(name)) {
                    saveState = ss;
                    break;
                }
            }

            if (saveState == null) {
                player.sendMessage(MessageUtil.translateMessage(
                        player, Message.NO_SAVESTATE, name));
            } else {
                saveState.restore();
            }
        } else if (subCmd.equals(SUB_DELETE)) {

            SaveState saveState = null;
            for (final SaveState ss : land.getSaveStates()) {
                if (ss.getSaveStateType().equals(SaveStateType.CUSTOM) && ss.getName().equals(name)) {
                    saveState = ss;
                    break;
                }
            }

            if (saveState == null) {
                player.sendMessage(MessageUtil.translateMessage(
                        player, Message.NO_SAVESTATE, name));
            } else {
                saveState.delete();
            }
        }

        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return  path[0].equals("backup");
    }
}
