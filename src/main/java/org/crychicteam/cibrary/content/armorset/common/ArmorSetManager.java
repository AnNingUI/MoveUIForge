package org.crychicteam.cibrary.content.armorset.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultArmorSet;

import java.util.Optional;

/**
 * This class extends {@link ArmorSetUpdater} and {@link ArmorSetRegistry},
 * <p>
 * serving as the main class to manage armor set functionality. It handles both updates and registry operations.
 * <p>
 * This class itself is a Helper to handle armor sets' normal logic.
 * @author M1hono
 */
public class ArmorSetManager extends ArmorSetUpdater {
    private static ArmorSetManager instance;

    private ArmorSetManager() {
        super();
    }

    /**
     * @deprecated Use {@link Cibrary#ARMOR_SET_MANAGER}
     * @return ArmorSetManager instance
     */
    public static ArmorSetManager getInstance() {
        if (instance == null) {
            instance = new ArmorSetManager();
        }
        return instance;
    }

    /**
     * Retrieves the active armor set for the specified player.
     *
     * @param player The player whose active armor set is to be retrieved.
     * @return The active armor set {@link ArmorSet} of the player, or the default armor set {@link DefaultArmorSet} if none is active.
     */
    public ArmorSet getActiveArmorSet(Player player) {
        return player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY)
                .map(IArmorSetCapability::getActiveSet)
                .orElse(defaultArmorSet);
    }

    /**
     * Retrieves the state of the active armor set for the specified player.
     *
     * @param player The player whose armor set state is to be retrieved.
     * @return The state {@link ArmorSet.State} of the player's active armor set, or {@link ArmorSet.State#NORMAL} if none is active.
     */
    public ArmorSet.State getArmorSetState(Player player) {
        return getArmorSetCapability(player).map(IArmorSetCapability::getState).orElse(ArmorSet.State.NORMAL);
    }

    /**
     * Sets the state of the active armor set for the specified player.
     *
     * @param player The player whose armor set state is to be set.
     * @param state The state {@link ArmorSet.State} to set for the player's active armor set.
     */
    public void setArmorSetState(Player player, ArmorSet.State state) {
        getArmorSetCapability(player).ifPresent(cap -> cap.setState(state));
    }

    /**
     * Retrieves the skill state of the active armor set for the specified player.
     *
     * @param player The player whose armor set skill state is to be retrieved.
     * @return The skill state {@link String} of the player's active armor set, or "none" if none is active.
     */
    public String getArmorSetSkillState(Player player) {
        return getArmorSetCapability(player).map(IArmorSetCapability::getSkillState).orElse("none");
    }

    /**
     * Sets the skill state of the active armor set for the specified player.
     *
     * @param player The player whose armor set skill state is to be set.
     * @param skillState The skill state {@link String} to set for the player's active armor set.
     */
    public void setArmorSetSkillState(Player player, String skillState) {
        getArmorSetCapability(player).ifPresent(cap -> cap.setSkillState(skillState));
    }

    /**
     * Checks if the specified item is part of the active armor set for the specified player.
     * Used normally in Tooltip Events.
     *
     * @param player The player whose active armor set is to be checked.
     * @param item The item to check if it is part of the player's active armor set.
     * @return True if the item is part of the player's active armor set, false otherwise.
     */
    public boolean isItemInActiveSet(Player player, Item item) {
        return getArmorSetCapability(player)
                .map(cap -> cap.isItemInActiveSet(item.getDefaultInstance()))
                .orElse(false);
    }

    /**
     * Retrieves the armor set associated with the specified item for the specified player.
     * Used normally in Interaction Event.
     *
     * @param player The player whose armor set is to be retrieved.
     * @param item The item to check for the associated armor set.
     * @return The armor set associated with the specified item, or the default armor set if none is found.
     */
    public ArmorSet getSetForItem(Player player, Item item) {
        return getArmorSetCapability(player)
                .map(cap -> cap.getSetForItem(item.getDefaultInstance()))
                .orElse(defaultArmorSet);
    }

    /**
     * Retrieves the armor set capability for the specified player.
     *
     * @param player The player whose armor set capability is to be retrieved.
     * @return An Optional containing the player's armor set capability, or an empty Optional if not present.
     */
    private Optional<IArmorSetCapability> getArmorSetCapability(Player player) {
        return player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY).resolve();
    }
}