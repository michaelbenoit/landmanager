/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.security;

/**
 * Created by michaelbenoit on 29.01.17.
 */
public enum Permissions {

    BUY("buy"),
    INFO("info"),
    INIT_WORLD("initWorld"),
    REGEN("regen"),
    CLEAR("clear"),
    SELL("sell"),
    RESET("reset"),
    RESET_WORLD("resetworld"),
    BACKUP("backup");


    private final String value;
    public static final String PERMISSION_PREFIX = "landmanager.";


    Permissions(String value) {
        this.value = PERMISSION_PREFIX + value;
    }

    public String getValue() {
        return value;
    }
}
