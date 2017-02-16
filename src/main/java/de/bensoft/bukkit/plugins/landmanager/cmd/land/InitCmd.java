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
import java.util.logging.Level;

/**
 * Created by michaelbenoit on 16.02.17.
 */
@Permission(Permissions.INIT)
public class InitCmd implements SubCommand {

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
        return path[0].equals("init");
    }

    private Set<Chunk> getChunksInRadius(Location location, int radius) {
        final Set<Chunk> chunks = new HashSet<>();
        for (double x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for (double z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                chunks.add(location.getWorld().getChunkAt(new Location(location.getWorld(), x, 0, z)));
            }
        }
        return chunks;
    }

    private void handle(final Player player, int radius) {
        final Set<Chunk> chunks = getChunksInRadius(player.getLocation(), radius);

        player.sendMessage(MessageUtil.translateMessage(player, Message.PROCESSING_CHUNKS, chunks.size()));
        sch.schedule(new ChunkProcessor(chunks, new VoidFunction<ChunkProcessor>() {
            @Override
            public void exec(ChunkProcessor chunkProcessor) {
                player.sendMessage(MessageUtil.translateMessage(player, Message.FINISHED_PROCESSING,
                        chunkProcessor.getCountAdded()));
            }
        }), 1, TimeUnit.SECONDS);
    }

    private class ChunkProcessor implements Runnable {

        private final Set<Chunk> chunks;
        private int countAdded;
        private final VoidFunction<ChunkProcessor> postFunction;

        public ChunkProcessor(final Set<Chunk> chunks, final VoidFunction<ChunkProcessor> postFunction) {
            this.chunks = chunks;
            this.postFunction = postFunction;
        }

        @Override
        public void run() {
            try {
                doRun();
            } catch (final Exception e) {
                LandManager.getInstance().getLogger().log(Level.SEVERE, "Error during execution of ChunkProcessor", e);
                throw e;
            }
        }

        private void doRun() {
            final LandManagerModel model = LandManager.getInstance().getModel();
            for (final Chunk chunk : chunks) {
                final Land land = ModelUtil.findLandByChunk(model, chunk);
                if (land == null) {
                    LandWorld landWorld = ModelUtil.getLandWorldByWorld(model, chunk.getWorld());
                    if (landWorld == null) {
                        landWorld = new LandWorld();
                        landWorld.setName(chunk.getWorld().getName());
                        model.getLandWorlds().add(landWorld);
                    }


                    final Land newLand = new Land();
                    newLand.setName(ModelUtil.getLandNameByChunk(chunk));
                    newLand.setLandStatus(LandStatus.AVAILABLE);
                    newLand.setStartX(chunk.getX());
                    newLand.setStartZ(chunk.getZ());
                    newLand.setLandWorld(landWorld);

                    final SaveState initialSaveState = SaveState.createInitial(chunk, newLand);
                    newLand.getSaveStates().add(initialSaveState);

                    landWorld.getLands().add(newLand);
                    countAdded++;
                }
            }

            if (postFunction != null) {
                postFunction.exec(this);
            }
        }

        public int getCountAdded() {
            return countAdded;
        }
    }
}
