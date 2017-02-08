/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import org.bukkit.Chunk;

import java.io.Serializable;

/**
 * Created by michaelbenoit on 01.02.17.
 */
public class SaveState implements Serializable {

    private String name;
    private SaveStateType saveStateType;
    private Land land;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SaveStateType getSaveStateType() {
        return saveStateType;
    }

    public void setSaveStateType(SaveStateType saveStateType) {
        this.saveStateType = saveStateType;
    }

    public Land getLand() {
        return land;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    public static SaveState createInitial(final Chunk chunk, final Land land) {
        final SaveState saveState = new SaveState();
        saveState.setName("INITIAL");
        saveState.setSaveStateType(SaveStateType.INITIAL);
        saveState.setLand(land);

        // Save snapshot
        final LandSnapshot landSnapshot = LandSnapshot.create(saveState, chunk);
        landSnapshot.save();

        return saveState;
    }

    public static SaveState create(final Land land, String name) {
        final SaveState saveState = new SaveState();
        saveState.setName(name);
        saveState.setSaveStateType(SaveStateType.CUSTOM);
        saveState.setLand(land);

        final Chunk chunk = ModelUtil.findChunkByLand(land);
        final LandSnapshot landSnapshot = LandSnapshot.create(saveState, chunk);
        landSnapshot.save();

        return saveState;
    }

    public void restore() {
        final LandSnapshot landSnapshot = LandSnapshot.load(this,
                ModelUtil.findChunkByLand(land));
        landSnapshot.restore();

    }

    public void delete() {
        final LandSnapshot landSnapshot = LandSnapshot.load(this,
                ModelUtil.findChunkByLand(land));
        landSnapshot.delete();
        land.getSaveStates().remove(this);
    }
}
