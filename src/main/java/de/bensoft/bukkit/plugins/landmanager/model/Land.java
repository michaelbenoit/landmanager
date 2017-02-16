/************************************************
 * ************   COPYRIGHT 2015     *************
 * ************  BY MICHAEL BENOIT   *************
 * ************ www.michaelbenoit.de *************
 ************************************************/
package de.bensoft.bukkit.plugins.landmanager.model;

import de.bensoft.bukkit.plugins.landmanager.util.MessageUtil;
import de.bensoft.bukkit.plugins.landmanager.exception.LandManagerException;
import de.bensoft.bukkit.plugins.landmanager.util.ConfigUtil;
import de.bensoft.bukkit.plugins.landmanager.util.EconomyUtil;
import de.bensoft.bukkit.plugins.landmanager.util.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelbenoit on 31.01.17.
 */
public class Land implements Serializable {

    private String name;
    private double startX;
    private double startZ;
    private String owner;
    private LandStatus landStatus;
    private int sellPrice;
    private LandWorld landWorld;

    private List<SaveState> saveStates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartZ() {
        return startZ;
    }

    public void setStartZ(double startZ) {
        this.startZ = startZ;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public LandStatus getLandStatus() {
        return landStatus;
    }

    public void setLandStatus(LandStatus landStatus) {
        this.landStatus = landStatus;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public LandWorld getLandWorld() {
        return landWorld;
    }

    public void setLandWorld(LandWorld landWorld) {
        this.landWorld = landWorld;
    }

    public void setSaveStates(List<SaveState> saveStates) {
        this.saveStates = saveStates;
    }

    public List<SaveState> getSaveStates() {
        if (saveStates == null) {
            saveStates = new ArrayList<>();
        }
        return saveStates;
    }

    public boolean canBuild(Player player) {
        if (player.getName().equals(owner)) {
            return true;
        }
        return false;
    }

    public int getPriceForPlayer(final Player player) {

        if (player == null) {
            return 0;
        }

        if (!EconomyUtil.getInstance().isAvailable()) {
            return 0;
        }

        final int price;
        switch (getLandStatus()) {
            case AVAILABLE:
                price = ConfigUtil.getPriceAvailable();
                break;
            case TRANSFAREABLE:
                price = ConfigUtil.getPriceTransfareable();
                break;
            case BUYABLE:
                price = getSellPrice();
                break;
            default:
                price = 0;
        }

        return price;
    }

    public void buy(Player player) {

        if (!getLandStatus().equals(LandStatus.AVAILABLE) &&
                !getLandStatus().equals(LandStatus.TRANSFAREABLE) &&
                !getLandStatus().equals(LandStatus.BUYABLE)) {
            player.sendMessage(MessageUtil.translateMessage(player, Message.NOT_FOR_SALE));
        } else {

            final int price = getPriceForPlayer(player);
            if (price > 0) {

                if (!EconomyUtil.getInstance().moneyAvailable(player, price)) {
                    player.sendMessage(MessageUtil.translateMessage(player, Message.NOT_ENOUGH_MONEY,
                            price + " " + EconomyUtil.getInstance().getCurrencyNamePlural()));
                } else {
                    doBuy(player, price);
                }

            } else {
                doBuy(player, price);
            }

        }
    }

    private void doBuy(Player player, int price) {

        if (price != 0) {
            if (!EconomyUtil.getInstance().withdrawPlayer(player, price)) {
                throw new LandManagerException("Unable to withdraw player!");
            }
        }

        setOwner(player.getName());
        setLandStatus(LandStatus.UNAVAILABLE);
        player.sendMessage(MessageUtil.translateMessage(player, Message.LAND_BOUGHT, getName()));
    }


    public void sell(Player player, int price) {
        if (!player.getName().equals(owner)) {
            player.sendMessage(MessageUtil.translateMessage(player, Message.ONLY_OWNER));
            return;
        }

        setSellPrice(price);
        setLandStatus(LandStatus.BUYABLE);
    }

    public void resetToInitial() {
        SaveState saveState = null;
        for (SaveState st : getSaveStates()) {
            if (st.getSaveStateType().equals(SaveStateType.INITIAL)) {
                saveState = st;
                break;
            }
        }

        if (saveState == null) {
            throw new LandManagerException("No initial save state available!!");
        }

        saveState.restore();
    }

    public void backup(final String name) {
        final SaveState saveState = SaveState.create(this, name);
        getSaveStates().add(saveState);
    }


    public String getInfoMessage(final Player player) {
        return getInfoMessage(player, true);
    }

    public String getInfoMessage() {
        return getInfoMessage(null, false);
    }

    private String getInfoMessage(Player player, boolean colored) {
        final String clYellow = colored ? ChatColor.YELLOW.toString() : "";
        final StringBuilder sb = new StringBuilder();

        if (colored) {
            sb.append(clYellow + "###############################\n");
        }

        sb.append(clYellow + MessageUtil.translateMessage(player, Message.LAND_INFO_NAME, getName()) + "\n");

        if (!StringUtils.isEmpty(getOwner())) {
            sb.append(clYellow + MessageUtil.translateMessage(player, Message.LAND_INFO_OWNER, getOwner()) + "\n");
        }

        final int price = getPriceForPlayer(player);
        if (price != 0) {
            sb.append(clYellow + MessageUtil.translateMessage(player, Message.LAND_INFO_SELL, price) + "\n");
        }

        if (colored) {
            sb.append(clYellow + "###############################");
        }


        return sb.toString();
    }
}
