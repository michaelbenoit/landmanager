/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.handler;

import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.model.LandStatus;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import de.bensoft.bukkit.plugins.landmanager.model.SaveState;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by michaelbenoit on 08.02.17.
 */
public class ChunkHandler {

    private final Queue<Chunk> chunksToProcess = new ConcurrentLinkedQueue<>();

    private final int updatePeriod;

    public ChunkHandler() {
        updatePeriod = ConfigUtil.getUpdatePeriod();
        Bukkit.getScheduler().scheduleSyncDelayedTask(LandManager.getInstance(), new ChunkProcessor(), updatePeriod);
    }

    public void add(Chunk chunk) {
        chunksToProcess.offer(chunk);
    }

    public void addAll(Chunk[] chunks) {
        for (Chunk chunk : chunks) {
            chunksToProcess.offer(chunk);
        }
    }

    public void addAllOfWorlds(Collection<World> worlds) {
        for (final World world : worlds) {
            addAll(world.getLoadedChunks());
        }
    }

    public class ChunkProcessor implements Runnable {

        final LandManager landManager = LandManager.getInstance();
        final LandManagerModel model = landManager.getModel();

        private int countRemoved = 0;
        private int countAdded = 0;

        private void handleChunk(final Chunk chunk) {
            final World world = chunk.getWorld();
            Land land = ModelUtil.findLandByChunk(model, chunk);

            if (ConfigUtil.isWorldEnabled(world)) {

                // Create land if not existing yet
                if (land == null) {
                    LandWorld landWorld = ModelUtil.getLandWorldByWorld(model, world);
                    if (landWorld == null) {
                        landWorld = new LandWorld();
                        landWorld.setName(world.getName());
                        model.getLandWorlds().add(landWorld);
                    }

                    land = new Land();
                    land.setName(ModelUtil.getLandNameByChunk(chunk));
                    land.setStartX(chunk.getX());
                    land.setStartZ(chunk.getZ());
                    land.setLandStatus(LandStatus.AVAILABLE);
                    land.setLandWorld(landWorld);

                    final SaveState initialSaveState = SaveState.createInitial(chunk, land);
                    land.getSaveStates().add(initialSaveState);

                    landWorld.getLands().add(land);

                    countAdded++;
                }

            } else {
                // Remove the land if the world is disabled
                if (land != null) {
                    final LandWorld landWorld = ModelUtil.getLandWorldByWorld(model, world);
                    landWorld.getLands().remove(land);

                    countRemoved++;
                }
            }
        }

        @Override
        public void run() {
            while (!chunksToProcess.isEmpty()) {
                handleChunk(chunksToProcess.poll());
            }

            if (countAdded != 0) {
                LandManager.getInstance().getLogger().info("Added " + countAdded + " chunks!");
            }
            if (countRemoved != 0) {
                LandManager.getInstance().getLogger().info("Removed " + countRemoved + " chunks!");
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(LandManager.getInstance(), new ChunkProcessor(),
                    updatePeriod);
        }
    }

}
