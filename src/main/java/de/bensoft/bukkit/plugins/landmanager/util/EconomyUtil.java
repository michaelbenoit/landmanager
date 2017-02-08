/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.util;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Created by michaelbenoit on 01.02.17.
 */
public class EconomyUtil {

    private final Economy economy;

    private static EconomyUtil instance;

    public static EconomyUtil getInstance() {
        if (instance == null) {
            instance = new EconomyUtil();
        }
        return instance;
    }

    private EconomyUtil() {
        if(!Bukkit.getPluginManager().isPluginEnabled("iConomy")) {
            economy = null;
            return;
        }

        final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager()
                .getRegistration(Economy.class);
        economy = rsp.getProvider();
    }

    public boolean isAvailable() {
        return economy != null;
    }

    public boolean moneyAvailable(final Player player, int amount) {
        if (!isAvailable()) {
            return false;
        }
        return economy.has(player, amount);
    }

    public String getCurrencyNamePlural() {
        return economy.currencyNamePlural();
    }

    public boolean withdrawPlayer(final Player player, int amount) {
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

}
