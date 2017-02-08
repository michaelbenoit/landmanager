/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager;

import de.bensoft.bukkit.plugins.landmanager.dynmap.DynmapHandler;
import de.bensoft.bukkit.plugins.landmanager.listener.BlockBuildListener;
import de.bensoft.bukkit.plugins.landmanager.listener.ChunkLoadListener;
import de.bensoft.bukkit.plugins.landmanager.listener.SaveWorldListener;
import de.bensoft.bukkit.plugins.landmanager.model.LandManagerModel;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import de.bensoft.bukkit.plugins.landmanager.cmd.land.LandCommand;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class LandManager extends JavaPlugin {

    private LandManagerModel model;
    private ChunkLoadListener chunkLoadListener;
    private DynmapHandler dynmapHandler;

    private static LandManager instance;

    public static LandManager getInstance() {
        return instance;
    }

    public LandManager() {
        instance = this;
    }

    private void load() {
        this.model = LandManagerModel.loadFromFile(getDataFolder());
    }

    public void save() {
        this.model.saveToFile(getDataFolder());
    }

    @Override
    public void onEnable() {

        // Config defaults
        ConfigUtil.addDefaults();

        load();

        // Handle already loaded chunks
        chunkLoadListener = new ChunkLoadListener();
        for (final World world : getServer().getWorlds()) {
            if (ConfigUtil.isWorldEnabled(world)) {
                for (final Chunk chunk : world.getLoadedChunks()) {
                    chunkLoadListener.handleChunk(chunk);
                }
            }
        }

        // Register
        getCommand("land").setExecutor(new LandCommand());
        getServer().getPluginManager().registerEvents(chunkLoadListener, this);
        getServer().getPluginManager().registerEvents(new SaveWorldListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBuildListener(), this);


        // Dynmap
        dynmapHandler = new DynmapHandler();
        dynmapHandler.load();
    }

    @Override
    public void onDisable() {
        save();
        dynmapHandler.unload();
    }

    public LandManagerModel getModel() {
        return model;
    }

    public ChunkLoadListener getChunkLoadListener() {
        return chunkLoadListener;
    }

    public DynmapHandler getDynmapHandler() {
        return dynmapHandler;
    }
}