/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import de.bensoft.bukkit.plugins.landmanager.exception.LandManagerException;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class LandManagerModel implements Serializable {

    public static final String DATA_FOLDER = "data";

    private List<LandWorld> landWorlds;


    public List<LandWorld> getLandWorlds() {
        if (landWorlds == null) {
            landWorlds = new ArrayList<>();
        }
        return landWorlds;
    }

    /**
     * Loads the model from disk in xml format. Or returns a new initialized model if nothing is
     * found on disk;
     *
     * @param folder The folder where the data is contained
     * @return The loaded LandManagerModel
     */
    public static LandManagerModel loadFromFile(File folder) {

        final File dataFolder = new File(folder, DATA_FOLDER);

        if (!dataFolder.exists()) {
            return new LandManagerModel();
        } else {

            final LandManagerModel model = new LandManagerModel();
            final File[] filesInDir = dataFolder.listFiles();
            for (final File file : filesInDir) {

                if (file.isDirectory()) {
                    final String fName = file.getName();
                    final World world = Bukkit.getWorld(fName);
                    if (world != null && ConfigUtil.isWorldEnabled(world)) {
                        final LandWorld lw = loadFromWorldFile(file);
                        if(lw != null) {
                            model.getLandWorlds().add(lw);
                        }
                    } else {
                        file.delete();
                    }
                }
            }
            return model;
        }
    }

    private static LandWorld loadFromWorldFile(File parentDirectory) {
        final File file = new File(parentDirectory, parentDirectory.getName() + ".world");
        if(!file.exists()) {
            return null;
        }

        try {
            final FileInputStream fis = new FileInputStream(file);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            return (LandWorld) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new LandManagerException(e);
        }
    }

    /**
     * Saves the model to disk in xml format
     *
     * @param folder The folder where the data is contained
     */
    public void saveToFile(File folder) {
        for (final LandWorld landWorld : getLandWorlds()) {
            saveWorld(folder, landWorld);
        }

    }

    public void saveWorld(final File folder, final World world) {
        final LandWorld landWorld = ModelUtil.getLandWorldByWorld(
                this, world);

        if (landWorld != null) {
            saveWorld(folder, landWorld);
        }
    }

    public void saveWorld(final File folder, final LandWorld landWorld) {

        final File dataFolder = new File(folder, DATA_FOLDER);
        final File worldFolder = new File(dataFolder, landWorld.getName().replaceAll("\\W+", ""));

        if (!worldFolder.exists()) {
            worldFolder.mkdirs();
        }

        final File file = new File(worldFolder, worldFolder.getName() + ".world");

        if (!ConfigUtil.isWorldEnabled(landWorld) && worldFolder.exists()) {
            worldFolder.delete();
        } else {
            try {
                final FileOutputStream fos = new FileOutputStream(file, false);
                final ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(landWorld);
            } catch (IOException e) {
                throw new LandManagerException(e);
            }

        }
    }

}
