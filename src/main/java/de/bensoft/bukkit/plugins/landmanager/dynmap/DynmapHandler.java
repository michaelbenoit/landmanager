/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.dynmap;

import de.bensoft.bukkit.plugins.landmanager.model.Land;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import de.bensoft.bukkit.plugins.landmanager.LandManager;
import de.bensoft.bukkit.plugins.landmanager.model.LandWorld;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by michaelbenoit on 05.02.17.
 */
public class DynmapHandler {

    private final ScheduledThreadPoolExecutor sch = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);

    private static final String MARKER_SET = "landmanager.markerset";
    private static final String MARKER_SET_NAME = "Landmanager";

    private static final int COLOR_RED = 0xb22e2e;
    private static final int COLOR_GREEN = 0x699b23;
    private static final int COLOR_YELLOW = 0xede21e;
    private static final int COLOR_ORANGE = 0xffc116;
    private static final int COLOR_GREY = 0xbfbfbf;


    private final Logger log;
    private final Server server;
    private int updatePeriod;
    private LandManager landManager;
    private DynmapAPI dynmapAPI;
    private MarkerAPI markerAPI;
    private MarkerSet markerSet;

    public DynmapHandler() {
        this.server = Bukkit.getServer();
        this.landManager = LandManager.getInstance();
        this.log = landManager.getLogger();
    }

    public void load() {

        if (!ConfigUtil.isDynmapEnabled()) {
            return;
        }

        final PluginManager pm = server.getPluginManager();

        /* Get dynmap */
        dynmapAPI = (DynmapAPI) pm.getPlugin("dynmap");
        if (dynmapAPI == null) {
            return;
        }

        markerAPI = dynmapAPI.getMarkerAPI();
        markerSet = markerAPI.getMarkerSet(MARKER_SET);

        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet(
                    MARKER_SET,
                    MARKER_SET_NAME,
                    null,
                    true);
        }


        updatePeriod = ConfigUtil.getDynmapUpdatePeriod();
        sch.schedule(new UpdateJob(), updatePeriod, TimeUnit.SECONDS);

    }

    public void unload() {
        if (dynmapAPI != null) {

        }
    }

    public void reload() {
        if (dynmapAPI != null) {
            sch.schedule(new UpdateJob(), updatePeriod, TimeUnit.SECONDS);
        }
    }

    private void handleLand(final Land land) {

        final double sX = land.getStartX() * 16;
        final double sZ = land.getStartZ() * 16;

        final double[] x = {
                sX, sX + 16
        };
        final double[] z = {
                sZ, sZ + 16
        };

        AreaMarker areaMarker = null;
        for (AreaMarker marker : markerSet.getAreaMarkers()) {
            if (land.getName().equals(marker.getMarkerID())) {
                areaMarker = marker;
                break;
            }
        }

        if (areaMarker == null) {
            areaMarker = markerSet.createAreaMarker(
                    land.getName(),
                    land.getInfoMessage(),
                    false,
                    land.getLandWorld().getName(),
                    x, z,
                    false);
        }


        final int strokeColor = 0xFF0000;

        final int fillColor;
        switch (land.getLandStatus()) {
            case AVAILABLE:
                fillColor = COLOR_GREEN;
                break;
            case TRANSFAREABLE:
                fillColor = COLOR_YELLOW;
                break;
            case BUYABLE:
                fillColor = COLOR_ORANGE;
                break;
            case UNAVAILABLE:
                fillColor = COLOR_RED;
                break;
            default:
                fillColor = COLOR_GREY;
        }

        areaMarker.setLineStyle(1, 0.4, strokeColor);

        areaMarker.setFillStyle(0.4, fillColor);

    }

    private class UpdateJob implements Runnable {


        @Override
        public void run() {
            log.info("Refreshing Landmanager dynmap");

            markerSet.getAreaMarkers().clear();
            for (final LandWorld landWorld : landManager.getModel().getLandWorlds()) {
                for (final Land land : landWorld.getLands()) {
                    handleLand(land);
                }
            }

            sch.schedule(new UpdateJob(), updatePeriod, TimeUnit.SECONDS);
        }
    }
}
