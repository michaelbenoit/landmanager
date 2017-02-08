/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class LandWorld implements Serializable {

    private String name;
    private List<Land> lands;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Land> getLands() {
        if (lands == null) {
            lands = new ArrayList<>();
        }
        return lands;
    }

    public void setLands(List<Land> lands) {
        this.lands = lands;
    }


}
