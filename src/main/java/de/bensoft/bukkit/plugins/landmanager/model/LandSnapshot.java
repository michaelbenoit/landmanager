/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import de.bensoft.bukkit.plugins.landmanager.exception.LandManagerException;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * Created by michaelbenoit on 01.02.17.
 */
public class LandSnapshot implements Serializable {

    private int BLK_SIZE = 4 + 1;

    private final LandManager landManager;
    private final File file;
    private byte[] data;
    private final Chunk chunk;

    private LandSnapshot(SaveState saveState, Chunk chunk) {
        this.landManager = LandManager.getInstance();
        this.file = getFile(saveState, chunk);
        this.chunk = chunk;
    }


    public static LandSnapshot create(SaveState saveState, Chunk chunk) {
        final LandSnapshot landSnapshot = new LandSnapshot(saveState, chunk);
        landSnapshot.initFromChunk();
        return landSnapshot;
    }

    public static LandSnapshot load(SaveState saveState, Chunk chunk) {
        final LandSnapshot landSnapshot = new LandSnapshot(saveState, chunk);
        landSnapshot.loadFromDisk();
        return landSnapshot;
    }

    public void save() {

        try {
            final FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            throw new LandManagerException(e);
        }

    }

    public void restore() {
        try {
            final ByteArrayInputStream bis = new ByteArrayInputStream(data);
            final byte[] buffer = new byte[BLK_SIZE];
            for (int y = 0; y < 255; y++) {
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        bis.read(buffer, 0, BLK_SIZE);
                        restoreBlock(x, y, z, buffer);
                    }
                }
            }
            bis.close();
        } catch (IOException e) {
            throw new LandManagerException(e);
        }
    }

    private void restoreBlock(int x, int y, int z, byte[] blkData) {

        int off = 0;
        int matId = readInt(off, blkData);
        off += 4;
        byte data = blkData[off];

        // Set blkData
        final Block block = chunk.getBlock(
                x,
                y,
                z);

        final Material material = Material.getMaterial(matId);
        if(material == null) {
            throw new LandManagerException("Unable to find material with id " + matId);
        }
        block.setType(material);
        block.setData(data);
    }

    private void initFromChunk() {
        this.data = chunkToData(chunk);
    }

    private void loadFromDisk() {
        if (file.exists()) {
            try {
                data = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                throw new LandManagerException(e);
            }
        }
    }

    public void delete() {
        file.delete();
    }


    private byte[] chunkToData(final Chunk chunk) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int y = 0; y < 255; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    try {
                        baos.write(marshalBlock(chunk.getBlock(x, y, z)));
                    } catch (IOException e) {
                        throw new LandManagerException(e);
                    }
                }
            }
        }

        final byte[] res = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            throw new LandManagerException(e);
        }
        return res;
    }

    private byte[] marshalBlock(Block block) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            //byteArrayOutputStream.write(intToByteArray(block.getX()));
            //byteArrayOutputStream.write(intToByteArray(block.getY()));
            //byteArrayOutputStream.write(intToByteArray(block.getZ()));
            byteArrayOutputStream.write(intToByteArray(block.getTypeId()));
            byteArrayOutputStream.write(block.getData());

        } catch (IOException e) {
            throw new LandManagerException(e);
        }
        final byte[] res = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new LandManagerException(e);
        }
        return res;
    }

    private File getFile(final SaveState saveState, final Chunk chunk) {
        final File dataFolder = new File(landManager.getDataFolder(), LandManagerModel.DATA_FOLDER);
        final File worldFolder = new File(dataFolder, chunk.getWorld().getName());
        final File snapshotFolder = new File(worldFolder, "snapshot");
        if (!snapshotFolder.exists()) {
            snapshotFolder.mkdirs();
        }

        return new File(snapshotFolder, ModelUtil.getLandNameByChunk(chunk) +
                "_" + saveState.getName() + ".snapshot");
    }

    private static int readInt(int offset, byte[] data) {
        return (data[offset] << 24) |
                (data[offset + 1] & 0xFF << 16) |
                (data[offset + 2] & 0xFF << 8) |
                data[offset + 3];
    }

    private static final byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >> 24),
                (byte) (value >> 16),
                (byte) (value >> 8),
                (byte) value};
    }
}
