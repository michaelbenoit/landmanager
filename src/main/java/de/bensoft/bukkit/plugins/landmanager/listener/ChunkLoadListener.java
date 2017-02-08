/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.listener;

import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.model.LandStatus;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import de.bensoft.bukkit.plugins.landmanager.model.ModelUtil;
import de.bensoft.bukkit.plugins.landmanager.model.SaveState;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class ChunkLoadListener implements Listener {

    @EventHandler
    public void onChunkLoaded(final ChunkLoadEvent chunkLoadEvent) {
        handleChunk(chunkLoadEvent.getChunk());
    }

    public void handleChunk(Chunk chunk) {
        final LandManager landManager = LandManager.getInstance();
        final LandManagerModel model = landManager.getModel();

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
            }

        } else {
            // Remove the land if the world is disabled
            if (land != null) {
                final LandWorld landWorld = ModelUtil.getLandWorldByWorld(model, world);
                landWorld.getLands().remove(land);
            }
        }
    }

}
