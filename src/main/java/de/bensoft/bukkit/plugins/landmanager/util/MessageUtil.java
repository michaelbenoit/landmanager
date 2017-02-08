/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class MessageUtil {

    private static final String DEFAULT_LANG = "en";
    private static final String BUNDLE_NAME = "messages";

    private static final Map<String, ResourceBundle> bundleMap = new HashMap<>();

    private static ResourceBundle getBundle(final String language) {
        if (bundleMap.containsKey(language)) {
            return bundleMap.get(language);
        } else {
            final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                    BUNDLE_NAME,
                    new Locale(language),
                    MessageUtil.class.getClassLoader());

            bundleMap.put(language, resourceBundle);
            return resourceBundle;
        }
    }

    public static String translateMessage(Player player, Message message, String... replacements) {
        final ResourceBundle resourceBundle = getBundle(getPlayerLanguage(player));

        final String msg;
        try {
            msg = resourceBundle.getString(message.name());
        } catch (MissingResourceException e) {
            return "???UNABLE_TO_GET_I18N_VAL???";
        }

        if (replacements != null) {
            return MessageFormat.format(msg, replacements);
        } else {
            return msg;
        }
    }

    public static String translateMessage(CommandSender commandSender, Message message, String... replacements) {

        if (commandSender instanceof Player) {
            return translateMessage((Player) commandSender, message, replacements);
        } else {
            return translateMessage(message, replacements);
        }

    }

    public static String translateMessage(Message message, String... replacements) {
        final String msg;
        try {
            msg = getBundle(DEFAULT_LANG).getString(message.name());
        } catch (MissingResourceException e) {
            return "???UNABLE_TO_GET_I18N_VAL???";
        }
        if (replacements != null) {
            return MessageFormat.format(msg, replacements);
        } else {
            return msg;
        }
    }

    private static String getPlayerLanguage(Player p) {
        try {
            final Method m = getMethod("getHandle", p.getClass());
            final Object handle = m.invoke(p, (Object[]) null);
            final Field fLocale = handle.getClass().getDeclaredField("locale");
            fLocale.setAccessible(true);
            return (String) fLocale.get(handle);
        } catch (Exception e) {
            return DEFAULT_LANG;
        }
    }

    private static Method getMethod(String name, Class<?> clazz) {
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name))
                return m;
        }
        return null;
    }
}
