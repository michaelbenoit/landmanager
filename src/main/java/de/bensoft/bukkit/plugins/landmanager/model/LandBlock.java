/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import de.bensoft.bukkit.plugins.landmanager.exception.LandManagerException;
import org.bukkit.block.Block;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by michaelbenoit on 01.02.17.
 */
public class LandBlock implements Serializable {

    public static final int SIZE = 4 * 4;

    private int x;
    private int y;
    private int z;
    private int material;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getMaterial() {
        return material;
    }

    public void setMaterial(int material) {
        this.material = material;
    }

    public static LandBlock fromBlock(final Block blk) {
        final LandBlock landBlock = new LandBlock();
        landBlock.setX(blk.getX());
        landBlock.setY(blk.getY());
        landBlock.setZ(blk.getZ());
        landBlock.setMaterial(blk.getTypeId());
        return landBlock;
    }

    public byte[] getBytes() {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(intToByteArray(x));
            byteArrayOutputStream.write(intToByteArray(y));
            byteArrayOutputStream.write(intToByteArray(z));
            byteArrayOutputStream.write(intToByteArray(material));
        } catch (IOException e) {
            throw new LandManagerException(e);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static LandBlock fromBytes(final byte[] data) {

        int offset = 0;
        final LandBlock landBlock = new LandBlock();
        landBlock.setX(readInt(offset, data));
        offset += 4;

        landBlock.setY(readInt(offset, data));
        offset += 4;

        landBlock.setZ(readInt(offset, data));
        offset += 4;

        landBlock.setMaterial(readInt(offset, data));


        return landBlock;
    }

    private static int readInt(int offset, byte[] data) {
        return (data[offset + 3] << 24) |
                (data[offset + 2] & 0xFF << 16) |
                (data[offset + 1] & 0xFF << 8) |
                data[offset];
    }

    public static final byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

}
