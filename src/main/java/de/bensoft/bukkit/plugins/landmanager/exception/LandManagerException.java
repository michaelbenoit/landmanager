/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.exception;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class LandManagerException extends RuntimeException {

    public LandManagerException() {
    }

    public LandManagerException(String message) {
        super(message);
    }

    public LandManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public LandManagerException(Throwable cause) {
        super(cause);
    }

    public LandManagerException(String message, Throwable cause, boolean enableSuppression, boolean
            writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
