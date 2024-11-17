package org.crychicteam.cibrary.content.armorset.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.Cibrary;
import org.crychicteam.cibrary.api.registry.ArmorSetRegistry;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.defaults.DefaultArmorSet;

import java.util.Optional;

/**
 * This class extends {@link ArmorSetUpdater},
 * <p>
 * serving as the main class to manage armor set functionality.
 * <p>
 * This class itself is a Helper to handle armor sets' normal logic.
 * @author M1hono
 */
public class ArmorSetManager extends ArmorSetUpdater {
    private static ArmorSetManager instance;
    private static ArmorSet defaultarmorSet;

    private ArmorSetManager() {
        super();
        defaultarmorSet = new DefaultArmorSet();
    }

    /**
     * Use {@link Cibrary#ARMOR_SET_MANAGER}
     * @return ArmorSetManager instance
     */
    public static ArmorSetManager getInstance() {
        if (instance == null) {
            instance = new ArmorSetManager();
        }
        return instance;
    }

    /**
     * Gets the ResourceKey for a given ArmorSet
     *
     * @param armorSet The ArmorSet to get the key for
     * @return The ResourceKey for the ArmorSet, or empty if not found
     */
    public Optional<ResourceKey<ArmorSet>> getKey(ArmorSet armorSet) {
        ResourceLocation id = ArmorSetRegistry.getRegistry().getKey(armorSet);
        if (id == null) return Optional.empty();
        return Optional.of(ResourceKey.create(ArmorSetRegistry.ARMOR_SET_REGISTRY_KEY, id));
    }

    /**
     * Gets an ArmorSet by its ResourceKey
     *
     * @param key The ResourceKey of the ArmorSet
     * @return The ArmorSet, or empty if not found
     */
    public Optional<ArmorSet> getArmorSet(ResourceKey<ArmorSet> key) {
        return Optional.ofNullable(ArmorSetRegistry.getRegistry().getValue(key.location()));
    }

    public ArmorSet getDefaultarmorSet() {
        return defaultarmorSet;
    }

    /**
     * Gets an ArmorSet by its identifier
     *
     * @param identifier The identifier of the ArmorSet (e.g. "modid:armorset_name")
     * @return The ArmorSet, or empty if not found
     */
    public Optional<ArmorSet> getArmorSet(String identifier) {
        try {
            ResourceLocation id = new ResourceLocation(identifier);
            return Optional.ofNullable(ArmorSetRegistry.getRegistry().getValue(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the active armor set for the specified player.
     *
     * @param player The player whose active armor set is to be retrieved.
     * @return The active armor set {@link ArmorSet} of the player, or the empty set if none is active.
     */
    public ArmorSet getActiveArmorSet(Player player) {
        return player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY)
                .map(IArmorSetCapability::getActiveSet)
                .orElse(ArmorSetRegistry.EMPTY_SET.get());
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
     * @return The armor set associated with the specified item, or the empty set if none is found.
     */
    public ArmorSet getSetForItem(Player player, Item item) {
        return getArmorSetCapability(player)
                .map(cap -> cap.getSetForItem(item.getDefaultInstance()))
                .orElse(ArmorSetRegistry.EMPTY_SET.get());
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