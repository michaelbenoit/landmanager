/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.cmd.land;

import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.cmd.Permission;
import de.bensoft.bukkit.plugins.landmanager.cmd.SubCommand;
import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.model.LandStatus;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import de.bensoft.bukkit.plugins.landmanager.model.SaveState;
import de.bensoft.bukkit.plugins.landmanager.security.Permissions;
import de.bensoft.bukkit.plugins.landmanager.util.CmdUtil;
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import de.bensoft.bukkit.plugins.landmanager.util.VoidFunction;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by michaelbenoit on 16.02.17.
 */
@Permission(Permissions.REMOVE)
public class RemoveCmd implements SubCommand {

    private final ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);

    @Override
    public boolean execute(CommandSender commandSender, String[] params) {
        final Player player = CmdUtil.ensureAndGetPlayer(commandSender);
        if (player == null) {
            return false;
        }

        final int radius;
        if (params.length != 2) {
            radius = 0;
        } else {
            radius = Integer.parseInt(params[1]);
        }

        handle(player, radius);
        return true;
    }

    @Override
    public boolean isResponsilbe(String[] path) {
        return path[0].equals("remove");
    }

    private Set<Chunk> getChunksInRadius(Location location, int radius) {
        final Set<Chunk> chunks = new HashSet<>();
        for (double x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; x++) {
                chunks.add(location.getWorld().getChunkAt(new Location(location.getWorld(), x, 0, z)));
            }
        }
        return chunks;
    }

    private void handle(final Player player, int radius) {
        final Set<Chunk> chunks = getChunksInRadius(player.getLocation(), radius);

        player.sendMessage(MessageUtil.translateMessage(player, Message.PROCESSING_CHUNKS, chunks.size()));
        sch.schedule(new RemoveProcessor(chunks, new VoidFunction<RemoveProcessor>() {
            @Override
            public void exec(RemoveProcessor chunkProcessor) {
                player.sendMessage(MessageUtil.translateMessage(player, Message.FINISHED_PROCESSING,
                        chunkProcessor.getCountRemoved()));
            }
        }), 1, TimeUnit.SECONDS);
    }

    private class RemoveProcessor implements Runnable {

        private final Set<Chunk> chunks;
        private int countRemoved;
        private final VoidFunction<RemoveProcessor> postFunction;

        public RemoveProcessor(final Set<Chunk> chunks, final VoidFunction<RemoveProcessor> postFunction) {
            this.chunks = chunks;
            this.postFunction = postFunction;
        }

        @Override
        public void run() {
            final LandManagerModel model = LandManager.getInstance().getModel();
            for (final Chunk chunk : chunks) {
                final Land land = ModelUtil.findLandByChunk(model, chunk);
                if (land != null) {
                    for (final SaveState saveState : land.getSaveStates()) {
                        saveState.delete();
                    }
                    land.getLandWorld().getLands().remove(land);
                    countRemoved++;
                }
            }

            if (postFunction != null) {
                postFunction.exec(this);
            }
        }

        public int getCountRemoved() {
            return countRemoved;
        }
    }
}
