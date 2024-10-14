package org.crychicteam.cibrary.content.armorset.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.crychicteam.cibrary.content.armorset.ArmorSet;
import org.crychicteam.cibrary.content.armorset.capability.ArmorSetCapability;
import org.crychicteam.cibrary.content.armorset.capability.IArmorSetCapability;

import java.util.Optional;

public class ArmorSetManager extends ArmorSetUpdater {
    private static ArmorSetManager instance;

    private ArmorSetManager() {
        super();
    }

    public static ArmorSetManager getInstance() {
        if (instance == null) {
            instance = new ArmorSetManager();
        }
        return instance;
    }

    public ArmorSet getActiveArmorSet(Player player) {
        return player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY)
                .map(IArmorSetCapability::getActiveSet)
                .orElse(defaultArmorSet);
    }

    public ArmorSet.State getArmorSetState(Player player) {
        return getArmorSetCapability(player).map(IArmorSetCapability::getState).orElse(ArmorSet.State.NORMAL);
    }

    public void setArmorSetState(Player player, ArmorSet.State state) {
        getArmorSetCapability(player).ifPresent(cap -> cap.setState(state));
    }

    public String getArmorSetSkillState(Player player) {
        return getArmorSetCapability(player).map(IArmorSetCapability::getSkillState).orElse("none");
    }

    public void setArmorSetSkillState(Player player, String skillState) {
        getArmorSetCapability(player).ifPresent(cap -> cap.setSkillState(skillState));
    }

    public boolean isItemInActiveSet(Player player, Item item) {
        return getArmorSetCapability(player)
                .map(cap -> cap.isItemInActiveSet(item.getDefaultInstance()))
                .orElse(false);
    }

    public ArmorSet getSetForItem(Player player, Item item) {
        return getArmorSetCapability(player)
                .map(cap -> cap.getSetForItem(item.getDefaultInstance()))
                .orElse(defaultArmorSet);
    }

    private Optional<IArmorSetCapability> getArmorSetCapability(Player player) {
        return player.getCapability(ArmorSetCapability.ARMOR_SET_CAPABILITY).resolve();
    }
}